package com.orbital.lead.controller.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.LocalStorage.LocalStorage;
import com.orbital.lead.logic.s3.S3Logic;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumS3ServiceType;

/**
 * Created by joseph on 19/6/2015.
 */
public class S3Service extends IntentService{

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = -1;

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private S3Logic mS3Logic;
    private Parser mParser;
    private LocalStorage mLocalStorage;
    private String userID;
    private String userProfilePicID;
    private String userProfilePicFileName;
    private EnumS3ServiceType serviceType;


    public S3Service() {
        super(S3Service.class.getName());
        this.initLogging();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mLogging.debug(TAG, "onHandleIntent -> Service Started!");

        boolean hasError = false;

        this.initS3Logic();
        this.initParser();
        this.initLocalStorage();

        // get any extras that is passed from mainactivity
        // retrieve service type from extra
        // retrieve user id from extra
        final ResultReceiver receiver = intent.getParcelableExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG);
        this.userID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG);
        this.serviceType = (EnumS3ServiceType) intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

        Bundle returnBundle = new Bundle();

        returnBundle.putSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, this.serviceType); // return the type of service

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        switch (this.serviceType){
            case QUERY_PHOTOS:
                //get all photos

            case QUERY_PROFILE_PICTURE:
                this.mLogging.debug(TAG, "onHandleIntent -> service type => QUERY_PROFILE_PICTURE");

                // get the profile picture based on the file ID
                this.userProfilePicID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_ID_TAG);
                this.userProfilePicFileName = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_FILENAME_TAG);

                Bitmap result = this.getS3UserProfilePicture(this.userProfilePicFileName);
                if(!mParser.isBitmapEmpty(result)){ //there's bitmap
                    boolean isSaved = this.mLocalStorage.saveProfilePicture(this.userProfilePicFileName, result);

                    if(isSaved){
                        //saved
                        this.mLogging.debug(TAG, "onHandleIntent -> Successfully saved bitmap into local storage");
                        returnBundle.putString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_STATUS_TAG, Constant.INTENT_SERVICE_RESULT_DOWNLOAD_SUCCESS_VALUE); // download status - success
                        // mLocalStorage.getProfilePictureDirectory() +
                        returnBundle.putString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_PROFILE_PICTURE_FILENAME_TAG, this.userProfilePicFileName); // full file path in local storage
                        receiver.send(STATUS_FINISHED, returnBundle);

                    }else{
                        //not saved
                        this.mLogging.debug(TAG, "onHandleIntent -> Unable to save bitmap into local storage");
                        returnBundle.putString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_STATUS_TAG, Constant.INTENT_SERVICE_RESULT_DOWNLOAD_FAILURE_VALUE); // download status - failure
                        receiver.send(STATUS_ERROR, returnBundle);

                    }

                }else{
                    // unable to download
                    this.mLogging.debug(TAG, "onHandleIntent -> Bitmap is empty");
                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_DOWNLOAD_STATUS_TAG, Constant.INTENT_SERVICE_RESULT_DOWNLOAD_FAILURE_VALUE); // download status - failure
                    receiver.send(STATUS_ERROR, returnBundle);
                }

                break;

            default:
                this.mLogging.debug(TAG, "onHandleIntent -> No service type found");

        }


        //try to get files from s3
        //if succcess

        //returnBundle.putString("result", "Here is the demo data");
        //receiver.send(STATUS_FINISHED, returnBundle);

        //if failed
        //bundle.putString(Intent.EXTRA_TEXT, e.toString());
        //receiver.send(STATUS_ERROR, bundle);


        this.mLogging.debug(TAG, "onHandleIntent -> Service Stopping!");
        this.stopSelf();

    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initS3Logic(){
        this.mS3Logic = S3Logic.getInstance(this);
    }

    private void initParser() { this.mParser = Parser.getInstance(); }

    private void initLocalStorage() { this.mLocalStorage = LocalStorage.getInstance(); }

    private void getS3ListObject(){
        //String key = this.getUserID() + Constant.AWS_S3_SLASH + Constant.AWS_S3_FOLDER_PROFILE_NAME + Constant.AWS_S3_SLASH;
        //this.mS3Logic.getListObject(Constant.AWS_S3_BUCKET_NAME, key);
    }

    private Bitmap getS3UserProfilePicture(String pictureName){
        String key = this.getUserID() + Constant.AWS_S3_SLASH + Constant.AWS_S3_FOLDER_PICTURE_NAME + Constant.AWS_S3_SLASH + pictureName;
        this.mLogging.debug(TAG, "key -> " + key);
        S3ObjectInputStream is = this.mS3Logic.getObject(Constant.AWS_S3_BUCKET_NAME, key);
        if(is == null){
            return null;
        }
        return this.mS3Logic.getObjectImage(is);
    }

    private String getUserID(){
        return this.userID;
    }

    private EnumS3ServiceType getServiceType(){
        return this.serviceType;
    }


}
