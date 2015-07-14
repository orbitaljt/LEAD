package com.orbital.lead.controller.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentProfile;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;

public class ProfileActivity extends BaseActivity
        implements FragmentProfile.OnFragmentInteractionListener {

    private final String TAG = this.getClass().getSimpleName();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentProfile mFragmentProfile;

    private View mToolbarView;
    private TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, getBaseFrameLayout());

        this.initFragmentManager();
        this.initToolbar();
        this.pushToolbarToActionbar();
        this.initToolbarTitle();
        this.setToolbarTitle(Constant.TITLE_FRAGMENT_PROFILE);
        this.restoreCustomActionbar();
        this.displayFragmentProfile();
        this.restoreDrawerHeaderValues(); // restore drawer values
    }

    @Override
    public void setToolbarTitle(String title){
        this.mToolbarTitle.setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            this.restoreCustomActionbar();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;

        }else if (id == R.id.action_done) {
            this.saveUserProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onFragmentProfileInteraction(int requestType) {
        switch (requestType) {
            case Constant.DIALOG_REQUEST_OPEN_INTENT_CAMERA:
                this.openCameraIntent();
                break;
            case Constant.DIALOG_REQUEST_OPEN_INTENT_IMAGES:
                this.openImagesIntent();
                break;

            case Constant.DIALOG_REQUEST_OPEN_FACEBOOK_ALBUM:
                // open facebook album
                break;
        }
    }

    private void initToolbar() {
        this.mToolbarView = findViewById(R.id.custom_toolbar);
    }

    private void initToolbarTitle() {
        this.mToolbarTitle = (TextView) findViewById(R.id.toolbar_text_title);
    }

    private void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }

    private void restoreCustomActionbar(){
        // disable the home button and onClick to open navigation drawer
        // enable the back arrow button
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    private View getToolbar() {
        return this.mToolbarView;
    }

    private void initFragmentManager(){
        this.fragmentManager = getSupportFragmentManager();
    }

    private void displayFragmentProfile(){
        this.getCustomLogging().debug(TAG, "displayFragmentProfile");
        this.mFragmentProfile = FragmentProfile.newInstance("","");
        this.replaceFragment(this.mFragmentProfile, Constant.FRAGMENT_PROFILE);
    }

    private void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = this.fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .replace(R.id.container, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }

    private void saveUserProfile() {
        if(mFragmentProfile.hasChanges()){
            getCustomLogging().debug(TAG, "updateUserProfile there's changes made in fragment profile");
            this.updateUserProfile();
        }else {
            getCustomLogging().debug(TAG, "updateUserProfile no changes made in fragment profile");
        }
    }

    private void updateUserProfile(){

            String newFirstName = this.mFragmentProfile.getNewFirstName();
            String newMiddleName = this.mFragmentProfile.getNewMiddleName();
            String newLastName = this.mFragmentProfile.getNewLastName();
            String newBirthday = this.mFragmentProfile.getNewBirthday();
            String newEmail = this.mFragmentProfile.getNewEmail();
            String newContact = this.mFragmentProfile.getNewContact();
            String newAddress = this.mFragmentProfile.getNewAddress();
            String newCountry = this.mFragmentProfile.getNewCountry();

            CurrentLoginUser.getUser().setFirstName(newFirstName);
            CurrentLoginUser.getUser().setMiddleName(newMiddleName);
            CurrentLoginUser.getUser().setLastName(newLastName);
            CurrentLoginUser.getUser().setBirthday(newBirthday);
            CurrentLoginUser.getUser().setEmail(newEmail);
            CurrentLoginUser.getUser().setContact(newContact);
            CurrentLoginUser.getUser().setAddress(newAddress);
            CurrentLoginUser.getUser().setCountry(newCountry);

            String detail = getParser().userObjectToJson(CurrentLoginUser.getUser());
            getCustomLogging().debug(TAG, "updateUserProfile detail => " + detail);
            getLogic().updateUserProfileDatabase(this, CurrentLoginUser.getUser().getUserID(), detail);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case REQUEST_PICK_IMAGE_INTENT:
                if (resultCode == RESULT_OK
                        && data != null && data.getData() != null) {

                    Uri uri = data.getData();
                    this.getCustomLogging().debug(TAG, "onActivityResult REQUEST_PICK_IMAGE_INTENT uri string => " + uri.toString());
                    /*
                    try {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   */
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK
                        && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    this.getCustomLogging().debug(TAG, "onActivityResult REQUEST_IMAGE_CAPTURE uri string => " + uri.toString());


                }

                break;


        }

    }

}
