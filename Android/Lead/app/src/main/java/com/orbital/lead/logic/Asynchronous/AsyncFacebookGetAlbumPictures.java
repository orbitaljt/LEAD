package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumFacebookQueryType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joseph on 8/7/2015.
 */
public class AsyncFacebookGetAlbumPictures extends AsyncTask<String, Void, String> {
    private final String TAG = "AsyncFacebookGetAlbumPictures";
    Parser mParser;
    CustomLogging mLogging;
    InputStream urlStream = null;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - url (include access token, album id and relevant after id)
        initLogging();

        try{
            if(params.length == 1){
                mLogging.debug(TAG, "param length is 1");
                String response = "";
                String url = params[0];

                response = getFacebookAlbumPicture(url);

                return response;

            }else{
                mLogging.debug(TAG, "params length is not 1...");
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

    private String getFacebookAlbumPicture(String url) throws IOException {
        this.urlStream = WebConnector.sendGetUrl(url, Constant.TYPE_FACEBOOK);
        return WebConnector.convertStreamToString(this.urlStream);
    }
}
