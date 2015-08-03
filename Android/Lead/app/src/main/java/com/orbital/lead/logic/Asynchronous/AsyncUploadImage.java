package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumFileType;
import com.orbital.lead.model.EnumPictureServiceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 29/6/2015.
 */
public class AsyncUploadImage extends AsyncTask<String, Integer, String> {

    Parser mParser;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        this.initParser();
        try{

            String userID = "";
            String albumID = "";
            String imageUrl = "";
            String fileName = "";
            String fileType = "";
            String fromFacebook = "";
            String fromeLead = "";
            //String base64value = "";
            String filePath = "";

            String response = "";

            EnumPictureServiceType serviceType = EnumPictureServiceType.fromString(params[0]);

            switch(serviceType){

                case UPLOAD_PROFILE_IMAGE_URL:

                    userID = params[1];
                    imageUrl = params[2];
                    fileName = params[3];
                    fileType = params[4];
                    fromFacebook = params[5];
                    fromeLead = params[6];

                    response = this.uploadProfileImageUrl(userID, albumID, imageUrl, fileName, fileType,
                            fromFacebook, fromeLead);
                    System.out.println("AsyncUploadImage TYPE_UPLOAD_PROFILE_IMAGE_URL response => " + response);
                    return response;

                /*
                case UPLOAD_IMAGE_FILE:

                    userID = params[1];
                    albumID = params[2];
                    filePath = params[3];

                    response = this.uploadImage(userID, albumID, filePath);

                    return response;
                   */
                default:
                    return "";
            }



        }catch(IOException e){
            //print error
            e.printStackTrace();
        }catch(Exception e){
            //print error
            e.printStackTrace();
        }

        return "";
    }

    public void doProgress(int value){
        System.out.println("AsyncUploadImage current progress value => " + value);
        publishProgress(value);
    }

    private void initParser(){
        mParser = Parser.getInstance();
    }

    private String uploadProfileImageUrl(String userID, String albumID, String imageUrl, String fileName, String fileType,
                                         String fromFacebook, String fromLead) throws IOException {

        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        params.put(Constant.URL_POST_PARAMETER_TAG_ALBUM_ID, albumID);
        params.put(Constant.URL_POST_PARAMETER_TAG_URL, imageUrl); // http://...
        params.put(Constant.URL_POST_PARAMETER_TAG_FILE_NAME, fileName); //xxx.ext
        params.put(Constant.URL_POST_PARAMETER_TAG_FILE_TYPE, fileType); //jpg, png etc
        params.put(Constant.URL_POST_PARAMETER_TAG_FROM_FACEBOOK, fromFacebook); // true or false
        params.put(Constant.URL_POST_PARAMETER_TAG_FROM_LEAD, fromLead); // true or false

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_UPLOAD_PROFILE_IMAGE_URL, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

    private String uploadImageBase64(String userID, String albumID, String base64value) throws IOException  {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        //params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        //params.put(Constant.URL_POST_PARAMETER_TAG_ALBUM_ID, albumID);
        //params.put(Constant.URL_POST_PARAMETER_TAG_BASE_64, base64value);

        //this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_UPLOAD_IMAGE, params);
        //return WebConnector.convertStreamToString(this.urlStream);


        return "";
    }

    /*
    private String uploadImage(String userID, String albumID, String filePath) throws IOException  {

        String url = Constant.URL_CLIENT_SERVER;
        //HashMap<String, String> params = new HashMap<String, String>();

        String response = WebConnector.uploadFile(Constant.TYPE_UPLOAD_IMAGE, url, userID, albumID, filePath, EnumFileType.IMAGE);

        return response;
    }
    */

}
