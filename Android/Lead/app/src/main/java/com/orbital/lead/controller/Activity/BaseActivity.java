package com.orbital.lead.controller.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.NavigationDrawerFragment;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;


public class BaseActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final String TAG = this.getClass().getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ActionBarDrawerToggle mNavigationDrawerToggle;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    protected FrameLayout mBaseFrameLayout;
    private CustomLogging mLogging;
    private Parser mParser;
    private Logic mLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        this.initLogging(); // initialize logging first
        this.initParser();
        this.initLogic();
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



}
