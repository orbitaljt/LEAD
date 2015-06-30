package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumLoginType;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 14/6/2015.
 */
public class AsyncLogin extends AsyncTask<String, Void, String> {

    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // Login using normal username and password
        // Login using facebook
        // params[0] - login type
        // if using normal lead account, param[1] and [2] will be username and password
        // if using facebook, param[1] will be facebook id
        String result = "";
        try{
            String loginType = params[0];
            switch(EnumLoginType.fromString(loginType)){

                case LOGIN_LEAD:
                    String username = params[1];
                    String password = params[2];

                    result = this.login(username, password);
                    break;

                case LOGIN_FACEBOOK:
                    String facebookUserID = params[1];

                    result = this.login(facebookUserID);
                    break;
            }

            return result;

        }catch(IOException e){
            //print error
            e.printStackTrace();
        }catch(Exception e){
            //print error
            e.printStackTrace();
        }

        return "";
    }

    private String login(String username, String password) throws IOException{
        String url = Constant.URL_CLIENT_SERVER;

        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put(Constant.URL_POST_PARAMETER_TAG_USERNAME, username);
        loginParams.put(Constant.URL_POST_PARAMETER_TAG_PASSWORD, password);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_LOGIN, loginParams);
        return WebConnector.convertStreamToString(this.urlStream);
    }

    private String login(String facebookUserID) throws IOException{
        String url = Constant.URL_CLIENT_SERVER;

        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put(Constant.URL_POST_PARAMETER_TAG_FACEBOOK_USER_ID, facebookUserID);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_LOGIN_WITH_FACEBOOK, loginParams);
        return WebConnector.convertStreamToString(this.urlStream);


    }

}
