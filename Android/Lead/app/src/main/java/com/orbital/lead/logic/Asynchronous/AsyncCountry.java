package com.orbital.lead.logic.Asynchronous;

import android.os.AsyncTask;

import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumLoginType;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.logic.WebConnector;
import com.orbital.lead.logic.CustomLogging;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Terence on 7/9/2015.
 */
public class AsyncCountry extends AsyncTask<String, Void, String> {

    Parser mParser;
    InputStream urlStream = null;
    CustomLogging mlogging;

    @Override
    final protected String doInBackground(String... params) {
        //params[0] - getAllCountries
        String response = "";
        try{
            response = getAllCountries();
            return response;


        }catch(IOException e){
            //print error
            e.printStackTrace();
        }catch(Exception e){
            //print error
            e.printStackTrace();
        }

        return "";
    }

    private void initParser() {
        mParser = Parser.getInstance();
    }

    private String getAllCountries() throws IOException {
        String url = Constant.URL_CLIENT_SERVER;
        HashMap<String, String> params = new HashMap<String, String>();

        this.urlStream = WebConnector.downloadUrl(url, Constant.TYPE_GET_ALL_COUNTRIES, params);
        return WebConnector.convertStreamToString(this.urlStream);
    }
}
