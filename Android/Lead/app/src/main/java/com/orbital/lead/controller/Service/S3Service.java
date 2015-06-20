package com.orbital.lead.controller.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.orbital.lead.logic.CustomLogging;
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


    public S3Service() {
        super(S3Service.class.getName());
        this.initLogging();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mLogging.debug(TAG, "onHandleIntent -> Service Started!");
        final ResultReceiver receiver = intent.getParcelableExtra(Constant.INTENT_SERVICE_EXTRA_RECEIVER_TAG);

        // get any extras that is passed from mainactivity
        // retrieve service type from extra
        // retrieve user id from extra
        //String url = intent.getStringExtra("url");


        Bundle bundle = new Bundle();

        /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        //try to get files from s3
        //if succcess

        bundle.putString("result", "Here is the demo data");
        receiver.send(STATUS_FINISHED, bundle);

        //if failed
        //bundle.putString(Intent.EXTRA_TEXT, e.toString());
        //receiver.send(STATUS_ERROR, bundle);


        this.mLogging.debug(TAG, "onHandleIntent -> Service Stopping!");
        this.stopSelf();

    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

}
