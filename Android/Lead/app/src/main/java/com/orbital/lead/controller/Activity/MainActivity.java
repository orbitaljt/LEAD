package com.orbital.lead.controller.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomFragmentStatePagerAdapter;
import com.orbital.lead.controller.Fragment.FragmentDetail;
import com.orbital.lead.controller.Fragment.FragmentMainUserJournalList;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.S3Receiver;
import com.orbital.lead.controller.iosched.SlidingTabLayout;
import com.orbital.lead.model.Constant;


import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;


public class MainActivity extends BaseActivity
        implements
        FragmentMainUserJournalList.OnFragmentInteractionListener,
        FragmentDetail.OnFragmentInteractionListener,
        JournalReceiver.Receiver,
        ObservableScrollViewCallbacks {

    private final String TAG = this.getClass().getSimpleName();

    private View mToolbarView;
    private View mHeaderView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private CustomFragmentStatePagerAdapter mPagerAdapter;
    private FragmentMainUserJournalList mFragmentMainUserJournalList;

    private JournalReceiver mJournalReceiver;

    private String currentLeadUserID;
    private String currentLoginUsername;
    private String currentLoginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_main, getBaseFrameLayout());

        this.initToolbar();
        this.pushToolbarToActionbar();
        this.restoreActionBar();
        this.setToolbarTitle(Constant.TITLE_LEAD);

        this.initHeader();
        this.initPagerAdapter();
        this.initViewPager();
        this.initSlidingTabLayout();

        this.initJournalReceiver();


        Bundle getBundleExtra = getIntent().getExtras();

        if(getBundleExtra != null){
            String bundleLeadUserID = getBundleExtra.getString(Constant.BUNDLE_PARAM_LEAD_USER_ID, "");
            String bundleFacebookUserID = getBundleExtra.getString(Constant.BUNDLE_PARAM_FACEBOOK_USER_ID, "");
            String bundleUsername = getBundleExtra.getString(Constant.BUNDLE_PARAM_USERNAME, "");
            String bundlePassword = getBundleExtra.getString(Constant.BUNDLE_PARAM_PASSWORD, "");
            boolean bundleIsFacebookLogin = getBundleExtra.getBoolean(Constant.BUNDLE_PARAM_IS_FACEBOOK_LOGIN, false);

            this.currentLeadUserID = bundleLeadUserID;
            this.currentLoginUsername = bundleUsername;
            this.currentLoginPassword = bundlePassword;

            if(bundleLeadUserID != ""){
                this.getUserProfile(bundleLeadUserID);
            }
        }else{
            getCustomLogging().debug(TAG, "No bundle extra from getIntent()");
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void initToolbar(){
        this.mToolbarView = findViewById(R.id.custom_toolbar);
    }

    public View getToolbar(){
        return this.mToolbarView;
    }

    public void pushToolbarToActionbar(){
        setSupportActionBar((Toolbar) this.getToolbar());
    }

    public void initHeader(){
        this.mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
    }

    public void initPagerAdapter(){
        mPagerAdapter = new CustomFragmentStatePagerAdapter(getSupportFragmentManager());
    }

    public void initViewPager(){
        this.mPager = (ViewPager) findViewById(R.id.pager);
        this.mPager.setAdapter(this.mPagerAdapter);
    }

    public void initSlidingTabLayout(){
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, R.id.tabtext); //android.R.id.text1
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(this.mPager);

        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        propagateToolbarState(toolbarIsShown());

    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = getToolbar().getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = getToolbar().getHeight();
        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = getToolbar().getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {

        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding)
            if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }

        // check out the scroll from here
        // https://github.com/ksoichiro/Android-ObservableScrollView/blob/master/samples/res/layout/fragment_scrollview_noheader.xml

    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -getToolbar().getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = getToolbar().getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }

    /*===================== User profile ================================*/
    public User getCurrentUser(){
        return CurrentLoginUser.getUser();
    }

    public void setCurrentUser(User user){
        getCustomLogging().debug(TAG, "setCurrentUser");
        CurrentLoginUser.setUser(user);
        CurrentLoginUser.getUser().setUsername(this.currentLoginUsername);
        CurrentLoginUser.getUser().setPassword(this.currentLoginPassword);
    }

    public void getUserProfile(String userID){
        this.getLogic().getUserProfile(this, userID);
    }

    public void getUserProfilePicture(String userID){
        this.getLogic().getUserProfilePicture(this, userID);
    }

    public void setUserProfilePicture(String url){
        this.setNavigationDrawerUserProfilePicture(url);
    }

    public void setUserJournalList(JournalList list){
        this.getCurrentUser().setJournalList(list);
    }



    public void setUserProfilePicture(){
        /*
        Bitmap result = this.mLogic.getUserProfilePicture(this, this.getCurrentUser());
        if(result == null){
            //set default empty user profile icon
            mLogging.debug(TAG, "setUserProfilePicture => bitmap null");
            this.setNavigationDrawerUserProfilePicture(getResources().getDrawable(R.drawable.ic_default_user), "");
        }else{
            // there's image in local storage (not image from S3)
            mLogging.debug(TAG, "setUserProfilePicture => bitmap from local storage");
            String fileName = this.getCurrentUser().getProfilePictureID() + Constant.STORAGE_DOT_EXTENSION + this.getCurrentUser().getProfilePictureType().toString();
            this.setNavigationDrawerUserProfilePicture(result, this.mLogic.getLocalStorageProfileDirectory() + fileName);
        }
        */
    }

    public void setNavigationDrawerUserProfilePicture(String url){
        getCustomLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using url => " + url);
        this.getNavigationDrawerFragment().setImageUserProfile(url);
    }

    public void setNavigationDrawerUserProfilePicture(Drawable drawable, String fileName){
        getCustomLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using drawable");

        this.getNavigationDrawerFragment().setImageUserProfile(drawable);
        //this.getNavigationDrawerFragment().setCurrentUserProfilePictureFilePath(fileName);
    }

    public void setNavigationDrawerUserProfilePicture(Bitmap bmp, String fileName){
        getCustomLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using Bitmap");
        this.getNavigationDrawerFragment().setImageUserProfile(bmp);
        //this.getNavigationDrawerFragment().setCurrentUserProfilePictureFilePath(fileName);
    }

    public void setNavigationDrawerUserName(){
        this.getNavigationDrawerFragment().setTextUserName(this.getCurrentUser().getFirstName(), this.getCurrentUser().getMiddleName(), this.getCurrentUser().getLastName());
    }

    public void setNavigationDrawerUserEmail(){
        this.getNavigationDrawerFragment().setmTextUserEmail(this.getCurrentUser().getEmail());
    }

    public void updateFragmentMainUserJournalList(JournalList list){
        getFragmentMainUserJournalList().updateJournalList(list);
    }

    public FragmentMainUserJournalList getFragmentMainUserJournalList(){
        this.mFragmentMainUserJournalList = this.mPagerAdapter.getFragmentMainUserJournalList();
        return this.mFragmentMainUserJournalList;
    }

    /*===================== S3 Intent Service & Receiver ================================*/
