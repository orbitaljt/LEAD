package com.orbital.lead.logic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.EditSpecificJournalActivity;
import com.orbital.lead.controller.Activity.PictureActivity;
import com.orbital.lead.controller.Activity.SpecificJournalActivity;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.controller.Fragment.FragmentLogin;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.logic.Asynchronous.AsyncUploadImage;
import com.orbital.lead.logic.Asynchronous.AsyncUserProfilePicture;
import com.orbital.lead.logic.Asynchronous.AsyncUserProfile;
import com.orbital.lead.logic.LocalStorage.LocalStorage;
import com.orbital.lead.logic.Preference.History;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.EnumPictureServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by joseph on 14/6/2015.
 */
public class Logic {

    private static Logic mLogic = new Logic();

    private final String TAG = this.getClass().getSimpleName();

    private ProgressDialog prog;
    private Parser mParser;
    private LocalStorage mStorageLogic;
    private CustomLogging mLogging;
    private History mHistoryPref;

    private Logic(){} // private constructor to prevent creating new instance

    public static Logic getInstance(){
        mLogic.initLogging();
        mLogic.initParser();
        mLogic.initHistoryPreference();
        mLogic.initLocalStorageLogic();
        return mLogic;
    }

    /**
     * @param username User input a username in plaintext
     * @param password User input a password in plaintext
     * @return Message object - consists of code and message
     *
     * **/
    public void login(FragmentLogin frag, String username, String password){
        //frag.HttpAsyncLogin mAsyncLogin = new frag.HttpAsyncLogin(frag);
       // mAsyncLogin.execute(username, password);


    }

    public void getUserProfile(Activity activity, String leadUserID){
        HttpAsyncUserProfile mAsync = new HttpAsyncUserProfile(activity, Constant.TYPE_GET_USER_PROFILE);
        mAsync.execute(Constant.TYPE_GET_USER_PROFILE, leadUserID);
    }

    public void getUserProfilePicture(Activity activity, String leadUserID){
        HttpAsyncUserProfilePicture mAsync = new HttpAsyncUserProfilePicture(activity, Constant.PICTURE_QUERY_QUANTITY_PROFILE);
        mAsync.execute(Constant.TYPE_GET_USER_PICTURE, leadUserID, Constant.PICTURE_QUERY_QUANTITY_PROFILE);
    }

    /*
    public Bitmap getUserProfilePicture(Context mContext, User currentUser){
        if(this.mParser.isStringEmpty(currentUser.getProfilePictureID()) ||
                currentUser.getProfilePictureType() == EnumPictureType.NONE){
            // profile picture not exist for the user
            mLogging.debug(TAG, "getUserProfilePicture => Either user database has no profile pic ID or picture type is NONE");
            return null;
        }else{
            // check if local storage has the picture
            // user_id.extension
            String fileName = currentUser.getProfilePictureID() + Constant.STORAGE_DOT_EXTENSION + currentUser.getProfilePictureType().toString();
            //mLogging.debug(TAG, "getUserProfilePicture => fileName => " + fileName);

            if(!this.mStorageLogic.isProfilePictureExist(fileName)){ // not exist
                // get picture from S3
                // will still return null
                // activity will get image from S3 via onReceiveResult
                mLogging.debug(TAG, "getUserProfilePicture => Get image from S3");
                this.callS3Service(mContext, EnumS3ServiceType.QUERY_PROFILE_PICTURE, currentUser);
            }else{
                mLogging.debug(TAG, "getUserProfilePicture => Get image from local storage");
                return this.getLocalStorageLoadProfilePicture(fileName);
            }

        }

        return null;
    }
*/

    public String getLocalStorageProfileDirectory(){
        return this.mStorageLogic.getProfilePictureDirectory();
    }

    public Bitmap getLocalStorageLoadProfilePicture(String fileName){
        return this.mStorageLogic.loadProfilePicture(fileName);
    }


    public void getUserJournalList(Context context, User currentUser){
        if(currentUser == null){
            // profile picture not exist for the user
            this.getLogging().debug(TAG, "getUserJournalList => No journal list ID available.");
        }else{
            mLogging.debug(TAG, "getUserJournalList => Get journal list from web service");
            this.executeJournalService(context, EnumJournalServiceType.GET_ALL_JOURNAL, currentUser, null);
        }
    }

