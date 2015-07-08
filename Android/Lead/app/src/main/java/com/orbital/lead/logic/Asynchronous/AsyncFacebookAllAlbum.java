package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumFacebookQueryType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 8/7/2015.
 */
public class AsyncFacebookAllAlbum extends AsyncTask<String, Void, String> {
    private final String TAG = "AsyncFacebookAllAlbum";
    Parser mParser;
    CustomLogging mLogging;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - Enum facebook type in string
        // params[1] - url (include access token and after id)

        initLogging();

        try{
            if(params.length == 2){
                mLogging.debug(TAG, "param length is 2");
                String response = "";
                EnumFacebookQueryType type = EnumFacebookQueryType.fromString(params[0]);
                String nextPageUrl = params[1];

                switch (type){
                    case GET_ALL_ALBUM:
                        mLogging.debug(TAG, "getFacebookNextPage...");
                        response = this.getFacebookNextPage(nextPageUrl);
                        break;
                }

                return response;

            }else{
                mLogging.debug(TAG, "params length is not 2...");
                return  "";
            }

        }catch(Exception e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    private void initLogging() {
        this.mLogging = CustomLogging.getInstance();
    }

    private String getFacebookNextPage(String url) throws IOException {
        this.urlStream = WebConnector.sendGetUrl(url, Constant.TYPE_FACEBOOK);
        return WebConnector.convertStreamToString(this.urlStream);
    }
}
