package com.orbital.lead.controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.controller.Fragment.NavigationDrawerFragment;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.FacebookLogic;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.logic.Preference.Preference;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.CurrentLoginUser;

import org.json.JSONObject;


public class BaseActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

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

    private boolean isFacebookLogin;
    private String currentFacebookAccessToken;

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
                getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_ALBUM, null);
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

    protected void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    protected void initParser(){
        this.mParser = Parser.getInstance();
    }

    protected void initLogic(){
        this.mLogic = Logic.getInstance();
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
            this.getNavigationDrawerFragment().setImageUserProfile(CurrentLoginUser.getUser().getProfilePicUrl());
            this.getNavigationDrawerFragment().setTextUserName(CurrentLoginUser.getUser().getFirstName(),
                                                                CurrentLoginUser.getUser().getMiddleName(),
                                                                CurrentLoginUser.getUser().getLastName());
            this.getNavigationDrawerFragment().setTextUserEmail(CurrentLoginUser.getUser().getEmail());
        }else{
            this.getNavigationDrawerFragment().setImageUserProfile("");
        }

    }


    protected void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    protected FrameLayout getBaseFrameLayout(){
        return this.mBaseFrameLayout;
    }

    protected CustomLogging getCustomLogging(){
        return this.mLogging;
    }

    protected Logic getLogic(){
        return this.mLogic;
    }

    protected Parser getParser(){
        return this.mParser;
    }

    protected NavigationDrawerFragment getNavigationDrawerFragment(){
        return this.mNavigationDrawerFragment;
    }

    protected void initNavigationDrawerToggle(){
        if(mNavigationDrawerFragment != null){
            this.mNavigationDrawerToggle = this.mNavigationDrawerFragment.getDrawerToggle();
        }
    }

    protected ActionBarDrawerToggle getNavigationDrawerToggle(){
        return this.mNavigationDrawerToggle;
    }

    protected FacebookLogic getFacebookLogic() {
        return this.mFacebookLogic;
    }


    protected void openImagesIntent() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        this.getCustomLogging().debug(TAG, "openImagesIntent startActivityForResult");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), this.REQUEST_PICK_IMAGE_INTENT);
    }

    protected void openCameraIntent() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            this.getCustomLogging().debug(TAG, "openCameraIntent startActivityForResult");
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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

    public CustomApplication getCustomApplication(){
        return this.mApp;
    }

    private void initApplication(){
        this.mApp = (CustomApplication) getApplicationContext();
    }

    private void initFacebookLogic() {
        this.mFacebookLogic = FacebookLogic.getInstance();
    }


}
