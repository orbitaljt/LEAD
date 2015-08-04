package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 29/6/2015.
 */
public class AsyncUploadImage extends AsyncTask<String, Void, String> {

    Parser mParser;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        this.initParser();
        try{
            if(params.length == 7){
                String userID = params[1];
                String imageUrl = params[2];
                String fileName = params[3];
                String fileType = params[4];
                String fromFacebook = params[5];
                String fromeLead = params[6];

                String response = "";

                switch(params[0]){
                    case Constant.TYPE_UPLOAD_IMAGE_URL:
                        response = this.uploadImageUrl(userID, imageUrl, fileName, fileType,
                                                    fromFacebook, fromeLead);
                        System.out.println("AsyncUploadImage response => " + response);
                        return response;

                    default:
                        return "";
                }


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

    private void initParser(){
        mParser = Parser.getInstance();
    }

    private String uploadImageUrl(String userID, String imageUrl, String fileName, String fileType,
                            String fromFacebook, String fromLead) throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        params.put(Constant.URL_POST_PARAMETER_TAG_URL, imageUrl); // http://...
        params.put(Constant.URL_POST_PARAMETER_TAG_FILE_NAME, fileName); //xxx.ext
        params.put(Constant.URL_POST_PARAMETER_TAG_FILE_TYPE, fileType); //jpg, png etc
        params.put(Constant.URL_POST_PARAMETER_TAG_FROM_FACEBOOK, fromFacebook); // true or false
        params.put(Constant.URL_POST_PARAMETER_TAG_FROM_LEAD, fromLead); // true or false

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_UPLOAD_IMAGE_URL, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }


}
