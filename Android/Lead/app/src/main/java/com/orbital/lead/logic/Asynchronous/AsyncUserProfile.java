package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 18/6/2015.
 */
public class AsyncUserProfile extends AsyncTask<String, Void, String> {

    Parser mParser;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - type
        // type values = 300 / 302 / 303 (see constant)
        // params[1] - user ID
        // params[2] - details (for 302 and 303)
        this.initParser();

        String userID = "";
        String response = "";
        String detail = "";

        try{
            if(params.length > 1){
                userID = params[1];

                switch(params[0]){
                    case Constant.TYPE_GET_USER_PROFILE:
                        if(!mParser.isStringEmpty(userID)){
                            response = this.getUserProfile(userID);
                            System.out.println("AsyncUserProfile TYPE_GET_USER_PROFILE response => " + response);
                            return response;
                        }
                        return "";

                    case Constant.TYPE_UPDATE_USER_PROFILE:
                        if(!mParser.isStringEmpty(userID)){
                            detail = params[2];
                            response = this.updateUserProfile(userID, detail);
                            System.out.println("AsyncUserProfile TYPE_UPDATE_USER_PROFILE response => " + response);
                            return response;
                        }
                        return "";

                    case Constant.TYPE_CREATE_USER_PROFILE:
                        // does not need user ID
                        detail = params[2];
                        response = this.insertUserProfile(detail);
                        System.out.println("AsyncUserProfile TYPE_CREATE_USER_PROFILE response => " + response);
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

    private String getUserProfile(String userID) throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_PROFILE, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

    private String updateUserProfile(String userID, String detail) throws IOException{
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        params.put(Constant.URL_POST_PARAMETER_TAG_DETAIL, detail);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_UPDATE_USER_PROFILE, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

    private String insertUserProfile(String detail) throws IOException{
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        //params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        params.put(Constant.URL_POST_PARAMETER_TAG_DETAIL, detail);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_CREATE_USER_PROFILE, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }
}
