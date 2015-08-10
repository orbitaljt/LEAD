package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumPictureServiceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 18/6/2015.
 */
public class AsyncDeleteAlbum extends AsyncTask<String, Void, String> {

    Parser mParser;
    InputStream urlStream = null;

    final int INDEX_SERVICE_TYPE = 0;
    final int INDEX_ALBUM_ID = 1;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - service type
        String albumID = "";
        String response = "";
        this.initParser();

        try{
            if(params.length <= 0) {
                throw new Exception("user ID is required");
            }

            EnumPictureServiceType serviceType = EnumPictureServiceType.fromString(params[INDEX_SERVICE_TYPE]);

            switch(serviceType){
                case DELETE_ALBUM:
                    albumID = params[INDEX_ALBUM_ID];
                    response = this.deleteAlbum(albumID);
                    System.out.println("AsyncDeleteAlbum DELETE_ALBUM response => " + response);
                    return response;

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

    private void initParser(){
        mParser = Parser.getInstance();
    }

    /**
     * Gets new generated journal and album IDs from server
     * **/
    private String deleteAlbum(String albumID) throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_ALBUM_ID, albumID);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_DELETE_ALBUM, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

}
