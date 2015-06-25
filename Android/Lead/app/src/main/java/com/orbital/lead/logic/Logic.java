package com.orbital.lead.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.controller.Activity.SpecificJournalActivity;
import com.orbital.lead.controller.Fragment.FragmentLogin;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.logic.Asynchronous.AsyncUserProfilePicture;
import com.orbital.lead.logic.Asynchronous.AsyncUserProfile;
import com.orbital.lead.logic.LocalStorage.LocalStorage;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.User;

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

    private Logic(){} // private constructor to prevent creating new instance

    public static Logic getInstance(){
        mLogic.initLogging();
        mLogic.initParser();
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
        HttpAsyncUserProfile mAsync = new HttpAsyncUserProfile(activity);
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
        if(this.getParser().isStringEmpty(currentUser.getJournalListID())){
            // profile picture not exist for the user
            this.getLogging().debug(TAG, "getUserJournalList => No journal list ID available.");
        }else{
            mLogging.debug(TAG, "getUserJournalList => Get journal list from web service");
            this.executeJournalService(context, EnumJournalServiceType.GET_ALL_JOURNAL, currentUser, null);
        }
    }

    public void getUserSpecificJournal(Context context, String journalID){
        if(this.getParser().isStringEmpty(journalID)){
            // profile picture not exist for the user
            this.getLogging().debug(TAG, "getUserSpecificJournal => Journal ID is empty.");
        }else{
            mLogging.debug(TAG, "getUserSpecificJournal => Get specific journal from web service");
            this.executeJournalService(context, EnumJournalServiceType.GET_SPECIFIC_JOURNAL, null, journalID);
        }
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

    private CustomLogging getLogging(){
        return this.mLogging;
    }

    private Parser getParser(){
        return this.mParser;
    }

    private void initLocalStorageLogic(){
        this.mStorageLogic = LocalStorage.getInstance();
    }

    private class HttpAsyncUserProfile extends AsyncUserProfile {

        private Context mContext;

        public HttpAsyncUserProfile(Context c){
            this.mContext = c;
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
            if(mContext instanceof MainActivity){ // calling from mainactivity
                // update current login user in mainactivity
                User user = mParser.parseJsonToUser(result);
                ((MainActivity) mContext).setCurrentUser(user);
                ((MainActivity) mContext).getUserProfilePicture(user.getUserID());
                ((MainActivity) mContext).setNavigationDrawerUserName();
                ((MainActivity) mContext).setNavigationDrawerUserEmail();
                ((MainActivity) mContext).getUserJournalList();
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
                        ((MainActivity) mContext).setUserProfilePicture(getImageLink);

                        break;
                    case Constant.PICTURE_QUERY_QUANTITY_ALBUM:
                        break;
                    case Constant.PICTURE_QUERY_QUANTITY_ALL:
                        break;

                }//end switch

            }//end if

        }//end onPostExecute

    }//end HttpAsyncUserProfilePicture




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
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG, currentUser.getJournalListID()); // journal list ID
                break;

            case GET_SPECIFIC_JOURNAL:
                intent.putExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG, journalID); // journal ID
                break;
        }


        intent.putExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, serviceType);

        if(mContext instanceof MainActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((MainActivity) mContext).getJournalReceiver());

        }else if(mContext instanceof SpecificJournalActivity){
            intent.putExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG, ((SpecificJournalActivity) mContext).getJournalReceiver());

        }

        mContext.startService(intent);
    }






}
