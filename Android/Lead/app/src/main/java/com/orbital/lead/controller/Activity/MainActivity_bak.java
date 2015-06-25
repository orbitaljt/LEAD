package com.orbital.lead.controller.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomFragmentStatePagerAdapter;
import com.orbital.lead.controller.Fragment.FragmentDetail;
import com.orbital.lead.controller.Fragment.FragmentMainUserJournalList;
import com.orbital.lead.controller.Fragment.NavigationDrawerFragment;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.S3Receiver;
import com.orbital.lead.controller.iosched.SlidingTabLayout;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;


public class MainActivity_bak extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        FragmentMainUserJournalList.OnFragmentInteractionListener,
        FragmentDetail.OnFragmentInteractionListener,
        JournalReceiver.Receiver,
        ObservableScrollViewCallbacks {

    private final String TAG = this.getClass().getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private View mHeaderView;
    private View mToolbarView;
    private int mBaseTranslationY;
    private ViewPager mPager;
    private CustomFragmentStatePagerAdapter mPagerAdapter;
    private FragmentMainUserJournalList mFragmentMainUserJournalList;

    private CustomLogging mLogging;
    private Parser mParser;
    private Logic mLogic;
    //private User mCurrentLoginUser;
    private S3Receiver mReceiver;
    private JournalReceiver mJournalReceiver;
    private JournalService mJournalService;

    private String currentLeadUserID;
    private String currentLoginUsername;
    private String currentLoginPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        this.initLogging(); // initialize logging first
        this.initParser();
        this.initLogic();

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
            mLogging.debug(TAG, "No bundle extra from getIntent()");
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        // Facebook Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Facebook Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction()
        //        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
        //        .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            this.restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    public void initParser(){
        this.mParser = Parser.getInstance();
    }

    public void initLogic(){
        this.mLogic = Logic.getInstance();
    }

    public void restoreActionBar() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        /*
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        */

    }

    public void initToolbar(){
        //this.mToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        this.mToolbarView = findViewById(R.id.custom_toolbar);
    }

    public void pushToolbarToActionbar(){
        setSupportActionBar((Toolbar) this.mToolbarView);
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
            int toolbarHeight = mToolbarView.getHeight();
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

    public void setToolbarTitle(String title){
        //this.mToolbar.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = mToolbarView.getHeight();
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
        int toolbarHeight = mToolbarView.getHeight();

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
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
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
        int toolbarHeight = mToolbarView.getHeight();
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
        mLogging.debug(TAG, "setCurrentUser");
        CurrentLoginUser.setUser(user);
        CurrentLoginUser.getUser().setUsername(this.currentLoginUsername);
        CurrentLoginUser.getUser().setPassword(this.currentLoginPassword);
    }

    public void getUserProfile(String userID){
        this.mLogic.getUserProfile(this, userID);
    }

    public void getUserProfilePicture(String userID){
        this.mLogic.getUserProfilePicture(this, userID);
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
        mLogging.debug(TAG, "setNavigationDrawerUserProfilePicture using url => " + url);
        this.mNavigationDrawerFragment.setImageUserProfile(url);
    }

    public void setNavigationDrawerUserProfilePicture(Drawable drawable, String fileName){
        mLogging.debug(TAG, "setNavigationDrawerUserProfilePicture using drawable");

        this.mNavigationDrawerFragment.setImageUserProfile(drawable);
        //this.mNavigationDrawerFragment.setCurrentUserProfilePictureFilePath(fileName);
    }

    public void setNavigationDrawerUserProfilePicture(Bitmap bmp, String fileName){
        mLogging.debug(TAG, "setNavigationDrawerUserProfilePicture using Bitmap");
        this.mNavigationDrawerFragment.setImageUserProfile(bmp);
        //this.mNavigationDrawerFragment.setCurrentUserProfilePictureFilePath(fileName);
    }

    public void setNavigationDrawerUserName(){
        this.mNavigationDrawerFragment.setTextUserName(this.getCurrentUser().getFirstName(), this.getCurrentUser().getMiddleName(), this.getCurrentUser().getLastName());
    }

    public void setNavigationDrawerUserEmail(){
        this.mNavigationDrawerFragment.setmTextUserEmail(this.getCurrentUser().getEmail());
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
        this.mLogic.getUserJournalList(this,this.getCurrentUser());
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
                this.mLogging.debug(TAG, "onReceiveResult -> S3Service.STATUS_RUNNING");
                break;
            case JournalService.STATUS_FINISHED:
                this.mLogging.debug(TAG, "onReceiveResult -> S3Service.STATUS_FINISHED");
                type = (EnumJournalServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_ALL_JOURNAL:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);

                        this.mLogging.debug(TAG, "onReceiveResult GET_ALL_JOURNAL -> jsonResult => " + jsonResult);
                        JournalList list = this.getJournalListFromJson(jsonResult);

                        this.setUserJournalList(list);
                        this.mLogging.debug(TAG, "onReceiveResult list -> list.size() => " + list.size());
                        this.updateFragmentMainUserJournalList(list);
                        break;
                    case GET_SPECIFIC_JOURNAL:
                        break;
                }

                break;
            case JournalService.STATUS_ERROR:
                this.mLogging.debug(TAG, "S3Service.STATUS_ERROR");
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
        return mParser.parseJsonToJournalList(json);
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
