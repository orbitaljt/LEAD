package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joseph on 14/6/2015.
 */
public class AsyncLogin extends AsyncTask<String, Void, String> {

    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // Login using normal username and password
        try{
            String username = params[0];
            String password = params[1];

            String result = this.login(username, password);

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
        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_LOGIN, username, password);
        return  WebConnector.convertStreamToString(this.urlStream);
    }


}
