package com.orbital.lead.logic;

import android.net.Uri;

import com.orbital.lead.model.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by joseph on 4/5/2015.
 */
public class WebConnector {

    public WebConnector(){}

    public static InputStream downloadUrl(String urlString, String type, HashMap<String, String> mapParam) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(Constant.URL_POST_PARAMETER_TAG_QUERY_TYPE, type);

        HashMap<String, String> mp = new HashMap<String, String>(mapParam);
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            builder.appendQueryParameter(pair.getKey().toString(), pair.getValue().toString());
        }

        String query = builder.build().getEncodedQuery();
        System.out.println("Webconnector query => " + query);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        // get the response stream
        InputStream stream = conn.getInputStream();
        return stream;

    }


    public static InputStream sendGetUrl(String urlString, String type) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");

        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.connect();

        // get the response stream
        InputStream stream = conn.getInputStream();
        return stream;
    }


    public static InputStream downloadUrl(String urlString, String type, String username, String password) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        /*
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        */
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Constant.URL_POST_PARAMETER_TAG_QUERY_TYPE, type)
                .appendQueryParameter(Constant.URL_POST_PARAMETER_TAG_USERNAME, username)
                .appendQueryParameter(Constant.URL_POST_PARAMETER_TAG_PASSWORD, password);

        String query = builder.build().getEncodedQuery();
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        // get the response stream
        InputStream stream = conn.getInputStream();
        return stream;

    }





    public static String convertStreamToString(InputStream is)
            throws IOException {
        //
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        //
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

}