    public void getUserSpecificAlbum(Context context, String albumID){
        if(this.getParser().isStringEmpty(albumID)){
            this.getLogging().debug(TAG, "getUserSpecificAlbum => Album ID is empty.");
        }else{
            mLogging.debug(TAG, "getUserSpecificAlbum => Get specific album from web service with album ID => " + albumID);
            this.executePictureService(context, EnumPictureServiceType.GET_SPECIFIC_ALBUM, albumID, "");
        }
    }

    public void getUserAllAlbum(Context context, String userID){
        if(this.getParser().isStringEmpty(userID)){
            this.getLogging().debug(TAG, "getUserAllAlbum => User ID is empty.");
        }else{
            mLogging.debug(TAG, "getUserAllAlbum => Get all album from web service with user ID => " + userID);
            this.executePictureService(context, EnumPictureServiceType.GET_ALL_ALBUM, "", userID);
        }
    }

    public void updateUserProfileDatabase(Context context, String userID, String detail){
        // detail in json string
        HttpAsyncUserProfile mAsync = new HttpAsyncUserProfile(context, Constant.TYPE_UPDATE_USER_PROFILE);
        mAsync.execute(Constant.TYPE_UPDATE_USER_PROFILE, userID, detail);
    }

    public void uploadProfilePictureFromFacebook(Context context, String userID, String imageUrl, String fileName, String fileType,
                                                 boolean fromFacebook, boolean fromLead){
        HttpAsyncUploadImageUrl mAsync = new HttpAsyncUploadImageUrl(context);
        mAsync.execute(Constant.TYPE_UPLOAD_IMAGE_URL, userID, imageUrl, fileName, fileType,
                        mParser.convertBooleanToString(fromFacebook),
                        mParser.convertBooleanToString(fromLead));
    }

    public void insertUserProfileDatabase(Context context, String detail) {
        // detail in json string
        HttpAsyncUserProfile mAsync = new HttpAsyncUserProfile(context, Constant.TYPE_CREATE_USER_PROFILE);
        mAsync.execute(Constant.TYPE_CREATE_USER_PROFILE, "", detail);
    }




    /*============= Display activity =================*/
    public void displaySpecificJournalActivity(Context context, Journal journal){
        mLogging.debug(TAG, "displaySpecificJournalActivity with journal ID => " + journal.getJournalID());
        Intent newIntent = new Intent(context, SpecificJournalActivity.class);
        Bundle mBundle = new Bundle();
        //mBundle.putString(Constant.BUNDLE_PARAM_JOURNAL_ID, journal.getJournalID());
        //mBundle.putString(Constant.BUNDLE_PARAM_JOURNAL_IMAGE_URL, journal.getPictureCoverUrl());
        mBundle.putParcelable(Constant.BUNDLE_PARAM_JOURNAL, journal);

        newIntent.putExtras(mBundle);
        context.startActivity(newIntent);
    }

    public void displayPictureActivity(Context context, String type, Album album){
        //, ArrayList<Picture> picList
        mLogging.debug(TAG, "displayPictureActivity");
        Intent newIntent = new Intent(context, PictureActivity.class);

        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE, type);
        mBundle.putParcelable(Constant.BUNDLE_PARAM_ALBUM, album);
        //mBundle.putBoolean(Constant.BUNDLE_PARAM_IS_FACEBOOK_LOGIN, isFacebookLogin);
        //mBundle.putParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST, picList);

