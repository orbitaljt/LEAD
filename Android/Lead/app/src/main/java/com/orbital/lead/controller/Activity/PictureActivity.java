package com.orbital.lead.controller.Activity;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentAlbum;
import com.orbital.lead.controller.Fragment.FragmentPictures;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.PictureReceiver;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumPictureServiceType;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureActivity extends BaseActivity implements
       FragmentPictures.OnFragmentInteractionListener,
                FragmentAlbum.OnFragmentInteractionListener,
        PictureReceiver.Receiver{
    private final String TAG = this.getClass().getSimpleName();

    public static final String OPEN_FRAGMENT_LIST_PICTURES = "0"; // fragment that shows a list of all albums
    public static final String OPEN_FRAGMENT_ALBUM = "1"; // fragment that shows all pictures of an album

    private String openType;
    private AlbumList mAlbumList;
    private Album mAlbum;
    //private ArrayList<Picture> mPictureList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragmentAlbum mFragmentAlbum;
    private FragmentPictures mFragmentPictures;
    private View mToolbarView;
    private TextView mToolbarTitle;

    private PictureReceiver mPictureReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_picture, getBaseFrameLayout());

        this.getCustomLogging().debug(TAG, "onCreate");
        this.initPictureReceiver();
        this.initFragmentManager();
        this.initToolbar();
        this.initToolbarTitle();
        this.pushToolbarToActionbar();
        this.restoreCustomActionbar();

        Bundle getBundleExtra = getIntent().getExtras();

        if (getBundleExtra != null) {
            if(getBundleExtra.getString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE) != null){
                this.setOpenFragmentType(getBundleExtra.getString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE));
            }

            if(getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM)!= null) {
                this.setAlbum((Album) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM));
                //this.setPictureList(this.getAlbum().getPictureList().getList());
            }

            /*
            if(getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST) != null) {
                this.setPictureList((ArrayList) getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST));
            }
            */

            switch(this.getOpenFragmentType()){
                case PictureActivity.OPEN_FRAGMENT_ALBUM:
                    this.getCustomLogging().debug(TAG, "PictureActivity.OPEN_FRAGMENT_ALBUM");
                    this.getLogic().getUserAllAlbum(this, CurrentLoginUser.getUser().getUserID());
                    this.setToolbarTitle(Constant.TITLE_FRAGMENT_ALBUM);
                    this.displayFragmentAlbum();

                    break;

                case PictureActivity.OPEN_FRAGMENT_LIST_PICTURES:
                    this.getCustomLogging().debug(TAG, "PictureActivity.OPEN_FRAGMENT_LIST_PICTURES");
                    this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                    this.displayFragmentPictures();
                    break;

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_picture, menu);
            this.restoreCustomActionbar();
        }
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            getCustomLogging().debug(TAG, "onOptionsItemSelected");
            onBackPressed();
            return true;

        }else if (id == R.id.action_settings) {
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.setToolbarTitleBaseOnCurrentFragment();
    }

    @Override
    public void setToolbarTitle(String title){
        this.mToolbarTitle.setText(title);
    }


    public PictureReceiver getPictureReceiver(){
        if(this.mPictureReceiver == null){
            this.initPictureReceiver();
        }
        return this.mPictureReceiver;
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

    private void initPictureReceiver(){
        mPictureReceiver = new PictureReceiver(new Handler());
        mPictureReceiver.setReceiver(this);
    }

    private void initToolbar() {
        this.mToolbarView = findViewById(R.id.toolbar_specific_journal_pictures);
    }

    private void initToolbarTitle() {
        this.mToolbarTitle = (TextView) findViewById(R.id.toolbar_text_title);
    }

    private void initFragmentManager(){
        this.fragmentManager = getSupportFragmentManager();
        /*
        this.fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    getNavigationDrawerToggle().setDrawerIndicatorEnabled(false);
                } else {
                    getNavigationDrawerToggle().setDrawerIndicatorEnabled(true);
                    getNavigationDrawerToggle().setToolbarNavigationClickListener(getNavigationDrawerFragment().getOriginalToolbarNavigationClickListener());
                }
            }
        });
        */
    }

    private void displayFragmentPictures(){
        this.getCustomLogging().debug(TAG, "displayFragmentPictures");
        this.mFragmentPictures = FragmentPictures.newInstance(this.getAlbum());
        if(!isAnyFragmentExist()){ // no fragment exist
            this.getCustomLogging().debug(TAG, "Replace Fragment");
            this.replaceFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES);
        }else{
            this.getCustomLogging().debug(TAG, "Add Fragment");
            this.addFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES);
        }

    }

    private void displayFragmentAlbum(){
        this.getCustomLogging().debug(TAG, "displayFragmentAlbum");
        this.mFragmentAlbum = FragmentAlbum.newInstance("", "");
        this.replaceFragment(this.mFragmentAlbum, Constant.FRAGMENT_ALBUM);
    }


    private void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = this.fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .replace(R.id.container_fragment, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }

    private void addFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = this.fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .add(R.id.container_fragment, newFrag, name)
                    .addToBackStack(name)
                    .commit();
        }
    }

    private boolean isAnyFragmentExist(){
        Fragment frag = this.fragmentManager.findFragmentById(R.id.container_fragment);
        if(frag == null){
            return false;
        }
        return true;
    }

    private void setToolbarTitleBaseOnCurrentFragment(){
        Fragment frag = this.fragmentManager.findFragmentById(R.id.container_fragment);
        if(frag != null){
            if(frag instanceof FragmentPictures) {
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
            }else if(frag instanceof FragmentAlbum) {
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_ALBUM);
            }
        }
    }

    private void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }

    private void updateFragmentAlbum(AlbumList list) {
        if(this.mFragmentAlbum != null) {
            this.getCustomLogging().debug(TAG, "onReceiveResult -> mFragmentAlbum.updateGridAlbum");
            this.mFragmentAlbum.updateGridAlbum(list);
        }else{
            this.getCustomLogging().debug(TAG, "onReceiveResult -> mFragmentAlbum is null");
        }
    }

    private String getOpenFragmentType() {
        return this.openType;
    }

    private FragmentAlbum getFragmentAlbum() {
        return this.mFragmentAlbum;
    }

    private AlbumList getAlbumList() {
        return this.mAlbumList;
    }

    private Album getAlbum() {
        return this.mAlbum;
    }

    /*
    private ArrayList<Picture> getPictureList() {
        return this.mPictureList;
    }
    */
    public View getToolbar() {
        return this.mToolbarView;
    }

    private void setOpenFragmentType(String type) {
        this.openType = type;
    }

    private void setAlbumList(AlbumList list) {
        this.mAlbumList = list;
    }

    private void setAlbum(Album a) {
        this.mAlbum = a;
    }

    /*
    private void setPictureList(ArrayList<Picture> pList) {
        this.mPictureList = pList;
    }
    */

    @Override
    public void onFragmentPicturesInteraction(Uri uri) {

    }

    @Override
    public void onFragmentAlbumInteraction(int request, Album selectedAlbum) {
        switch(request){
            case FragmentAlbum.REQUEST_OPEN_FRAGMENT_PICTURES:
                this.getCustomLogging().debug(TAG, "onFragmentAlbumInteraction REQUEST_OPEN_FRAGMENT_PICTURES");
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.setAlbum(selectedAlbum);
                this.displayFragmentPictures();
                break;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumPictureServiceType type = null;
        String jsonResult = "";

        switch (resultCode) {
            case PictureService.STATUS_RUNNING:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_RUNNING");
                break;

            case PictureService.STATUS_FINISHED:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_FINISHED");
                type = (EnumPictureServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_ALL_ALBUM:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getCustomLogging().debug(TAG, "onReceiveResult GET_ALL_ALBUM -> jsonResult => " + jsonResult);

                        AlbumList list = getParser().parseJsonToAlbumList(jsonResult);
                        this.setAlbumList(list);
                        this.getCustomLogging().debug(TAG, "onReceiveResult this.getAlbumList().size() => " + this.getAlbumList().size());

                        if(this.getAlbumList() !=  null){
                            this.updateFragmentAlbum(this.getAlbumList());
                        }

                        break;
                }

                break;

            case JournalService.STATUS_ERROR:
                this.getCustomLogging().debug(TAG, "PictureService.STATUS_ERROR");
                break;
        }

    }
}
