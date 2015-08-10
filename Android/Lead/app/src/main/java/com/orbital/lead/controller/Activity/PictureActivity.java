package com.orbital.lead.controller.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.orbital.lead.controller.Service.PictureReceiver;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumFacebookQueryType;
import com.orbital.lead.model.EnumPictureServiceType;
import com.orbital.lead.model.PictureList;

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
    private String journalID;
    //private ArrayList<Picture> mPictureList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragmentAlbum mFragmentAlbum;
    private FragmentPictures mFragmentPictures;
    private View mToolbarView;
    private TextView mToolbarTitle;

    private PictureReceiver mPictureReceiver;
    private Bitmap resultBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_picture, getBaseFrameLayout());

        this.getLogging().debug(TAG, "onCreate");
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

            if(getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_ID) != null){
                this.setJournalID(getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_ID));
            }


            if(getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM)!= null) {
                this.setSelectedAlbum((Album) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM));
            }

            //this.setIsFacebookLogin(getBundleExtra.getBoolean(Constant.BUNDLE_PARAM_IS_FACEBOOK_LOGIN));

            /*
            if(getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST) != null) {
                this.setPictureList((ArrayList) getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST));
            }
            */

            //this.getLogging().debug(TAG, "this.getIsFacebookLogin() => " + this.getIsFacebookLogin());
            //this.getLogging().debug(TAG, "this.getCurrentFacebookAccessTokenString() => " + this.getCurrentFacebookAccessTokenString());

            switch(this.getOpenFragmentType()){
                case PictureActivity.OPEN_FRAGMENT_ALBUM:
                    this.getLogging().debug(TAG, "PictureActivity.OPEN_FRAGMENT_ALBUM");

                    if(this.getFacebookLogic().getIsFacebookLogin()){
                        this.getFacebookLogic().sendGraphRequest(this, EnumFacebookQueryType.GET_ALL_ALBUM, "");
                    }

                    this.getLogic().getUserAllAlbum(this, CurrentLoginUser.getUser().getUserID());
                    this.setToolbarTitle(Constant.TITLE_FRAGMENT_ALBUM);
                    this.displayFragmentAlbum();

                    break;

                case PictureActivity.OPEN_FRAGMENT_LIST_PICTURES:
                    this.getLogging().debug(TAG, "PictureActivity.OPEN_FRAGMENT_LIST_PICTURES");

                    if(this.getFacebookLogic().getIsFacebookLogin() && this.getSelectedAlbum() != null){
                        this.getFacebookLogic().sendGraphRequest(this,
                                                EnumFacebookQueryType.GET_ALL_ALBUM_PICTURES,
                                                this.getSelectedAlbum().getAlbumID());
                    }

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
            getLogging().debug(TAG, "onOptionsItemSelected");
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
        this.getLogging().debug(TAG, "displayFragmentPictures");
        this.mFragmentPictures = FragmentPictures.newInstance(this.getSelectedAlbum());
        if(!isAnyFragmentExist()){ // no fragment exist
            this.getLogging().debug(TAG, "Replace Fragment");
            this.replaceFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES);
        }else{
            this.getLogging().debug(TAG, "Add Fragment");
            this.addFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES);
        }

    }

    private void displayFragmentAlbum(){
        this.getLogging().debug(TAG, "displayFragmentAlbum");
        this.mFragmentAlbum = FragmentAlbum.newInstance(this.getFacebookLogic().getIsFacebookLogin(),
                                                        this.getFacebookLogic().getCurrentFacebookAccessTokenString());
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

    /*
    private void updateFragmentAlbum(AlbumList list) {
        if(this.mFragmentAlbum != null) {
            this.getLogging().debug(TAG, "updateFragmentAlbum -> mFragmentAlbum.updateGridAlbum");
            this.mFragmentAlbum.updateGridAlbum(list);
        }else{
            this.getLogging().debug(TAG, "updateFragmentAlbum -> mFragmentAlbumis null");
        }
    }
    */

    public void updateFragmentAlbumGridAdapter(AlbumList list) {
        if(this.mFragmentAlbum != null) {
            this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentAlbum.updateGridAlbumAdapter");
            this.mFragmentAlbum.updateGridAlbumAdapter(list);
        }else{
            this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentAlbum is null");
        }
    }

    public void updateFragmentPicturesGridAdapter(PictureList list) {
        if(this.mFragmentPictures != null) {
            this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentPictures.updateGridPicturesAdapter");
            this.mFragmentPictures.updateGridPicturesAdapter(list);
        }else{
            this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentPictures is null");
        }
    }

    private void updateCurrentUserAlbum(Album newAlbum) {
        this.getCurrentUser().getJournalList().get(this.getJournalID()).setAlbum(newAlbum);
    }


    private String getOpenFragmentType() {
        return this.openType;
    }

    private String getJournalID() {
        return this.journalID;
    }

    private FragmentAlbum getFragmentAlbum() {
        return this.mFragmentAlbum;
    }

    private FragmentPictures getFragmentPictures() {
        return this.mFragmentPictures;
    }

    private AlbumList getAlbumList() {
        return this.mAlbumList;
    }

    private Album getSelectedAlbum() {
        return this.mAlbum;
    }

    /*
    private ArrayList<Picture> getPictureList() {
        return this.mPictureList;
    }
    */
    private View getToolbar() {
        return this.mToolbarView;
    }

    private void setOpenFragmentType(String type) {
        this.openType = type;
    }

    private void setJournalID(String id) {
        this.journalID = id;
    }

    private void setAlbumList(AlbumList list) {
        this.mAlbumList = list;
    }

    private void setSelectedAlbum(Album a) {
        this.mAlbum = a;
    }

    /*
    private void setPictureList(ArrayList<Picture> pList) {
        this.mPictureList = pList;
    }
    */

    @Override
    public void onFragmentPicturesInteraction(int requestType) {
        switch (requestType) {
            case Constant.DIALOG_REQUEST_OPEN_INTENT_IMAGES:
                // open up all images intent choices
                this.openImagesIntent();
                break;

            case Constant.DIALOG_REQUEST_OPEN_INTENT_CAMERA:
                this.openCameraIntent();
                break;

            case Constant.DIALOG_REQUEST_OPEN_FACEBOOK_ALBUM:
                // open fragment album that contain all facebook albums
                break;

        }
    }

    @Override
    public void onFragmentAlbumInteraction(int request, Album selectedAlbum) {
        this.setSelectedAlbum(selectedAlbum);
        switch(request){
            case FragmentAlbum.REQUEST_OPEN_FRAGMENT_PICTURES:
                this.getLogging().debug(TAG, "onFragmentAlbumInteraction REQUEST_OPEN_FRAGMENT_PICTURES");
                if(this.getFacebookLogic().getIsFacebookLogin()){
                    this.getFacebookLogic().sendGraphRequest(this,
                            EnumFacebookQueryType.GET_ALL_ALBUM_PICTURES,
                            this.getSelectedAlbum().getAlbumID());
                }
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.setSelectedAlbum(selectedAlbum);
                this.displayFragmentPictures();
                break;
        }
    }


    /**
     * onActivityResult is used to receive the result from Intent (images)
     * **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.REQUEST_PICK_IMAGE_INTENT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();
            this.getLogging().debug(TAG, "onActivityResult uri string => " + getPath(uri));

            this.getLogic().uploadNewPicture(this, getPath(uri),
                                            CurrentLoginUser.getUser().getUserID(),
                                            this.getSelectedAlbum().getAlbumID());

  /*
            try {

                if (this.resultBitmap != null) {
                    this.resultBitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                this.resultBitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                this.getLogic().uploadNewPicture(this, this.resultBitmap,
                                                CurrentLoginUser.getUser().getUserID(),
                                                 this.getSelectedAlbum().getAlbumID());


            } catch (FileNotFoundException e) {
                this.getLogging().debug(TAG, "onActivityResult error => " + e.getMessage());
                e.printStackTrace();

            } catch (IOException e) {
                this.getLogging().debug(TAG, "onActivityResult error => " + e.getMessage());
                e.printStackTrace();

            }
  */



            /*
            try {

            } catch (IOException e) {
                e.printStackTrace();
            }
           */
        }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    /**
     * onReceiveResult is used to receive the picture service result
     * **/
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumPictureServiceType type = null;
        String jsonResult = "";
        int progressValue = -1;

        if(resultData == null) {
            this.getLogging().debug(TAG, "onReceiveResult resultData is null...");
            return;
        }

        switch (resultCode) {
            case PictureService.STATUS_RUNNING:
                this.getLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_RUNNING");

                type = (EnumPictureServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
                switch(type){
                    case UPLOAD_IMAGE_FILE:
                        try{
                            progressValue = resultData.getInt(Constant.INTENT_SERVICE_RESULT_UPLOAD_FILE_PROGRESS_VALUE_TAG);
                            this.getLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_RUNNING UPLOAD_IMAGE_FILE progress value => " + progressValue);
                        }catch (Exception e) {
                            e.printStackTrace();
                            progressValue = -1;
                        }

                        if(progressValue != -1) {
                            if(this.getFragmentPictures() != null) {
                                this.getFragmentPictures().updateUploadImageProgressValue(progressValue);
                            }
                        }

                }


                break;

            case PictureService.STATUS_FINISHED:
                this.getLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_FINISHED");
                type = (EnumPictureServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_ALL_ALBUM:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        //this.getLogging().debug(TAG, "onReceiveResult GET_ALL_ALBUM -> jsonResult => " + jsonResult);

                        AlbumList list = getParser().parseJsonToAlbumList(jsonResult);
                        this.setAlbumList(list);

                        if(this.getAlbumList() !=  null){
                            this.updateFragmentAlbumGridAdapter(this.getAlbumList());
                        }else{
                            this.getLogging().debug(TAG, "onReceiveResult this.getAlbumList() is null!");
                        }

                        break;

                    case UPLOAD_IMAGE_FILE:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult UPLOAD_IMAGE_FILE -> jsonResult => " + jsonResult);

                        Album updatedAlbum = getParser().parseJsonToSpecificAlbum(jsonResult);

                        this.updateCurrentUserAlbum(updatedAlbum);
                        this.updateFragmentPicturesGridAdapter(updatedAlbum.getPictureList());
                        if(this.getFragmentPictures() != null) {
                            this.getFragmentPictures().closeUploadImageProgressDialog();
                        }

                        break;
                } //end switch

                break;

            case PictureService.STATUS_ERROR:
                this.getLogging().debug(TAG, "PictureService.STATUS_ERROR");
                break;
        }

    }


}
