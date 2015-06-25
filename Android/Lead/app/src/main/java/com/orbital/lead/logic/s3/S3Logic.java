package com.orbital.lead.logic.s3;

/**
 * Created by joseph on 19/6/2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.mobileconnectors.s3.transfermanager.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.orbital.lead.R;
import com.orbital.lead.logic.CustomLogging;

import java.io.IOException;

public class S3Logic {

    private final String TAG = this.getClass().getSimpleName();
    private static S3Logic mlogic = new S3Logic();

    private AWSCredentials mCredential;
    private AmazonS3Client mClient;
    private TransferManager mTransferManager;

    private Context mContext;
    private CustomLogging mLogging;

    private S3Logic(){

    }

    public static S3Logic getInstance(Context context){
        mlogic.initLogging();
        mlogic.initContext(context);
        mlogic.initCredential();
        mlogic.initS3Client();
        mlogic.initTransferManager();
        return mlogic;
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
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

    private void initS3Client(){
        mClient = new AmazonS3Client(getCredential());
    }

    private AmazonS3Client getS3Client(){
        return this.mClient;
    }

    public void getListObject(String bucketName, String key){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                                    .withBucketName(bucketName)
                                                    .withPrefix(key);
        ObjectListing objectListing;

        do {
            objectListing = this.getS3Client().listObjects(listObjectsRequest);

            for (S3ObjectSummary objectSummary :
                    objectListing.getObjectSummaries()) {
                System.out.println( " - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() +
                        ")");
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());


    }

    /**
     *  @param bucketName A valid bucket name
     *  @param key A valid prefix - must have a target file name
     * **/
    public S3ObjectInputStream getObject(String bucketName, String key){
        try {
            GetObjectRequest request = new GetObjectRequest(bucketName, key);
            S3Object object = this.getS3Client().getObject(request);
            S3ObjectInputStream objectContent = object.getObjectContent();

            return objectContent;
        }catch(AmazonS3Exception e){
            mLogging.debug(TAG, key + " is not found." + e);
            e.printStackTrace();
            return null;
        }

    }

    public Bitmap getObjectImage(S3ObjectInputStream is){
        Bitmap image = null;
        try {
            // Use the inputStream and close it after.
            image = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                mLogging.debug(TAG, "Error trying to close image stream. " + e);
            }
        }

        return image;
    }


    private void initTransferManager(){
        this.mTransferManager = new TransferManager(this.getCredential());
    }









}
