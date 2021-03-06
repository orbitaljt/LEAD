package com.orbital.lead.controller.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.BaseActivity;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.controller.ListAdapter.DrawerListAdapter;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.DrawerListObject;
import com.squareup.picasso.Picasso;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private final String TAG = this.getClass().getSimpleName();
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;




    private DrawerLayout mDrawerLayout;
    //private ListView mDrawerListView;
    private View mFragmentContainerView;
    private RelativeLayout mDrawerImageHeader;
    private CircularImageView mDrawerUserProfileImage;
    private TextView mTextUserName;
    private TextView mTextUserEmail;
    private DrawerListAdapter mDrawerListAdapter;
    private ListView mDrawerListView;

    private CustomLogging mLogging;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private View.OnClickListener mOriginalListener;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initLogging();
        this.mLogging.debug(TAG, "onCreate");
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        //selectItem(mCurrentSelectedPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mLogging.debug(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        this.initDrawerListAdapter();
        this.initDrawerList(rootView);
        this.initImageHeader(rootView);
        this.initImageUserProfile(rootView);
        this.initTextUserName(rootView);
        this.initTextUserEmail(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mLogging.debug(TAG, "onActivityCreated");
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        this.mLogging.debug(TAG, "onResume");
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getBaseActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        this.mOriginalListener = mDrawerToggle.getToolbarNavigationClickListener();

    }

    public ActionBarDrawerToggle getDrawerToggle(){
        return this.mDrawerToggle;
    }

    public View.OnClickListener getOriginalToolbarNavigationClickListener(){
        return this.mOriginalListener;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onPause(){
        super.onPause();
        this.mLogging.debug(TAG, "onPause");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLogging.debug(TAG, "onSaveInstanceState");
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //if (item.getItemId() == R.id.action_) {
        //    Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public BaseActivity getBaseActivity(){
        if(getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();

        }else{
            return null;
        }
    }

    public void setImageUserProfile(Bitmap bmp){
        if(this.mDrawerUserProfileImage != null){
            this.mDrawerUserProfileImage.setImageBitmap(bmp);
        }
    }

    public void setImageUserProfile(Drawable drawable){
        if(this.mDrawerUserProfileImage != null){
            this.mDrawerUserProfileImage.setImageDrawable(drawable);
        }
    }

    public void setImageUserProfile(String url){
        if(this.mDrawerUserProfileImage != null && url != ""){
            //Picasso.with(getBaseActivity()).load(url).error(R.drawable.ic_default_user).into(this.);
            ImageLoader.getInstance()
                    .displayImage(url, this.mDrawerUserProfileImage, this.getBaseActivity().getCustomApplication().getDisplayImageOptions(),
                            new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    //holder.progressBar.setProgress(0);
                                    //holder.progressBar.setVisibility(View.VISIBLE);
                                    //().debug(TAG, "onLoadingStarted");
                                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.ic_default_user));

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    //getLogging().debug(TAG, "onLoadingFailed");
                                    //mAnimator.setDisplayedChild(2);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    //getLogging().debug(TAG, "onLoadingComplete");
                                   // mAnimator.setDisplayedChild(0);
                                }
                            },
                            new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    //getLogging().debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                    //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });
        }else{
            this.mDrawerUserProfileImage.destroyDrawingCache();
            this.mDrawerUserProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_user));
        }
    }

    public void setTextUserName(String firstName, String middleName, String lastName) {
        String fullName = firstName + " " + middleName + " " + lastName;
        this.mTextUserName.setText(fullName);
    }

    public void setTextUserEmail(String email){
        this.mTextUserEmail.setText(email);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initDrawerListAdapter() {
        if(this.mDrawerListAdapter == null){
            this.mDrawerListAdapter = new DrawerListAdapter(getBaseActivity().getBaseContext(), R.layout.drawer_list_row_layout, null);
        }
    }

    private void initDrawerList(View v) {
        this.mDrawerListView = (ListView) v.findViewById(R.id.drawer_list_view);
        this.mDrawerListView.setAdapter(mDrawerListAdapter);
        this.mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }



    private void initImageHeader(View v){
        this.mDrawerImageHeader = (RelativeLayout) v.findViewById(R.id.drawer_image_header);
        this.mDrawerImageHeader.setBackgroundResource(R.drawable.image_drawer_header);
    }

    private void initImageUserProfile(View v){
        this.mDrawerUserProfileImage = (CircularImageView) v.findViewById(R.id.drawer_user_profile_image);
    }

    private void initTextUserName(View v){
        this.mTextUserName = (TextView) v.findViewById(R.id.text_drawer_user_name);
    }

    private void initTextUserEmail(View v){
        this.mTextUserEmail = (TextView) v.findViewById(R.id.text_drawer_user_email);
    }






    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setName(R.string.app_name);
    }

    /*
    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }
    */

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
