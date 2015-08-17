package com.orbital.lead.controller.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumFileType;
import com.orbital.lead.model.EnumPictureServiceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 27/6/2015.
 */
public class PictureService extends IntentService  {
    public static final int STATUS_RUNNING = 900;
    public static final int STATUS_FINISHED = 901;
    public static final int STATUS_ERROR = -901;
    private final String TAG = this.getClass().getSimpleName();

    private CustomLogging mLogging;
    private Parser mParser;

    private EnumPictureServiceType serviceType;
    InputStream urlStream = null;
    private String urlStreamStr;
    private String albumID;
    private String userID;
    private String uploadFilePath;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private boolean fromFacebook;
    private boolean fromLead;

    private ResultReceiver receiver;

    public PictureService() {
        super(PictureService.class.getName());
        this.initLogging();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = "";
        HashMap<String, String> params;
        Bundle returnBundle = new Bundle();

        this.initParser();
        this.mLogging.debug(TAG, "onHandleIntent");

        this.urlStreamStr = "";

        this.receiver = intent.getParcelableExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG);

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG) != null){
            this.mLogging.debug(TAG, "setServiceType" );
            this.setServiceType((EnumPictureServiceType) intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG));
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_ALBUM_ID_TAG) != null){
            this.mLogging.debug(TAG, "setAlbumID" );
           this.setAlbumID(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_ALBUM_ID_TAG));
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG) != null){
            this.mLogging.debug(TAG, "setUserID" );
            this.setUserID(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG));
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_PATH_TAG) != null){
            this.mLogging.debug(TAG, "setUploadFilePath" );
            this.setUploadFilePath(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_PATH_TAG));
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_URL_TAG) != null){
            this.mLogging.debug(TAG, "setFileUrl" );
            this.setFileUrl(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_URL_TAG));
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_TYPE_TAG) != null){
            this.setFileType(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_TYPE_TAG));
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_NAME_TAG) != null){
            this.setFileName(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FILE_NAME_TAG));
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FROM_FACEBOOK_TAG) != null){
            this.setFromFacebook(intent.getBooleanExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FROM_FACEBOOK_TAG, false));
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FROM_LEAD_TAG) != null){
            this.setFromLead(intent.getBooleanExtra(Constant.INTENT_SERVICE_EXTRA_UPLOAD_FROM_LEAD_TAG, false));
        }

        returnBundle.putSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, this.getServiceType()); // return the type of service

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, returnBundle);

        this.mLogging.debug(TAG, "this.getServiceType() => " + this.getServiceType().toString());

        try {
            switch (this.getServiceType()) {
                case GET_SPECIFIC_ALBUM:
                    // get all photo of an album
                    // requires album ID
                    url = Constant.URL_CLIENT_SERVER;
                    params = new HashMap<String, String>();
                    params.put(Constant.URL_POST_PARAMETER_TAG_ALBUM_ID, this.getAlbumID());

                    this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_SPECIFIC_ALBUM, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                case GET_ALL_ALBUM:
                    // get all photo from all album
                    // requires user ID
                    url = Constant.URL_CLIENT_SERVER;
                    params = new HashMap<String, String>();

                    params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, this.getUserID());
                    this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_ALL_ALBUM, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                case DELETE_ALBUM:
                    // delete a specific album
                    // requires album ID

                case UPLOAD_IMAGE_FILE:
                    // upload an image file based
                    // requires user ID, album ID, file data
                    url = Constant.URL_CLIENT_SERVER;
                    this.urlStreamStr = WebConnector.uploadFile(this,
                            Constant.TYPE_UPLOAD_IMAGE,
                            url,
                            this.getUserID(),
                            this.getAlbumID(),
                            this.getUploadFilePath(),
                            EnumFileType.IMAGE);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                case UPLOAD_FACEBOOK_IMAGE:
                    url = Constant.URL_CLIENT_SERVER;
                    params = new HashMap<String, String>();
                    params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, this.getUserID());
                    params.put(Constant.URL_POST_PARAMETER_TAG_ALBUM_ID, this.getAlbumID());
                    params.put(Constant.URL_POST_PARAMETER_TAG_URL, this.getFileUrl());
                    params.put(Constant.URL_POST_PARAMETER_TAG_FILE_NAME, this.getFileName());
                    params.put(Constant.URL_POST_PARAMETER_TAG_FILE_TYPE, this.getFileType());
                    params.put(Constant.URL_POST_PARAMETER_TAG_FROM_FACEBOOK, mParser.convertBooleanToString(this.getFromFacebook()));
                    params.put(Constant.URL_POST_PARAMETER_TAG_FROM_LEAD, mParser.convertBooleanToString(this.getFromLead()));

                    this.urlStream = WebConnector.uploadFileUrl(this, url, Constant.TYPE_UPLOAD_FACEBOOK_IMAGE, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                default:
                    this.mLogging.debug(TAG, "onHandleIntent -> No service type found");
                    receiver.send(STATUS_ERROR, returnBundle);
                    break;
            }

        }catch(IOException e) {
            //print error
            e.printStackTrace();
            this.mLogging.debug(TAG, "onHandleIntent -> IOException");
            receiver.send(STATUS_ERROR, returnBundle);
        }//end try

        this.mLogging.debug(TAG, "onHandleIntent -> Service Stopping!");
        this.stopSelf();
    }

    public void updateProgress(int value, EnumPictureServiceType serviceType) {
        Bundle returnBundle = new Bundle();

        switch (serviceType) {
            case UPLOAD_IMAGE_FILE:

                if(this.receiver != null){
                    returnBundle.putSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, serviceType); // return the type of service
                    returnBundle.putInt(Constant.INTENT_SERVICE_RESULT_UPLOAD_FILE_PROGRESS_VALUE_TAG, value);
                    this.receiver.send(STATUS_RUNNING, returnBundle);
                }

                break;
        }
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }


    private void initParser() { this.mParser = Parser.getInstance(); }

    private void setServiceType(EnumPictureServiceType type) {
        this.serviceType = type;
    }

    private void setAlbumID(String id){
        this.albumID = id;
    }

    private void setUserID(String id) {
        this.userID = id;
    }

    private void setUploadFilePath(String path) {
        this.uploadFilePath = path;
    }

    private void setFileName(String val) {
        this.fileName = val;
    }

    private void setFileType(String val) {
        this.fileType = val;
    }

    private void setFileUrl(String val) {
        this.fileUrl = val;
    }

    private void setFromFacebook(boolean val) {
        this.fromFacebook = val;
    }

    private void setFromLead(boolean val) {
        this.fromLead = val;
    }

    private EnumPictureServiceType getServiceType() {
        return this.serviceType;
    }

    private String getAlbumID() {
        return this.albumID;
    }

    private String getUserID() {
        return this.userID;
    }

    private String getUploadFilePath() {
        return this.uploadFilePath;
    }

    private String getFileName() {
        return this.fileName;
    }

    private String getFileType() {
        return this.fileType;
    }

    private String getFileUrl() {
        return this.fileUrl;
    }

    private boolean getFromFacebook() {
        return this.fromFacebook;
    }

    private boolean getFromLead() {
        return this.fromLead;
    }




}
