package com.orbital.lead.controller;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.orbital.lead.R;
import com.orbital.lead.model.Constant;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

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

        //Other initialization
        //this.initFacebook();


        // fragment manager
        this.initFragmentManager();

        // Display login first
        //this.displayFragmentLogin();
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

    public void restoreActionBar() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
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




/*
    public void initFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
*/

    public void initFragmentManager(){

        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    //mNavigationDrawerToggle.setDrawerIndicatorEnabled(false);
                } else {
                    //mNavigationDrawerToggle.setDrawerIndicatorEnabled(true);
                    //mNavigationDrawerToggle.setToolbarNavigationClickListener(mNavigationDrawerFragment.getOriginalToolbarNavigationClickListener());
                }
            }
        });
    }

    /*
    public void displayFragmentLogin(){
        String fragName = Constant.FRAGMENT_LOGIN_NAME;
        this.mFragmentLogin = FragmentLogin.newInstance("", "");
        this.replaceFragment(this.mFragmentLogin, fragName);
    }
    */

    public void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .replace(R.id.container, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }

    public void addFragment(Fragment newFrag, String name){
        if(newFrag != null){
            if(fragmentManager == null){
                this.initFragmentManager();
            }

            this.fragmentTransaction = fragmentManager.beginTransaction();

            // remove the fragment from manager first if found
            Fragment searchFrag = fragmentManager.findFragmentByTag(name);
            if(searchFrag != null){
                this.removeFragment(searchFrag);
            }

            // add new fragment
            this.fragmentTransaction
                    .add(R.id.container, newFrag, name)
                    .addToBackStack(name)
                    .commit();

        }
    }

    public void removeFragment(Fragment frag){
        if(frag != null){
            this.fragmentTransaction
                    .remove(frag);
        }
    }


    public void removeFragmentFromBackStack(){
        this.fragmentManager.popBackStack();
        this.fragmentManager.executePendingTransactions();
    }

    public boolean hasReachedFirstPage(){
        if(this.fragmentManager.getBackStackEntryCount() == 0){
            return true;
        }else{
            return false;
        }
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    */

}
