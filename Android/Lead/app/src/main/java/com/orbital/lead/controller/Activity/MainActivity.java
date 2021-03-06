package com.orbital.lead.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentDetail;
import com.orbital.lead.controller.Fragment.FragmentMainUserJournalList;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.ProjectReceiver;
import com.orbital.lead.model.Constant;


import com.orbital.lead.model.CountryList;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.FacebookUserObject;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;


public class MainActivity extends BaseActivity
        implements
        FragmentMainUserJournalList.OnFragmentInteractionListener,
        FragmentDetail.OnFragmentInteractionListener,
        JournalReceiver.Receiver,
        ProjectReceiver.Receiver{
//,ObservableScrollViewCallbacks
    private final String TAG = this.getClass().getSimpleName();

    private View mToolbarView;
    //private View mHeaderView;
    //private int mBaseTranslationY;
    //private ViewPager mPager;
    //private CustomFragmentStatePagerAdapter mPagerAdapter;
    //private FragmentMainUserJournalList mFragmentMainUserJournalList;

    private JournalReceiver mJournalReceiver;
    //private ProjectReceiver mProjectReceiver;

    private String currentFacebookUserID;
    private String currentLeadUserID;
    private String currentLoginUsername;
    private String currentLoginPassword;
    private String currentFacebookResponse;
    private boolean currentIsFacebookLogin = false;
    private boolean isRegistered = false;

    private FragmentTransaction mFragmentTranscation;
    private FragmentManager mFragmentManager;
    private FragmentMainUserJournalList mFragmentJournalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_main, getBaseFrameLayout());

        this.initToolbar();
        this.pushToolbarToActionbar();
        this.restoreActionBar();
        this.setToolbarTitle(Constant.TITLE_LEAD);
        this.initFragmentManager();
        this.displayFragmentJournalList();

        /*
        this.initHeader();
        this.initPagerAdapter();
        this.initViewPager();
        this.initSlidingTabLayout();
        */


        this.initJournalReceiver();
        this.initProjectReceiver();

        Bundle getBundleExtra = getIntent().getExtras();

        if(getBundleExtra != null){
            this.isRegistered = getBundleExtra.getBoolean(Constant.BUNDLE_PARAM_IS_REGISTERED, false);
            this.currentIsFacebookLogin = getBundleExtra.getBoolean(Constant.BUNDLE_PARAM_IS_FACEBOOK_LOGIN, false);
            this.currentLeadUserID = getBundleExtra.getString(Constant.BUNDLE_PARAM_LEAD_USER_ID, ""); // if lead user is null, will return empty

            this.getFacebookLogic().setIsFacebookLogin(this.currentIsFacebookLogin); // is from base activity


            if(this.getFacebookLogic().getIsFacebookLogin()) { // login using facebook
                //getLogging().debug(TAG, "AccessToken.getCurrentAccessToken().getToken() => " + AccessToken.getCurrentAccessToken().getToken());
                //this.currentFacebookAccessToken = AccessToken.getCurrentAccessToken().getToken(); //get current access token string

                this.currentFacebookUserID = getBundleExtra.getString(Constant.BUNDLE_PARAM_FACEBOOK_USER_ID, "");
                this.currentFacebookResponse = getBundleExtra.getString(Constant.BUNDLE_PARAM_FACEBOOK_RESPONSE, "");

                getLogging().debug(TAG, "currentFacebookResponse => " + currentFacebookResponse);
            }


            if(this.isRegistered){ // is registered with lead
                getLogging().debug(TAG, "OnCreate Current user is registered");
                //getLogging().debug(TAG, "getProfile from this.currentLeadUserID => " + currentLeadUserID);
                this.getProfile(this.currentLeadUserID);
                // update user profile from facebook will be done after getProfile onPostExecute
                // update user profile picture from facebook will be done after getProfile onPostExecute

                if(!this.getFacebookLogic().getIsFacebookLogin()){ // login with lead account
                    this.currentLoginUsername = getBundleExtra.getString(Constant.BUNDLE_PARAM_USERNAME, "");
                    this.currentLoginPassword = getBundleExtra.getString(Constant.BUNDLE_PARAM_PASSWORD, "");
                }

            }else { // not registered with lead
                getLogging().debug(TAG, "OnCreate Current user is not registered!");
                this.setNewCurrentUser();
                this.updateCurrentUserProfileFromFacebook();
                this.createNewUserProfileToDatabase();
            }



        }else{
            getLogging().debug(TAG, "No bundle extra from getIntent()");
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

    public void initFragmentManager() {
        this.mFragmentManager = getSupportFragmentManager();
    }

    public void updateFragmentMainUserJournalList(JournalList list){
        this.getLogging().debug(TAG, "updateFragmentMainUserJournalList");
        this.getFragmentJournalList().updateJournalList(list);
    }

    public void showFragmentEmptyJournalLayout() {
        this.getFragmentJournalList().showEmptyJournalListLayout();
    }

    private void displayFragmentJournalList(){
        this.getLogging().debug(TAG, "displayFragmentAlbum");
        //this,
        this.mFragmentJournalList = FragmentMainUserJournalList.newInstance("", "");
        this.replaceFragment(this.mFragmentJournalList, Constant.FRAGMENT_JOURNAL_LIST_NAME);
    }

    private void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.mFragmentTranscation = this.mFragmentManager.beginTransaction();
            this.mFragmentTranscation
                    .replace(R.id.container, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }

    private FragmentMainUserJournalList getFragmentJournalList(){
        return this.mFragmentJournalList;
    }







    /*
    private TagSet getAllJournalTags(JournalList list) {
        TagSet set = list.getAllTags();
        return set;
    }
    */

    /*
    private String getCurrentFacebookAccessTokenString() {
        return this.currentFacebookAccessToken;
    }
    */


    /*
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
    */




    /*===================== User profile ================================*/
    public User getCurrentUser(){
        return CurrentLoginUser.getUser();
    }

    public void setCurrentUser(User user){
        getLogging().debug(TAG, "setCurrentUser");

        CurrentLoginUser.setUser(user);

        if(!this.currentIsFacebookLogin){ //not login using facebook
            CurrentLoginUser.getUser().setUsername(this.currentLoginUsername);
            CurrentLoginUser.getUser().setPassword(this.currentLoginPassword);
        }
    }

    public void setNewCurrentUser(){
        getLogging().debug(TAG, "setNewCurrentUser");
        User user = new User();
        CurrentLoginUser.setUser(user);
    }

    public void setNewCurrentUserID(String newUserID){
        this.getCurrentUser().setUserID(newUserID);
    }

    public void updateCurrentUserProfileFromFacebook(){
        getLogging().debug(TAG, "updateCurrentUserProfileFromFacebook");
        if(this.currentIsFacebookLogin){ // is login from facebook
            // update all info that can be found from facebook
            // plus profile picture
            FacebookUserObject fbObj = this.getFacebookUserObject(this.currentFacebookResponse);

            if(fbObj != null){
                CurrentLoginUser.getUser().setFacebookID(fbObj.getFacebookID());
                CurrentLoginUser.getUser().setProfilePicUrl(fbObj.getModifiedProfilePictureUrl()); // profile picture will be facebook url
                CurrentLoginUser.getUser().setProfilePictureID(fbObj.getProfilePictureID()); // update profile picture ID
                CurrentLoginUser.getUser().setProfilePictureType(fbObj.getProfilePictureType()); //update profile picture type
                getLogging().debug(TAG, "updateCurrentUserProfileFromFacebook fbObj.getProfilePictureType() => " + fbObj.getProfilePictureType());


                CurrentLoginUser.getUser().setBirthday(FormatDate.parseDate(fbObj.getBirthday(), FormatDate.FACEBOOK_DATE_TO_DATABASE_DATE, null));
                CurrentLoginUser.getUser().setFirstName(fbObj.getFirstName()); // update names
                CurrentLoginUser.getUser().setMiddleName(fbObj.getMiddleName());
                CurrentLoginUser.getUser().setLastName(fbObj.getLastName());
                CurrentLoginUser.getUser().setEmail(fbObj.getEmail()); // update email

            }else{
                getLogging().debug(TAG, "updateCurrentUserProfileFromFacebook FacebookUserObject is null!");
            }
        }
    }

    public void updateCurrentUserProfileToDatabase(){
        String detail = getParser().userObjectToJson(getCurrentUser());
        getLogging().debug(TAG, "updateCurrentUserProfileToDatabase detail => " + detail);
        getLogic().updateUserProfileDatabase(this, this.getCurrentUser().getUserID(), detail);
    }

    public void updateCurrentUserCountryList(CountryList list) {
        getLogging().debug(TAG, "updateCurrentUserCountryList listSize =>" + list.size());
        CurrentLoginUser.getUser().setCountryList(list);
    }

    public void createNewUserProfileToDatabase(){
        String detail = getParser().userObjectToJson(getCurrentUser());
        getLogging().debug(TAG, "createNewUserProfileToDatabase detail => " + detail);
        // new user does not have lead user ID
        getLogic().insertUserProfileDatabase(this, detail);
    }


    public void getProfile(String userID){
        this.getLogic().getUserProfile(this, userID);
    }

    public void setProfilePicture(){
        this.setNavigationDrawerUserProfilePicture(this.getCurrentUser().getProfilePicUrl());
    }


    public void uploadProfilePictureFromFacebook(){
        getLogging().debug(TAG, "uploadProfilePictureFromFacebook");

        String userID = this.getCurrentUser().getUserID();
        String imageUrl = this.getCurrentUser().getProfilePicUrl();
        String fileType = this.getCurrentUser().getProfilePictureType().toString();
        String fileName = getParser().generateFilename(this.getCurrentUser().getProfilePictureID(), fileType);

        this.getLogic().uploadProfilePictureFromFacebook(this, userID, "", imageUrl, fileName, fileType, true, false);
    }



    public void setCurrentUserJournalList(JournalList list){
        this.getCurrentUser().setJournalList(list);
    }

    /*
    public void setProjectList(ProjectList list) {
        this.getCurrentUser().setProjectList(list);
    }
    */

    public void setNavigationDrawerUserProfilePicture(String url){
        getLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using url => " + url);
        this.getNavigationDrawerFragment().setImageUserProfile(url);
    }

    /*
    public void setNavigationDrawerUserProfilePicture(Drawable drawable, String fileName){
        getLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using drawable");

        this.getNavigationDrawerFragment().setImageUserProfile(drawable);
        //this.getNavigationDrawerFragment().setCurrentUserProfilePictureFilePath(fileName);
    }

    public void setNavigationDrawerUserProfilePicture(Bitmap bmp, String fileName){
        getLogging().debug(TAG, "setNavigationDrawerUserProfilePicture using Bitmap");
        this.getNavigationDrawerFragment().setImageUserProfile(bmp);
        //this.getNavigationDrawerFragment().setCurrentUserProfilePictureFilePath(fileName);
    }
    */

    public void setNavigationDrawerUserName(){
        this.getNavigationDrawerFragment().setTextUserName(this.getCurrentUser().getFirstName(), this.getCurrentUser().getMiddleName(), this.getCurrentUser().getLastName());
    }

    public void setNavigationDrawerUserEmail(){
        this.getNavigationDrawerFragment().setTextUserEmail(this.getCurrentUser().getEmail());
    }

    /*
    public void updateFragmentMainUserJournalList(JournalList list){
        getFragmentMainUserJournalList().updateJournalList(list);
    }

    public FragmentMainUserJournalList getFragmentMainUserJournalList(){
        this.mFragmentMainUserJournalList = this.mPagerAdapter.getFragmentMainUserJournalList();
        return this.mFragmentMainUserJournalList;
    }
    */


    /*===================== Intent Service & Receiver ================================*/
    public void initJournalReceiver(){
        this.mJournalReceiver = new JournalReceiver(new Handler());
        this.mJournalReceiver.setReceiver(this);
    }

    /*
    public void initProjectReceiver() {
        this.mProjectReceiver = new ProjectReceiver(new Handler());
        this.mProjectReceiver.setReceiver(this);
    }
    */

    public JournalReceiver getJournalReceiver(){
        if(this.mJournalReceiver == null){
            this.initJournalReceiver();
        }
        return this.mJournalReceiver;
    }

    /*
    public ProjectReceiver getProjectReceiver() {
        if(this.mProjectReceiver == null){
            this.initProjectReceiver();
        }
        return this.mProjectReceiver;
    }
    */

    public void retrieveUserJournalList(){
        this.getLogic().getUserJournalList(this, this.getCurrentUser().getUserID());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle b = null;

        if(resultCode != Activity.RESULT_OK) {
            getLogging().debug(TAG, "resultCode is not OK");
            return;
        }

        if(data == null) {
            getLogging().debug(TAG, "data is null");
            return;
        }

        switch(requestCode) {
            case START_ADD_NEW_SPECIFIC_JOURNAL_ACTIVITY:
                getLogging().debug(TAG, "START_ADD_NEW_SPECIFIC_JOURNAL_ACTIVITY");
                b = data.getExtras();
                if (b != null) {
                    boolean refresh = b.getBoolean(Constant.BUNDLE_PARAM_JOURNAL_LIST_TOGGLE_REFRESH);

                    if(refresh) {
                        // refresh the entire list again
                        getLogging().debug(TAG, "refreshing fragment main journal list");
                        this.updateFragmentMainUserJournalList(this.getCurrentUser().getJournalList());
                    }
                }

                break;

            case START_SPECIFIC_JOURNAL_ACTIVITY:
                getLogging().debug(TAG, "START_SPECIFIC_JOURNAL_ACTIVITY");
                b = data.getExtras();
                if (b != null) {
                    boolean refresh = b.getBoolean(Constant.BUNDLE_PARAM_JOURNAL_TOGGLE_REFRESH);
                    getLogging().debug(TAG, "START_SPECIFIC_JOURNAL_ACTIVITY refresh => " + refresh);
                    if(refresh) {
                        // refresh the entire list again
                        getLogging().debug(TAG, "refreshing fragment main journal list");
                        this.updateFragmentMainUserJournalList(this.getCurrentUser().getJournalList());
                    }
                }
                break;
        }
    }

    /*
    public void retrieveAllProject() {
        this.getLogic().getAllProject(this);
    }
    */

    /**
     * Get result from services
     * **/
    /*
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        EnumJournalServiceType journalServiceType = null;
        EnumProjectServiceType projectServiceType = null;
        String jsonResult = "";

        switch (resultCode) {
            case JournalService.STATUS_RUNNING:
                this.getLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_RUNNING");
                break;


            case ProjectService.STATUS_RUNNING:
                this.getLogging().debug(TAG, "onReceiveResult -> ProjectService.STATUS_RUNNING");
                break;

            case JournalService.STATUS_FINISHED:
                this.getLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_FINISHED");
                journalServiceType = (EnumJournalServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(journalServiceType){
                    case GET_ALL_JOURNAL:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult GET_ALL_JOURNAL -> jsonResult => " + jsonResult);
                        JournalList list = this.getJournalListFromJson(jsonResult);

                        if(list != null){
                            this.setCurrentUserJournalList(list);
                            this.updateFragmentMainUserJournalList(list);
                            this.getLogic().addPreferenceTagSet(this, this.getAllJournalTags(list)); // get all journals tags and save to tag file
                        }else{
                            this.getLogging().debug(TAG, "onReceiveResult list is null");
                            this.showFragmentEmptyJournalLayout();
                        }

                        break;

                }

                break;


            case ProjectService.STATUS_FINISHED:
                this.getLogging().debug(TAG, "onReceiveResult -> ProjectService.STATUS_FINISHED");
                projectServiceType = (EnumProjectServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
                switch (projectServiceType) {
                    case GET_ALL_PROJECT:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult GET_ALL_PROJECT -> jsonResult => " + jsonResult);
                        ProjectList list = this.getProjectListFromJson(jsonResult);

                        if(list != null) {
                            this.setProjectList(list);
                        }
                        break;
                }
                break;


            case JournalService.STATUS_ERROR:
                this.getLogging().debug(TAG, "JournalService.STATUS_ERROR");
                break;

            case ProjectService.STATUS_ERROR:
                this.getLogging().debug(TAG, "ProjectService.STATUS_ERROR");
                break;

        }

    }
*/

/*
    public JournalList getJournalListFromJson(String json){
        return getParser().parseJsonToJournalList(json);
    }

    public ProjectList getProjectListFromJson(String json) {
        return getParser().parseJsonToProjectList(json);
    }
*/

    private FacebookUserObject getFacebookUserObject(String json) {
        return ParserFacebook.getFacebookUserObject(json);
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
