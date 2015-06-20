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
        // values = 300 / 301 / 302 (see constant)
        // params[1] - user ID
        this.initParser();

        try{
            if(params.length > 1){
                String userID = params[1];

                if(!mParser.isStringEmpty(userID)){

                    String response = "";

                    switch(params[0]){
                        case Constant.TYPE_GET_USER_PROFILE:
                            response = this.getUserProfile(userID);
                            System.out.println("AsyncUserProfile response => " + response);
                            return response;

                        case Constant.TYPE_UPDATE_USER_PROFILE:


                        case Constant.TYPE_CREATE_USER_PROFILE:

                        default:
                            return "";
                    }
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

    private String updateUserProfile(String userID) throws IOException{
       return "";
    }

    private String createUserProfile() throws IOException{
        return "";
    }
}
