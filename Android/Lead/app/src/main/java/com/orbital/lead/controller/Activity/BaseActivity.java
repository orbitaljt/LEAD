package com.orbital.lead.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.controller.Fragment.NavigationDrawerFragment;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.ProjectReceiver;
import com.orbital.lead.controller.Service.ProjectService;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.FacebookLogic;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CountryList;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumAndroidVersion;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.EnumOpenPictureActivityType;
import com.orbital.lead.model.EnumProjectServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.TagSet;
import com.orbital.lead.model.User;

import java.io.File;


public class BaseActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        JournalReceiver.Receiver,
        ProjectReceiver.Receiver{

    public static final int START_SPECIFIC_JOURNAL_ACTIVITY = 0;
    public static final int START_ADD_NEW_SPECIFIC_JOURNAL_ACTIVITY = 1;
    public static final int START_EDIT_SPECIFIC_JOURNAL_ACTIVITY = 2;
    public static final int START_PICTURE_ACTIVITY = 3;


    protected final int REQUEST_PICK_IMAGE_INTENT = 1;
    protected final int REQUEST_IMAGE_CAPTURE = 1337;

    private final String TAG = this.getClass().getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ActionBarDrawerToggle mNavigationDrawerToggle;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CustomApplication mApp;
    private CharSequence mTitle;
    protected FrameLayout mBaseFrameLayout;
    private CustomLogging mLogging;
    private Parser mParser;
    private Logic mLogic;
    private FacebookLogic mFacebookLogic;

    private ProjectList projectList;
    private TagSet tagSet;

    private ProjectReceiver mProjectReceiver;
    private JournalReceiver mJournalReceiver;

    //private boolean isFacebookLogin;
   // private String currentFacebookAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_layout);
        this.initApplication();
        this.initLogging(); // initialize logging first
        this.initParser();
        this.initLogic();
        this.initFacebookLogic();
        this.initBaseFrameLayout();
        this.initTagSet();

        mTitle = getTitle();
        if(this.mNavigationDrawerFragment == null){
            this.mLogging.debug(TAG, "Create NavigationDrawerFragment");

            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));

            this.initNavigationDrawerToggle();

        }

        this.mLogging.debug(TAG, "onCreate");
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch(position){
            case 0: // Profile
                getLogic().displayProfileActivity(this);
                break;
            case 1: // Albums
                //getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_ALBUM, null, Constant.STRING_EMPTY);
                break;
            case 2: // Badge
                break;
            case 3: // Milestone
                break;
            case 4: // Journal
                break;
            case 5: // Setting
                break;

        }
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

    public CustomApplication getCustomApplication(){
        return this.mApp;
    }

    protected void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    protected void initParser(){
        this.mParser = Parser.getInstance();
    }

    protected void initLogic(){
        this.mLogic = Logic.getInstance();
    }

    protected void initTagSet() {
        this.tagSet = new TagSet();
    }

    protected void initProjectReceiver() {
        this.mProjectReceiver = new ProjectReceiver(new Handler());
        this.mProjectReceiver.setReceiver(this);
    }

    protected void initJournalReceiver(){
        mJournalReceiver = new JournalReceiver(new Handler());
        mJournalReceiver.setReceiver(this);
    }


    protected void initBaseFrameLayout(){
        this.mBaseFrameLayout = (FrameLayout) findViewById(R.id.base_content_frame);
    }

    protected void restoreActionBar() {
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    protected void restoreDrawerHeaderValues() {
        if(CurrentLoginUser.getUser() != null){
            this.getNavigationDrawerFragment().setImageUserProfile(this.getCurrentUser().getProfilePicUrl());
            this.getNavigationDrawerFragment().setTextUserName(this.getCurrentUser().getFirstName(),
                                                                this.getCurrentUser().getMiddleName(),
                                                                this.getCurrentUser().getLastName());
            this.getNavigationDrawerFragment().setTextUserEmail(this.getCurrentUser().getEmail());
        }else{
            this.getNavigationDrawerFragment().setImageUserProfile(Constant.STRING_EMPTY);
        }

    }


    protected void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected FrameLayout getBaseFrameLayout() {
        return this.mBaseFrameLayout;
    }

    protected CustomLogging getLogging() {
        return this.mLogging;
    }

    protected Logic getLogic() {
        return this.mLogic;
    }

    protected Parser getParser() {
        return this.mParser;
    }

    protected NavigationDrawerFragment getNavigationDrawerFragment() {
        return this.mNavigationDrawerFragment;
    }

    protected void initNavigationDrawerToggle() {
        if(mNavigationDrawerFragment != null){
            this.mNavigationDrawerToggle = this.mNavigationDrawerFragment.getDrawerToggle();
        }
    }

    protected ActionBarDrawerToggle getNavigationDrawerToggle() {
        return this.mNavigationDrawerToggle;
    }

    protected FacebookLogic getFacebookLogic() {
        return this.mFacebookLogic;
    }


    protected void openImagesIntent() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_PICK);
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        this.getLogging().debug(TAG, "openImagesIntent startActivityForResult");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), this.REQUEST_PICK_IMAGE_INTENT);
    }

    protected void openCameraIntent() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            this.getLogging().debug(TAG, "openCameraIntent startActivityForResult");
            startActivityForResult(intent, this.REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void openCameraIntent(File outputFile) {
        if(outputFile == null) {
            this.getLogging().debug(TAG, "outputFile is null. Not running camera intent");
            return;
        }


        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            this.getLogging().debug(TAG, "openCameraIntent startActivityForResult");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
            startActivityForResult(intent, this.REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void openFacebookAlbum() {
        if(this instanceof PictureActivity) {
            ((PictureActivity) this).selectOpenFragment(EnumOpenPictureActivityType.SELECT_FACEBOOK_ALBUM);
        }
    }

    protected void retrievePreferenceTagSet() {
        this.setTagSet(getLogic().retrievePreferenceTagSet(this));
    }

    public void retrieveAllProject() {
        this.getLogic().getAllProject(this);
    }

    public void retrieveAllCountries() {
        this.getLogic().getAllCountries(this);
    }

    protected String convertToDisplayDate(String oldDate) {
        if(getParser().isStringEmpty(oldDate)) {
            return Constant.STRING_EMPTY;
        }
        return FormatDate.parseDate(oldDate, FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
    }

    protected String convertToDatabaseDate(String displayDate) {
        if(getParser().isStringEmpty(displayDate)) {
            return Constant.STRING_EMPTY;
        }
        return FormatDate.parseDate(displayDate, FormatDate.DISPLAY_DATE_TO_DATABASE_DATE, FormatDate.DATABASE_FORMAT);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumJournalServiceType journalServiceType = null;
        EnumProjectServiceType projectServiceType = null;
        String jsonResult = Constant.STRING_EMPTY;

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
                            if(this instanceof MainActivity) {
                                ((MainActivity)this).updateFragmentMainUserJournalList(list);
                            }

                            this.setCurrentUserJournalList(list);
                            this.getLogic().addPreferenceTagSet(this, this.getAllJournalTags(list)); // get all journals tags and save to tag file
                        }else{
                            this.getLogging().debug(TAG, "onReceiveResult list is null");
                            if(this instanceof MainActivity) {
                                ((MainActivity)this).showFragmentEmptyJournalLayout();
                            }
                        }

                        break;

                    case INSERT_NEW_JOURNAL:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult INSERT_NEW_JOURNAL -> jsonResult => " + jsonResult);
                        Journal journal = this.getSpecificJournalFromJson(jsonResult);

                        if(journal != null) {
                            this.getLogging().debug(TAG, "onReceiveResult INSERT_NEW_JOURNAL journal is not null");
                            this.getCurrentUser().getJournalList().addJournal(journal);

                            if(this instanceof AddNewSpecificJournalActivity) {
                                // close this add activity
                                // refresh the journal list
                                ((AddNewSpecificJournalActivity) this).setToggleRefresh(true);
                                ((AddNewSpecificJournalActivity) this).setIsSaved(true);
                                ((AddNewSpecificJournalActivity) this).onBackPressed();
                            }
                        }else{
                            if(this instanceof AddNewSpecificJournalActivity) {
                                this.getLogging().debug(TAG, "onReceiveResult INSERT_NEW_JOURNAL journal is null");
                            }
                        }


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

                        this.setProjectList(list);
                        this.setCurrentUserProjectList(list);

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


    /**
     *  Get results from activity that has backed pressed
     * **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case START_EDIT_SPECIFIC_JOURNAL_ACTIVITY :
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();
                    if (b != null) {
                        String journalID = b.getString(Constant.BUNDLE_PARAM_JOURNAL_ID);
                        boolean refresh = b.getBoolean(Constant.BUNDLE_PARAM_JOURNAL_TOGGLE_REFRESH);

                        getLogging().debug(TAG, "onActivityResult journalID => " + journalID);
                        getLogging().debug(TAG, "onActivityResult refresh => " + refresh);

                        if(refresh) {
                            ((SpecificJournalActivity) this).refreshJournal(journalID);
                        }
                    }
                }
                break;

            case START_PICTURE_ACTIVITY:
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();
                    if (b != null) {

                        getLogging().debug(TAG, "onActivityResult START_PICTURE_ACTIVITY ");

                        Album updatedAlbum = (Album) b.getParcelable(Constant.BUNDLE_PARAM_ALBUM);
                        String journalID = b.getString(Constant.BUNDLE_PARAM_JOURNAL_ID);

                        updateCurrentUserAlbum(journalID, updatedAlbum);

                        if(this instanceof SpecificJournalActivity) {
                            ((SpecificJournalActivity) this).refreshJournal(journalID);
                        }

                    }
                }
                break;




        }
    }



    protected void setCurrentUserJournalList(JournalList list) {
        this.getCurrentUser().setJournalList(list);
    }

    protected void setCurrentUserProjectList(ProjectList list) {
        this.getCurrentUser().setProjectList(list);
    }

    protected void setProjectList(ProjectList list) {
        this.projectList = new ProjectList();
        this.projectList.setList(list);
    }

    protected EnumAndroidVersion getAndroidVersion() {
        return getCustomApplication().getAndroidVersion();
    }

    public ProjectReceiver getProjectReceiver() {
        if(this.mProjectReceiver == null){
            this.initProjectReceiver();
        }
        return this.mProjectReceiver;
    }

    public JournalReceiver getJournalReceiver() {
        if(this.mJournalReceiver == null){
            this.initJournalReceiver();
        }
        return this.mJournalReceiver;
    }

    protected JournalList getJournalListFromJson(String json) {
        return this.getParser().parseJsonToJournalList(json);
    }

    protected ProjectList getProjectListFromJson(String json) {
        return this.getParser().parseJsonToProjectList(json);
    }

    protected Journal getSpecificJournalFromJson(String json) {
        return this.getParser().parseJsonToSpecificJournal(json);
    }

    protected User getCurrentUser() {
        return CurrentLoginUser.getUser();
    }

    protected ProjectList getProjectList() {
        return this.projectList;
    }

    protected TagSet getTagSet() {
        return this.tagSet;
    }

    protected TagSet getAllJournalTags(JournalList list) {
        TagSet set = list.getAllTags();
        return set;
    }

    public CountryList getCountryList() {
        return this.getCurrentUser().getCountryList();
    }

    protected void updateCurrentUserAlbum(String journalID, Album album) {
        if(getParser().isStringEmpty(journalID)) {
            getLogging().debug(TAG, "updateCurrentUserAlbum journalID is empty!");
            return;
        }

        try{
            getLogging().debug(TAG, "updateCurrentUserAlbum trying...");
            this.getCurrentUser().getJournalList().get(journalID).setAlbum(album);
            this.getCurrentUser().getJournalList().get(journalID).setPictureCoverID(album.getPictureCoverID());
            this.getCurrentUser().getJournalList().get(journalID).setPictureCoverType(album.getPictureCoverType());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    protected void setIsFacebookLogin(boolean val) {
        mLogging.debug(TAG, "setIsFacebookLogin to => " + val);
        this.isFacebookLogin = val;
    }


    protected boolean getIsFacebookLogin() {
        return this.isFacebookLogin;
    }

    protected String getCurrentFacebookAccessTokenString() {
        try {
            return AccessToken.getCurrentAccessToken().getToken();
        }catch(NullPointerException e){
            return "";
        }
    }

    protected AccessToken getCurrentFacebookAccessToken() {
        try {
            return AccessToken.getCurrentAccessToken();
        }catch(NullPointerException e){
            return null;
        }
    }


    public AlbumList getFacebookAllAlbum(String param) {
        mLogging.debug(TAG, "getFacebookAllAlbum");

        boolean finishLoop = false;

        GraphRequest request = GraphRequest.newMeRequest(
                this.getCurrentFacebookAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        mLogging.debug(TAG, "response getRawResponse -> " + response.getRawResponse());

                        //set that user is login using facebook
                        String data = response.getRawResponse();
                        if(!mParser.isStringEmpty(data)){

                            AlbumList facebookAlbumList = ParserFacebook.getFacebookAlbumList(data);

                            if(BaseActivity.this instanceof PictureActivity) {
                                if(facebookAlbumList != null){
                                    ((PictureActivity) BaseActivity.this).updateFragmentAlbumGridAdapter(facebookAlbumList);
                                }else{
                                    mLogging.debug(TAG, "facebookAlbumList is null");
                                }

                            }



                        }else{
                            mLogging.debug(TAG, "unable to get respose data. get error -> " + response.getError().toString());
                        }

                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", param);
        request.setParameters(parameters);
        request.executeAsync();

        return null;

    }
    */


    private void initApplication(){
        this.mApp = (CustomApplication) getApplicationContext();
    }

    private void initFacebookLogic() {
        this.mFacebookLogic = FacebookLogic.getInstance();
    }

    private void setTagSet(TagSet set) {
        if(set != null) {
            this.tagSet = new TagSet(set.getSet());
        }
    }

}
