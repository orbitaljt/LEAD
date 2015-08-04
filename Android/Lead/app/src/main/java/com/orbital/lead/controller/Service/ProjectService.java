package com.orbital.lead.controller.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.LocalStorage.LocalStorage;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.logic.s3.S3Logic;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.EnumProjectServiceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 19/6/2015.
 */
public class ProjectService extends IntentService{

    public static final int STATUS_RUNNING = 10000;
    public static final int STATUS_FINISHED = 10001;
    public static final int STATUS_ERROR = -1;

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private Parser mParser;
    private String userID;
    private String projectID;
    private String detail;
    private String urlStreamStr;
    private EnumProjectServiceType serviceType;
    InputStream urlStream = null;

    public ProjectService() {
        super(ProjectService.class.getName());
        this.initLogging();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mLogging.debug(TAG, "onHandleIntent -> Service Started!");

        boolean hasError = false;
        String url = "";
        HashMap<String, String> params;
        Bundle returnBundle = new Bundle();

        this.initParser();

        // get any extras that is passed from context
        // retrieve service type from extra
        // retrieve user id from extra
        final ResultReceiver receiver = intent.getParcelableExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG);
        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG) != null){
            this.userID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG);
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG) != null){
            this.projectID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG);
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_DETAIL_TAG) != null){
            this.detail = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_DETAIL_TAG);
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG) != null){
            this.serviceType = (EnumProjectServiceType) intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
        }


        this.urlStreamStr = "";

        returnBundle.putSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, this.getServiceType()); // return the type of service

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        try{
            url = Constant.URL_CLIENT_SERVER;

            switch (this.getServiceType()){
                case GET_ALL_PROJECT:
                    // get all project - no requirement
                    params = new HashMap<String, String>();
                    this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_ALL_PROJECT, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                case ADD_NEW_PROJECT:

                    break;
                case UPDATE_SPECIFIC_PROJECT:

                    break;

                case DELETE_SPECIFC_PROJECT:
                    break;

                default:
                    this.mLogging.debug(TAG, "onHandleIntent -> No service type found");
                    receiver.send(STATUS_ERROR, returnBundle);
                    break;

            }// end switch

        }catch(IOException e) {
            //print error
            e.printStackTrace();
            this.mLogging.debug(TAG, "onHandleIntent -> IOException");
            receiver.send(STATUS_ERROR, returnBundle);
        }//end try

        this.mLogging.debug(TAG, "onHandleIntent -> Service Stopping!");
        this.stopSelf();

    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initParser() { this.mParser = Parser.getInstance(); }

    private String getUserID() {
        return this.userID;
    }

    private String getProjectID() {
        return this.projectID;
    }

    private String getDetail() {
        return this.detail;
    }

    private EnumProjectServiceType getServiceType(){
        return this.serviceType;
    }


}
