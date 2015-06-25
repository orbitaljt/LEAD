package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 22/6/2015.
 */
public class AsyncUserProfilePicture extends AsyncTask<String, Void, String> {
    Parser mParser;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - type
        // values = 300 / 301 / 302 (see constant)
        // params[1] - user ID
        // params[2] - picture query quantity (only for type 900)
        this.initParser();

        try{
            if(params.length > 1){
                String userID = params[1];

                if(!mParser.isStringEmpty(userID)){

                    String response = "";

                    switch(params[0]){
                        case Constant.TYPE_GET_USER_PICTURE:
                            String type = params[2];
                            response = this.getUserProfilePictureURL(userID, type);
                            System.out.println("AsyncUserPicture response => " + response);

                            return response;

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

    private String getUserProfilePictureURL(String userID, String quantity) throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);
        params.put(Constant.URL_POST_PARAMETER_TAG_PICTURE_QUERY_QUANTITY, quantity);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_USER_PICTURE, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }
}
