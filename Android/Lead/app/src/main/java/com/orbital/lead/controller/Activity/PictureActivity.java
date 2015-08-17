package com.orbital.lead.controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
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
import com.orbital.lead.controller.Fragment.FragmentFacebookSelectionAlbum;
import com.orbital.lead.controller.Fragment.FragmentFacebookSelectionPictures;
import com.orbital.lead.controller.Fragment.FragmentPictures;
import com.orbital.lead.controller.Service.PictureReceiver;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumFacebookQueryType;
import com.orbital.lead.model.EnumOpenPictureActivityType;
import com.orbital.lead.model.EnumPictureServiceType;
import com.orbital.lead.model.PictureList;

import java.io.File;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureActivity extends BaseActivity implements
        FragmentPictures.OnFragmentInteractionListener,
        FragmentAlbum.OnFragmentInteractionListener,
        FragmentFacebookSelectionAlbum.OnFragmentInteractionListener,
        FragmentFacebookSelectionPictures.OnFragmentInteractionListener,
        PictureReceiver.Receiver{
    private final String TAG = this.getClass().getSimpleName();

    private EnumOpenPictureActivityType openType;
    private AlbumList mAlbumList;
    private Album mAlbum;
    private String journalID;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragmentAlbum mFragmentAlbum;
    private FragmentFacebookSelectionAlbum mFragmentFacebookSelectionAlbum;
    private FragmentPictures mFragmentPictures;
    private FragmentFacebookSelectionPictures mFragmentFacebookSelectionPictures;
    private View mToolbarView;
    private TextView mToolbarTitle;

    private PictureReceiver mPictureReceiver;

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
                this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.fromString(
                        getBundleExtra.getString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE)));
            }

            if(getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_ID) != null){
                this.setJournalID(getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_ID));
            }


            if(getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM)!= null) {
                this.setSelectedAlbum((Album) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM));
            }

            this.selectOpenFragment(this.getCurrentOpenFragmentType());

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
        getLogging().debug(TAG, "onBackPressed");
        Intent intent = new Intent();
        intent.putExtra(Constant.BUNDLE_PARAM_JOURNAL_ID, this.getJournalID());
        intent.putExtra(Constant.BUNDLE_PARAM_ALBUM, this.getSelectedAlbum());
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
        this.setToolbarTitleBaseOnCurrentFragment();
    }

    @Override
    public void setToolbarTitle(String title){
        this.mToolbarTitle.setText(title);
    }

    public void selectOpenFragment(EnumOpenPictureActivityType openFragmentType) {
        if(openFragmentType != null) {
            this.setCurrentOpenFragmentType(openFragmentType);
        }

        switch(openFragmentType){
            case OPEN_FRAGMENT_ALBUM:
                this.getLogging().debug(TAG, "OPEN_FRAGMENT_ALBUM");

                if(this.getFacebookLogic().getIsFacebookLogin()){
                    this.getFacebookLogic().sendGraphRequest(this, EnumFacebookQueryType.GET_ALL_ALBUM, "");
                }

                this.getLogic().getUserAllAlbum(this, this.getCurrentUser().getUserID());
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_ALBUM);
                this.displayFragmentAlbum();

                break;

            case OPEN_FRAGMENT_LIST_PICTURES:
                this.getLogging().debug(TAG, "OPEN_FRAGMENT_LIST_PICTURES");

                if(this.getFacebookLogic().getIsFacebookLogin() && this.getSelectedAlbum() != null){
                    this.getFacebookLogic().sendGraphRequest(this,
                            EnumFacebookQueryType.GET_ALL_ALBUM_PICTURES,
                            this.getSelectedAlbum().getAlbumID());
                }

                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.displayFragmentPictures(this.getSelectedAlbum());

                break;

            case SELECT_FACEBOOK_ALBUM:
                this.getLogging().debug(TAG, "SELECT_FACEBOOK_ALBUM");
                if(this.getFacebookLogic().getIsFacebookLogin()){
                    this.getFacebookLogic().sendGraphRequest(this, EnumFacebookQueryType.GET_ALL_ALBUM, "");
                }

                this.setToolbarTitle(Constant.TITLE_FRAGMENT_CHOOSE_FACEBOOK_ALBUM);
                this.displayFragmentFacebookSelectionAlbum();

                break;

            case SELECT_FACEBOOK_PICTURE:
                this.getLogging().debug(TAG, "OPEN_FRAGMENT_LIST_PICTURES");

                if(this.getFacebookLogic().getIsFacebookLogin() && this.getSelectedAlbum() != null){
                    this.getFacebookLogic().sendGraphRequest(this,
                            EnumFacebookQueryType.GET_ALL_ALBUM_PICTURES,
                            this.getSelectedAlbum().getAlbumID());
                }

                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.displayFragmentFacebookSelectionPictures(this.getSelectedAlbum());

                break;

        }
    }

    public PictureReceiver getPictureReceiver(){
        if(this.mPictureReceiver == null){
            this.initPictureReceiver();
        }
        return this.mPictureReceiver;
    }

    public void backToFragmentPicture() {
        this.getLogging().debug(TAG, "backToFragmentPicture");

        if(this.fragmentManager != null && this.fragmentManager.getBackStackEntryCount() > 0) {

/*
            this.getLogging().debug(TAG, "======Before delete====");
            for(int i =0; i<this.fragmentManager.getBackStackEntryCount(); i++) {
                getLogging().debug(TAG, i + " => " + this.fragmentManager.getBackStackEntryAt(i).getName());
            }

* */
            if(this.fragmentManager.findFragmentByTag(Constant.FRAGMENT_FACEBOOK_SELECTION_PICTURES_NAME) != null) {
                //this.removeFragment(Constant.FRAGMENT_FACEBOOK_SELECTION_PICTURES_NAME);
                this.fragmentManager.popBackStackImmediate();
            }

            if(this.fragmentManager.findFragmentByTag(Constant.FRAGMENT_FACEBOOK_SELECTION_ALBUM_NAME) != null) {
                //this.removeFragment(Constant.FRAGMENT_FACEBOOK_SELECTION_ALBUM_NAME);
                this.fragmentManager.popBackStackImmediate();
            }

            /*
            Fragment fragPicture = this.fragmentManager.findFragmentByTag(Constant.FRAGMENT_PICTURES_NAME);
            if(fragPicture != null){
                this.replaceFragment(fragPicture, Constant.FRAGMENT_PICTURES_NAME);
            }

            this.getLogging().debug(TAG, "======After delete====");
            for(int i =0; i<this.fragmentManager.getBackStackEntryCount(); i++) {
                getLogging().debug(TAG, i + " => " + this.fragmentManager.getBackStackEntryAt(i).getName());
            }
            */

            Fragment frag = this.fragmentManager.findFragmentByTag(Constant.FRAGMENT_PICTURES_NAME);

            if(frag != null) {
                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.OPEN_FRAGMENT_LIST_PICTURES);
                this.replaceFragment(frag, Constant.FRAGMENT_PICTURES_NAME);
            }


        }
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
    }

    /*
    // TODO - show fragment picture after remove all facebook fragment stuff
    private void refreshAndDisplayFragmentPicture(Album newAlbum) {
        if(this.mFragmentPictures != null) {
            this.mFragmentPictures.updateGridPicturesAdapter(newAlbum.getPictureList());
        }

        this.replaceFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES_NAME);
    }
    */

    private void displayFragmentPictures(Album album){
        this.getLogging().debug(TAG, "displayFragmentPictures");

        this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.OPEN_FRAGMENT_LIST_PICTURES);

        this.mFragmentPictures = FragmentPictures.newInstance(album);
        if(!isAnyFragmentExist()){ // no fragment exist
            this.getLogging().debug(TAG, "Replace Fragment");
            this.replaceFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES_NAME);
        }else{
            this.addFragment(this.mFragmentPictures, Constant.FRAGMENT_PICTURES_NAME);
        }

    }

    private void displayFragmentFacebookSelectionPictures(Album album) {
        this.getLogging().debug(TAG, "displayFragmentFacebookSelectionPictures");

        this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.SELECT_FACEBOOK_PICTURE);

        this.mFragmentFacebookSelectionPictures = FragmentFacebookSelectionPictures.newInstance(album);
        if(!isAnyFragmentExist()){ // no fragment exist
            this.replaceFragment(this.mFragmentFacebookSelectionPictures, Constant.FRAGMENT_FACEBOOK_SELECTION_PICTURES_NAME);
        }else{
            this.addFragment(this.mFragmentFacebookSelectionPictures, Constant.FRAGMENT_FACEBOOK_SELECTION_PICTURES_NAME);
        }
    }

    private void displayFragmentAlbum(){
        this.getLogging().debug(TAG, "displayFragmentAlbum");

        this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.OPEN_FRAGMENT_ALBUM);

        this.mFragmentAlbum = FragmentAlbum.newInstance(this.getFacebookLogic().getIsFacebookLogin(),
                                                        this.getFacebookLogic().getCurrentFacebookAccessTokenString());
        this.replaceFragment(this.mFragmentAlbum, Constant.FRAGMENT_ALBUM_NAME);
    }

    private void displayFragmentFacebookSelectionAlbum() {
        this.getLogging().debug(TAG, "displayFragmentFacebookSelectionAlbum");

        this.setCurrentOpenFragmentType(EnumOpenPictureActivityType.SELECT_FACEBOOK_ALBUM);

        this.mFragmentFacebookSelectionAlbum = FragmentFacebookSelectionAlbum.newInstance(
                                                this.getFacebookLogic().getIsFacebookLogin(),
                                                this.getFacebookLogic().getCurrentFacebookAccessTokenString());
        this.replaceFragment(this.mFragmentFacebookSelectionAlbum, Constant.FRAGMENT_FACEBOOK_SELECTION_ALBUM_NAME);
    }


    private void replaceFragment(Fragment newFrag, String name){
        this.getLogging().debug(TAG, "replaceFragment => " + name);
        if(newFrag != null){
            this.getLogging().debug(TAG, "replace Fragment newFrag is not null");
            this.fragmentTransaction = this.fragmentManager.beginTransaction();
            this.fragmentTransaction.replace(R.id.container_fragment, newFrag, name)
                    .addToBackStack(name)
                    .commit();
        //.commitAllowingStateLoss();
        }
    }

    private void addFragment(Fragment newFrag, String name){
        this.getLogging().debug(TAG, "addFragment");
        if(newFrag != null){
            this.fragmentTransaction = this.fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .add(R.id.container_fragment, newFrag, name)
                    .addToBackStack(name)
                    .commit();
        }
    }

    private void removeFragment(String fragmentName) {
        this.getLogging().debug(TAG, "removeFragment => " + fragmentName);
        if(!this.getParser().isStringEmpty(fragmentName)) {

            Fragment frag = this.fragmentManager.findFragmentByTag(fragmentName);

            if(frag!= null) {
                this.fragmentTransaction = this.fragmentManager.beginTransaction();
                this.fragmentTransaction.remove(frag).commit();
            }
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

        if(this.getCurrentOpenFragmentType() != null){
        this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter getCurrentOpenFragmentType => " + getCurrentOpenFragmentType().toString());
            switch(this.getCurrentOpenFragmentType()) {

                case OPEN_FRAGMENT_ALBUM:
                    if(this.mFragmentAlbum != null) {
                        this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentAlbum.updateGridAlbumAdapter");
                        this.mFragmentAlbum.updateGridAlbumAdapter(list);
                    }else{
                        this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentAlbum is null");
                    }
                    break;

                case SELECT_FACEBOOK_ALBUM:
                    if(this.mFragmentFacebookSelectionAlbum != null) {
                        this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentFacebookSelectionAlbum.updateGridAlbumAdapter");
                        this.mFragmentFacebookSelectionAlbum.updateGridAlbumAdapter(list);
                    }else{
                        this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter -> mFragmentFacebookSelectionAlbum is null");
                    }
                    break;
            }
        }else{
            this.getLogging().debug(TAG, "updateFragmentAlbumGridAdapter getCurrentOpenFragmentType is null");

        }



    }

    public void updateFragmentPicturesGridAdapter(PictureList list) {

        this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter getCurrentOpenFragmentType => " + getCurrentOpenFragmentType().toString());

        switch(this.getCurrentOpenFragmentType()) {
            case OPEN_FRAGMENT_LIST_PICTURES:
                if(this.mFragmentPictures != null) {
                    this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentPictures.updateGridPicturesAdapter");
                    this.mFragmentPictures.updateGridPicturesAdapter(list);
                }else{
                    this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentPictures is null");
                }

                break;

            case SELECT_FACEBOOK_PICTURE:
                if(this.mFragmentFacebookSelectionPictures != null) {
                    this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentFacebookSelectionPictures.updateGridPicturesAdapter");
                    this.mFragmentFacebookSelectionPictures.updateGridPicturesAdapter(list);
                }else{
                    this.getLogging().debug(TAG, "updateFragmentPicturesGridAdapter -> mFragmentFacebookSelectionPictures is null");
                }

                break;
        }

    }

    public void uploadFileUrl(String url, String albumID) {
        getLogging().debug(TAG, "uploadFileUrl url => " + url);
        this.getLogic().uploadNewPictureURL(this, url, this.getCurrentUser().getUserID(), albumID);
    }

    public void uploadFacebookImage(String userID, String albumID, String url) {
        getLogging().debug(TAG, "uploadFacebookImage url => " + url);
        getLogic().uploadFacebookImage(this, userID, albumID, url);
    }

    // TODO: 4/8/2015 when user from addNewJournal comes to here to add new picture, it will become null cos journal is not created
    private void updateCurrentUserAlbum(Album newAlbum) {
        this.getCurrentUser().getJournalList().get(this.getJournalID()).setAlbum(newAlbum);
    }


    private EnumOpenPictureActivityType getCurrentOpenFragmentType() {
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

    private FragmentFacebookSelectionPictures getFragmentFacebookSelectionPictures() {
        return this.mFragmentFacebookSelectionPictures;
    }

    private AlbumList getAlbumList() {
        return this.mAlbumList;
    }

    private Album getSelectedAlbum() {
        return this.mAlbum;
    }

    private View getToolbar() {
        return this.mToolbarView;
    }

    private void setCurrentOpenFragmentType(EnumOpenPictureActivityType type) {
        this.openType = type;
    }

    private void setJournalID(String id) {
        this.journalID = id;
    }

    private void setAlbumList(AlbumList list) {
        this.mAlbumList = list;
    }

    private void setSelectedAlbum(Album a) {
        this.mAlbum = null;
        this.mAlbum = new Album(a);
    }

    @Override
    public void onFragmentPicturesInteraction(int requestType) {
        switch (requestType) {
            case Constant.DIALOG_REQUEST_OPEN_INTENT_IMAGES:
                // open up all images intent choices
                this.openImagesIntent();
                break;

            case Constant.DIALOG_REQUEST_OPEN_INTENT_CAMERA:



                switch (this.getAndroidVersion()) {

                    case LESS_THAN_19:
                        this.openCameraIntent();
                        break;
                    case MORE_THAN_EQUAL_19:
                        File photoFile = null;
                        try{
                            photoFile = this.createImageFile();
                        }catch(IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        this.openCameraIntent(photoFile);
                        break;
                }

                break;

            case Constant.DIALOG_REQUEST_OPEN_FACEBOOK_ALBUM:
                // open fragment album that contain all facebook albums
                this.openFacebookAlbum();
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
                            Constant.STRING_EMPTY);
                }

                this.setToolbarTitle(Constant.TITLE_FRAGMENT_PICTURES);
                this.displayFragmentPictures(selectedAlbum);

                break;
        }
    }

    @Override
    public void onFragmentFacebookSelectionAlbumInteraction(int request, Album selectedAlbum) {
        switch(request) {
            case FragmentFacebookSelectionAlbum.REQUEST_OPEN_FRAGMENT_FACEBOOK_SELECTION_PICTURES: // open facebook album pictures
                this.getLogging().debug(TAG, "onFragmentFacebookSelectionAlbumInteraction REQUEST_OPEN_FRAGMENT_FACEBOOK_SELECTION_PICTURES");
                if(this.getFacebookLogic().getIsFacebookLogin()){
                    this.getFacebookLogic().sendGraphRequest(this,
                            EnumFacebookQueryType.GET_ALL_ALBUM_PICTURES,
                            selectedAlbum.getAlbumID());
                }

                this.setToolbarTitle(Constant.TITLE_FRAGMENT_CHOOSE_FACEBOOK_PICTURE);
                this.displayFragmentFacebookSelectionPictures(selectedAlbum);

                break;
        }
    }

    @Override
    public void onFragmenFacebookSelectiontPicturesInteraction(int requestType, String url) {
        switch (requestType) {
            case FragmentFacebookSelectionPictures.REQUEST_UPLOAD_FACEBOOK_IMAGE_FILE:

                //TODO - temp disable upload, test display fragmentPicture back
                this.backToFragmentPicture(); // close fragment facebook picture and album, back to fragmentPicture
                this.uploadFacebookImage(getCurrentUser().getUserID(), this.getSelectedAlbum().getAlbumID(), url);
                break;

        }
    }

    /**
     * onActivityResult is used to receive the result from Intent (images)
     * **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String newUri = "";

        if(resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //data.getExtras().get("data").to;

            this.getLogging().debug(TAG, "onActivityResult uri string => " + uri);

            if(uri != null) {
                switch (requestCode) {
                    case REQUEST_PICK_IMAGE_INTENT:

                        newUri = getPath(uri);

                    /*
                    switch (this.getAndroidVersion()) {

                        case LESS_THAN_19:
                            newUri = getPath(uri);
                            this.getLogging().debug(TAG, "onActivityResult LESS_THAN_19 get path from uri string => " + newUri);
                            break;
                        case MORE_THAN_EQUAL_19:
                            newUri = getPath_API19(this, uri);
                            this.getLogging().debug(TAG, "onActivityResult MORE_THAN_EQUAL_19 get path from uri string => " + newUri);
                            break;
                    }
                    */


                        this.getLogic().uploadNewPicture(this, newUri,
                                CurrentLoginUser.getUser().getUserID(),
                                this.getSelectedAlbum().getAlbumID());


                        break;

                    case REQUEST_IMAGE_CAPTURE:
                        switch (this.getAndroidVersion()) {
                            case LESS_THAN_19:

                                newUri = getPath(uri);
                                this.getLogging().debug(TAG, "onActivityResult LESS_THAN_19 REQUEST_IMAGE_CAPTURE uri => " + newUri);


                                break;

                            case MORE_THAN_EQUAL_19:

                                break;
                        }





                        break;

                }
            }

        }else{
            this.getLogging().debug(TAG, "resultCode may not be ok or data is null");
        }








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


    private File createImageFile() throws IOException {
        String fileName = "test_image";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(fileName, ".jpg", dir);

        return image;
    }


    /*
    @SuppressLint("NewApi")
    private String getPath_API19(Context context, Uri uri) {
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            getLogging().debug(TAG, "wholeID => " + wholeID);
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];
                getLogging().debug(TAG, "getPath_API19 id => " + id);
                String[] column = {MediaStore.Images.Media.DATA};

                String sel = MediaStore.Images.Media._ID + "=?";
                getLogging().debug(TAG, "getPath_API19 sel => " + sel);
                Cursor cursor = context.getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }else {
            filePath = uri.getPath();
        }

        getLogging().debug(TAG, "getPath_API19 returning filepath => " + filePath);

        return filePath;

    }
    */

    /**
     * onReceiveResult is used to receive the picture service result
     * **/
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumPictureServiceType type = null;
        Album updatedAlbum = null;
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

                            switch(this.getCurrentOpenFragmentType()) {
                                case OPEN_FRAGMENT_LIST_PICTURES:
                                    if(this.getFragmentPictures() != null) {
                                        this.getFragmentPictures().updateUploadImageProgressValue(progressValue);
                                    }
                                    break;

                                case SELECT_FACEBOOK_PICTURE:
                                    if(this.getFragmentFacebookSelectionPictures() != null) {
                                        this.getFragmentFacebookSelectionPictures().updateUploadImageProgressValue(progressValue);
                                    }

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

                        updatedAlbum = getParser().parseJsonToSpecificAlbum(jsonResult);

                        this.setSelectedAlbum(updatedAlbum);
                        //this.updateCurrentUserAlbum(updatedAlbum);
                        this.updateFragmentPicturesGridAdapter(updatedAlbum.getPictureList());
                        if(this.getFragmentPictures() != null) {
                            this.getFragmentPictures().closeUploadImageProgressDialog();
                        }

                        break;

                    case UPLOAD_FACEBOOK_IMAGE:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult UPLOAD_FACEBOOK_IMAGE -> jsonResult => " + jsonResult);

                        updatedAlbum = getParser().parseJsonToSpecificAlbum(jsonResult);
                        this.getLogging().debug(TAG, "onReceiveResult UPLOAD_FACEBOOK_IMAGE -> updatedAlbum number of pictures => " + updatedAlbum.getPictureList().size());

                        this.setSelectedAlbum(updatedAlbum);
                        this.updateFragmentPicturesGridAdapter(updatedAlbum.getPictureList());

                } //end switch

                break;

            case PictureService.STATUS_ERROR:
                this.getLogging().debug(TAG, "PictureService.STATUS_ERROR");
                break;
        }

    }



}
