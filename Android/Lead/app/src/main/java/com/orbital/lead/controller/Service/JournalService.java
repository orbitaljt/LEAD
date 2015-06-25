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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 19/6/2015.
 */
public class JournalService extends IntentService{

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = -1;

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private S3Logic mS3Logic;
    private Parser mParser;
    private LocalStorage mLocalStorage;
    private String userID;
    private String journalID;
    private String journalListID;
    private String userProfilePicID;
    private String urlStreamStr;
    private EnumJournalServiceType serviceType;
    InputStream urlStream = null;

    public JournalService() {
        super(JournalService.class.getName());
        this.initLogging();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mLogging.debug(TAG, "onHandleIntent -> Service Started!");

        boolean hasError = false;
        String url = "";
        HashMap<String, String> params;
        Bundle returnBundle = new Bundle();

        this.initS3Logic();
        this.initParser();
        this.initLocalStorage();

        // get any extras that is passed from mainactivity
        // retrieve service type from extra
        // retrieve user id from extra
        final ResultReceiver receiver = intent.getParcelableExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG);
        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG) != null){
            this.userID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_ID_TAG);
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG) != null){
            this.journalListID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG);
        }

        if(intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG) != null){
            this.journalID = intent.getStringExtra(Constant.INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG);
        }

        if(intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG) != null){
            this.serviceType = (EnumJournalServiceType) intent.getSerializableExtra(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
        }


        this.urlStreamStr = "";

        returnBundle.putSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG, this.getServiceType()); // return the type of service

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        try{
            switch (this.getServiceType()){
                case GET_ALL_JOURNAL:
                    // get all journals
                    // requires user ID and journal list ID
                    url = Constant.URL_CLIENT_SERVER;
                    params = new HashMap<String, String>();
                    params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, this.getUserID());
                    params.put(Constant.URL_POST_PARAMETER_TAG_USER_JOURNAL_LIST_ID, this.getJournalListID());

                    this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_ALL_JOURNAL, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);

                    break;

                case GET_SPECIFIC_JOURNAL:
                    // get user specific journal
                    // requires journal ID only
                    /*
                    url = Constant.URL_CLIENT_SERVER;
                    params = new HashMap<String, String>();
                    params.put(Constant.URL_POST_PARAMETER_TAG_USER_JOURNAL_ID, this.getJournalID());

                    this.mLogging.debug(TAG, "params -> getJournalID => " + getJournalID());


                    this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_SPECIFIC_JOURNAL, params);
                    this.urlStreamStr = WebConnector.convertStreamToString(this.urlStream);

                    returnBundle.putString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG, this.urlStreamStr);
                    receiver.send(STATUS_FINISHED, returnBundle);
                    */
                    break;

                case UPDATE_SPECIFIC_JOURNAL:
                    break;

                case DELETE_SPECIFC_JOURNAL:
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

    private void initS3Logic(){
        this.mS3Logic = S3Logic.getInstance(this);
    }

    private void initParser() { this.mParser = Parser.getInstance(); }

    private void initLocalStorage() { this.mLocalStorage = LocalStorage.getInstance(); }


    private String getUserID() {
        return this.userID;
    }

    private String getJournalID() {
        return this.journalID;
    }

    private String getJournalListID(){
        return this.journalListID;
    }

    private EnumJournalServiceType getServiceType(){
        return this.serviceType;
    }


}