/*
    public S3Receiver getS3Receiver(){
        if(this.mReceiver == null){
            this.initS3Receiver();
        }
        return this.mReceiver;
    }

    public void initS3Receiver(){
        mReceiver = new S3Receiver(new Handler());
        //mReceiver.setReceiver(this);
    }
*/

    /*===================== Journal Intent Service & Receiver ================================*/
    public void initJournalReceiver(){
        mJournalReceiver = new JournalReceiver(new Handler());
        mJournalReceiver.setReceiver(this);
    }

    public JournalReceiver getJournalReceiver(){
        if(this.mJournalReceiver == null){
            this.initJournalReceiver();
        }
        return this.mJournalReceiver;
    }

    public void getUserJournalList(){
        this.getLogic().getUserJournalList(this, this.getCurrentUser());
    }

    /**
     * Get result from S3Receiver
     * **/
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumJournalServiceType type = null;
        String jsonResult = "";

        switch (resultCode) {
            case JournalService.STATUS_RUNNING:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_RUNNING");
                break;
            case JournalService.STATUS_FINISHED:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_FINISHED");
                type = (EnumJournalServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_ALL_JOURNAL:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);

                        this.getCustomLogging().debug(TAG, "onReceiveResult GET_ALL_JOURNAL -> jsonResult => " + jsonResult);
                        JournalList list = this.getJournalListFromJson(jsonResult);

                        this.setUserJournalList(list);
                        this.getCustomLogging().debug(TAG, "onReceiveResult list -> list.size() => " + list.size());
                        this.updateFragmentMainUserJournalList(list);
                        break;

                    /*
                    case GET_SPECIFIC_JOURNAL:
                        break;
                   */
                }

                break;
            case JournalService.STATUS_ERROR:
                this.getCustomLogging().debug(TAG, "S3Service.STATUS_ERROR");
                break;
        }


        /*
        EnumS3ServiceType type = null;
        String downloadStatus = "";


        switch (resultCode) {
            case S3Service.STATUS_RUNNING:
                this.mLogging.debug(TAG, "onReceiveResult -> S3Service.STATUS_RUNNING");
                // here can call progressbar

                break;

            case S3Service.STATUS_FINISHED:
                this.mLogging.debug(TAG, "onReceiveResult -> S3Service.STATUS_FINISHED");

                type = (EnumS3ServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
                switch(type){

                    case QUERY_PHOTOS:

                        break;
                    case QUERY_PROFILE_PICTURE:
                        downloadStatus = resultData.getString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_STATUS_TAG);
                        if(downloadStatus.equals(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_SUCCESS_VALUE)){ //success
                            String profilePicFilePath = resultData.getString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_PROFILE_PICTURE_FILENAME_TAG);

                            this.mLogging.debug(TAG, "onReceiveResult -> profilePicFilePath => " + profilePicFilePath);

                            Bitmap bmp = this.mLogic.getLocalStorageLoadProfilePicture(profilePicFilePath);
                            this.setNavigationDrawerUserProfilePicture(bmp, this.mLogic.getLocalStorageProfileDirectory() + profilePicFilePath);

                        }else{ //failure
                            this.mLogging.debug(TAG, "onReceiveResult -> Failed to download image from S3");
                        }
                        break;
                }


                //this.mLogging.debug(TAG, "Return value from S3Service -> " + result);
                break;

            case S3Service.STATUS_ERROR:
                this.mLogging.debug(TAG, "S3Service.STATUS_ERROR");
                break;

        }

        */
    }

    public JournalList getJournalListFromJson(String json){
        return getParser().parseJsonToJournalList(json);
    }


    @Override
    public void onFragmentMainUserJournalListInteraction(Uri uri) {

    }

    @Override
    public void onFragmentDetailInteraction(Uri uri) {

    }


    /*
    @Override
    public void updateFragmentMainUserJournalList(JournalList list) {
        FragmentMainUserJournalList frag = this.mPagerAdapter.getFragmentMainUserJournalList();
        frag.updateJournalList(list);

    }
    */
}
