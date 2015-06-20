package com.orbital.lead.logic.s3;

/**
 * Created by joseph on 19/6/2015.
 */
import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transfermanager.*;
import com.amazonaws.auth.*;
import com.orbital.lead.R;

public class S3Logic {

    private static S3Logic mlogic = new S3Logic();
    private AWSCredentials mCredential;
    private TransferManager mTransferManager;

    private Context mContext;

    private S3Logic(){

    }

    public static S3Logic getInstance(Context context){
        mlogic.initContext(context);
        mlogic.initCredential();
        mlogic.initTransferManager();
        return mlogic;
    }

    private void initContext(Context context){
        this.mContext = context;
    }

    private Context getContext(){
        return this.mContext;
    }

    private  void initCredential(){
        String getID = this.getContext().getResources().getString(R.string.aws_s3_id);
        String getSecretKey = this.getContext().getResources().getString(R.string.aws_s3_secret_key);
        this.mCredential = new BasicAWSCredentials(getID, getSecretKey);
    }

    private AWSCredentials getCredential(){
        return this.mCredential;
    }

    private void initTransferManager(){
        this.mTransferManager = new TransferManager(this.getCredential());

    }







}
