package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumJournalServiceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by joseph on 18/6/2015.
 */
public class AsyncJournal extends AsyncTask<String, Void, String> {

    Parser mParser;
    InputStream urlStream = null;

    final int INDEX_SERVICE_TYPE = 0;
    final int INDEX_USER_ID = 1;

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - service type
        String userID = "";
        String response = "";
        this.initParser();

        try{
            if(params.length <= 0) {
                throw new Exception("user ID is required");
            }

            EnumJournalServiceType serviceType = EnumJournalServiceType.fromString(params[INDEX_SERVICE_TYPE]);

            switch(serviceType){
                case GET_NEW_JOURNAL_ALBUM_ID:
                    userID = params[INDEX_USER_ID];
                    response = this.getNewJournalAlbumID(userID);
                    System.out.println("AsyncJournal TYPE_GET_NEW_JOURNAL_ALBUM_ID response => " + response);
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
    private String getNewJournalAlbumID(String userID) throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.URL_POST_PARAMETER_TAG_USER_ID, userID);

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_NEW_JOURNAL_ALBUM_ID, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

}
