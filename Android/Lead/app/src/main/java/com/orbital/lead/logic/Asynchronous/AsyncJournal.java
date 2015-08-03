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

    @Override
    final protected String doInBackground(String... params) {
        // params[0] - serviec type

        String response = "";
        this.initParser();

        try{
            EnumJournalServiceType serviceType = EnumJournalServiceType.fromString(params[0]);

            switch(serviceType){
                case GET_NEW_JOURNAL_ALBUM_ID:

                    response = this.getNewJournalAlbumID();
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
    private String getNewJournalAlbumID() throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_NEW_JOURNAL_ALBUM_ID, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }

}