        newIntent.putExtras(mBundle);
        context.startActivity(newIntent);
    }

    public void displayEditSpecificJournalActivity(Context context, Journal journal){
        Intent newIntent = new Intent(context, EditSpecificJournalActivity.class);
        Bundle mBundle = new Bundle();
        //mBundle.putString(Constant.BUNDLE_PARAM_JOURNAL_ID, journal.getJournalID());
        //mBundle.putString(Constant.BUNDLE_PARAM_JOURNAL_IMAGE_URL, journal.getPictureCoverUrl());
        mBundle.putParcelable(Constant.BUNDLE_PARAM_JOURNAL, journal);

        newIntent.putExtras(mBundle);
        context.startActivity(newIntent);
    }









    public void showSnackBarRed(Activity activity){
        Snackbar mSnackBar = Snackbar.with(activity)
                .text("")
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                .actionLabel("")
                .actionColor(Color.WHITE)
                .color(Color.RED)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                    }
                });

        SnackbarManager.show(mSnackBar, activity);
    }

    public void showSnackBarNormal(Activity activity){
        Snackbar mSnackBar = Snackbar.with(activity)
                .text("")
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                .actionLabel("")
                .actionColor(Color.WHITE)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                    }
                });

        SnackbarManager.show(mSnackBar, activity);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initParser(){
        if(this.mParser == null) {
            this.mParser = Parser.getInstance();
        }
    }

    private void initHistoryPreference() {
        if(this.mHistoryPref == null) {
            this.mHistoryPref = History.getInstance();
        }
    }

    private CustomLogging getLogging(){
        return this.mLogging;
    }

    private Parser getParser(){
        return this.mParser;
    }

    private void initLocalStorageLogic(){
        this.mStorageLogic = LocalStorage.getInstance();
    }

    private void showPicture(Context context, final ViewAnimator animator, ImageView targetView, String url){
        animator.setDisplayedChild(1);

        /*
        Picasso.with(context)
                .load(url)
                .noFade()
                .into(targetView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //mLogging.debug(TAG, "Picasso onSuccess with URL => " + url);
                        animator.setDisplayedChild(0); // show actual image
                    }

                    @Override
                    public void onError() {
                        //mLogging.debug(TAG, "Picasso onError with URL => " + url);
                        animator.setDisplayedChild(2); // failed to load
                    }
                });
        */
        ImageLoader.getInstance()
                .displayImage(url, targetView, CustomApplication.getDisplayImageOptions(),
                        new SimpleImageLoadingListener(){
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                //holder.progressBar.setProgress(0);
                                //holder.progressBar.setVisibility(View.VISIBLE);
                                mLogging.debug(TAG, "onLoadingStarted");
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                //holder.progressBar.setVisibility(View.GONE);
                                mLogging.debug(TAG, "onLoadingFailed");
                                animator.setDisplayedChild(2);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                //holder.progressBar.setVisibility(View.GONE);
                                mLogging.debug(TAG, "onLoadingComplete");
                                animator.setDisplayedChild(0);
                            }
                        },
                        new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                            }
                        });

    }


    private class HttpAsyncUserProfile extends AsyncUserProfile {

        private Context mContext;
        private String type = "";

        public HttpAsyncUserProfile(Context c, String type){
            this.mContext = c;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            //prog = new ProgressDialog(this.mContext);
           // prog.setMessage("Logging in...");
            //prog.show();
        }

        @Override
        protected void onPostExecute(final String result) {
           // if (prog != null && prog.isShowing()) {
           //     prog.dismiss();
           // }//end if
            switch(type){
                case Constant.TYPE_GET_USER_PROFILE:
                    if(mContext instanceof MainActivity){ // calling from mainactivity
                        // update current login user in mainactivity
                        User user = mParser.parseJsonToUser(result);
                        ((MainActivity) mContext).setCurrentUser(user);
                        ((MainActivity) mContext).updateCurrentUserProfileFromFacebook();
                        ((MainActivity) mContext).updateCurrentUserProfileToDatabase();
                        ((MainActivity) mContext).uploadUserProfilePictureUrl();
                        //((MainActivity) mContext).getUserProfilePicture(user.getUserID());
                        ((MainActivity) mContext).setUserProfilePicture();
                        ((MainActivity) mContext).setNavigationDrawerUserName();
                        ((MainActivity) mContext).setNavigationDrawerUserEmail();
                        ((MainActivity) mContext).getUserJournalList();
                    }
                    break;

                case Constant.TYPE_UPDATE_USER_PROFILE:
                    if(mContext instanceof MainActivity) { // calling from mainactivity
                        mLogging.debug(TAG, "HttpAsyncUserProfile onPostExecute result => " + result);
                    }
                    break;

                case Constant.TYPE_CREATE_USER_PROFILE:

                    String newUserID = mParser.parseUserIDFromJson(result);

                    if(mContext instanceof MainActivity) { // calling from mainactivity
                        if(!mParser.isStringEmpty(newUserID)){ // user id is not empty
                            mLogging.debug(TAG, "HttpAsyncUserProfile TYPE_CREATE_USER_PROFILE onPostExecute newUserID => " + newUserID);

                            ((MainActivity) mContext).setNewCurrentUserID(newUserID);
                            ((MainActivity) mContext).uploadUserProfilePictureUrl();
                            ((MainActivity) mContext).setUserProfilePicture();
                            ((MainActivity) mContext).setNavigationDrawerUserName();
                            ((MainActivity) mContext).setNavigationDrawerUserEmail();
                        }else{
                            mLogging.debug(TAG, "Unable to get new lead user ID");
                        }
                    }


                    break;
            }


        }
    }

    private class HttpAsyncUserProfilePicture extends AsyncUserProfilePicture {

        private Context mContext;
        private String query_type = "";

        public HttpAsyncUserProfilePicture(Context c, String pic_query_quantity){
            this.mContext = c;
            this.query_type = pic_query_quantity;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            if(mContext instanceof MainActivity){ // calling from mainactivity

                switch(this.query_type){
                    case Constant.PICTURE_QUERY_QUANTITY_PROFILE:
                        User user = ((MainActivity) mContext).getCurrentUser();
                        String getImageLink = mParser.parseJsonToProfilePictureLink(result);

                        user.setProfilePicUrl(getImageLink);
                        //((MainActivity) mContext).setUserProfilePicture(getImageLink);

                        break;
                    case Constant.PICTURE_QUERY_QUANTITY_ALBUM:
                        break;
                    case Constant.PICTURE_QUERY_QUANTITY_ALL:
                        break;

                }//end switch

            }//end if

        }//end onPostExecute

    }//end HttpAsyncUserProfilePicture


    private class HttpAsyncUploadImageUrl extends AsyncUploadImage {
        private Context mContext;

        public HttpAsyncUploadImageUrl(Context c){
            this.mContext = c;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            mLogging.debug(TAG, "HttpAsyncUploadImageUrl onPostExecute result => " + result);

            //if(mContext instanceof MainActivity){ // calling from mainactivity


                //User user = ((MainActivity) mContext).getCurrentUser();
                //String getImageLink = mParser.parseJsonToProfilePictureLink(result);

                //user.setProfilePicUrl(getImageLink);
                //((MainActivity) mContext).setUserProfilePicture(getImageLink);


            //}//end if

        }//end onPostExecute




    }




    /*=============== SERVICE ==============*/
    /*
    private void callS3Service(Context mContext, EnumS3ServiceType serviceType, User currentUser){

        String fileName = currentUser.getProfilePictureID() + Constant.STORAGE_DOT_EXTENSION + currentUser.getProfilePictureType().toString();

        Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext, S3Service.class);

        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG, currentUser.getUserID()); // user ID
        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_ID_TAG, currentUser.getProfilePictureID()); // profile picture ID
        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_FILENAME_TAG, fileName); // profile picture file name (include extension)
        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, serviceType);
        if(mContext instanceof MainActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((MainActivity) mContext).getS3Receiver());
        }

        mContext.startService(intent);

    }
*/

    private void executeJournalService(Context mContext, EnumJournalServiceType serviceType, User currentUser, String journalID){
        Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext, JournalService.class);

        switch(serviceType){
            case GET_ALL_JOURNAL:
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG, currentUser.getUserID()); // user ID
                //intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG, currentUser.getJournalListID()); // journal list ID
                break;

            case GET_SPECIFIC_JOURNAL:
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG, journalID); // journal ID
                break;
        }


        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, serviceType);

        if(mContext instanceof MainActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((MainActivity) mContext).getJournalReceiver());

        }else if(mContext instanceof SpecificJournalActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((SpecificJournalActivity) mContext).getPictureReceiver());

        }

        mContext.startService(intent);
    }

    private void executePictureService(Context mContext, EnumPictureServiceType serviceType, String albumID, String userID) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext, PictureService.class);
        switch(serviceType){
            case GET_SPECIFIC_ALBUM:
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_ALBUM_ID_TAG, albumID); // user ID
                break;
            case GET_ALL_ALBUM:
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG, userID); // user ID
                break;
        }

        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, serviceType);

        if(mContext instanceof SpecificJournalActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((SpecificJournalActivity) mContext).getPictureReceiver());

        }else if(mContext instanceof PictureActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((PictureActivity) mContext).getPictureReceiver());
        }

        mContext.startService(intent);
    }


    /*=========== DIALOGS WITH CUSTOM LAYOUT ===========*/
    public void showEditTagProjectDialog(Context context, EnumDialogEditJournalType type, String editTextCurrentValue){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.dialog_add_new_layout, null);
        EditText mEditText = (EditText) dialogView.findViewById(R.id.edit_text_add_new);
        TextView mToolbarTitle = (TextView) dialogView.findViewById(R.id.toolbar_text_title_add_new);

        switch(type){
            case ADD_TAG:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_add_new_tag));
                mEditText.setHint(context.getResources().getString(R.string.dialog_editext_tag_hint));
                break;

            case ADD_PROJECT:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_add_new_project));
                mEditText.setHint(context.getResources().getString(R.string.dialog_editext_project_hint));
                break;

            case EDIT_TAG:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_edit_tag));
                mEditText.setText(editTextCurrentValue);
                break;

            case EDIT_PROJECT:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_edit_project));
                mEditText.setText(editTextCurrentValue);
                break;
        }

        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public void showDeleteTagProjectDialog(final Context context, final EnumDialogEditJournalType type){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.dialog_delete_layout, null);

        TextView mToolbarTitle = (TextView) dialogView.findViewById(R.id.toolbar_text_title_delete);
        TextView mText = (TextView) dialogView.findViewById(R.id.text_delete);

        switch(type) {
            case DELETE_TAG:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_delete_tag));
                mText.setText(context.getResources().getString(R.string.dialog_description_delete_tag));
                break;

            case DELETE_PROJECT:
                mToolbarTitle.setText(context.getResources().getString(R.string.dialog_toolbar_delete_project));
                mText.setText(context.getResources().getString(R.string.dialog_description_delete_project));
                break;
        }

        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }



    /*=========== POPUP MENU ===========*/
    public void showPopUpMenu(final Context context, View v, final EnumDialogEditJournalType type, final String currentValue){
        PopupMenu menu = new PopupMenu(context, v){
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dialog_overflow_edit_journal_edit: // Edit the current row
                        switch(type){
                            case EDIT_TAG: //edit tag
                                showEditTagProjectDialog(context, EnumDialogEditJournalType.EDIT_TAG, currentValue);
                                break;
                            case EDIT_PROJECT:
                                showEditTagProjectDialog(context, EnumDialogEditJournalType.EDIT_PROJECT, currentValue);
                                break;
                        }
                        return true;

                    case R.id.dialog_overflow_edit_journal_delete: // Delete the current row
                        switch(type){
                            case EDIT_TAG:
                                showDeleteTagProjectDialog(context, EnumDialogEditJournalType.DELETE_TAG);
                                break;
                            case EDIT_PROJECT:
                                showDeleteTagProjectDialog(context, EnumDialogEditJournalType.DELETE_PROJECT);
                                break;
                        }
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };

        menu.inflate(R.menu.menu_overflow_dialog_journal_edit);
        menu.show();
    }




    /*=========== ACCESS TO PREFERENCE ===========*/

    public String getHistoryTags(Context context){
        mLogging.debug(TAG, "getHistoryTags");
        try{
            return this.mHistoryPref.getRecentTags(context);

        }catch (FileNotFoundException e){
            mLogging.debug(TAG, "error -> " + e.getMessage());
            e.printStackTrace();
        }catch (IOException e){
            mLogging.debug(TAG, "error -> " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



}
