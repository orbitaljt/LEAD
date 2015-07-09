package com.orbital.lead.controller.Activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentAlbum;
import com.orbital.lead.controller.Fragment.FragmentProfile;
import com.orbital.lead.model.Constant;

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
        this.restoreCustomActionbar();
        this.displayFragmentProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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

    @Override
    public void onFragmentProfileInteraction(Uri uri) {

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

}
