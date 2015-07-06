package com.orbital.lead.logic.Preference;

import android.content.Context;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by joseph on 5/7/2015.
 */
public class History {
    private static final History mHistory = new History();
    private static CustomLogging mLogging;
    private static String TAG = History.class.getClass().getSimpleName();
    private History() {
    }

    public static History getInstance() {
        mHistory.initLogging();
        /*
        File file = new File(Constant.PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME);
        mLogging.debug(TAG," history file path => " + file.getPath());
        try{
            if(!file.exists()){
                mLogging.debug(TAG, Constant.PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME + " file does not exist");
                file.createNewFile();
                mLogging.debug(TAG, Constant.PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME + " file is created");
            }
        }catch (IOException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
        }finally{
            file = null;
        }
        */
        return mHistory;
    }

    public String getRecentTags(Context context) throws FileNotFoundException, IOException {
        String value = this.getStringFromFile(context, Constant.PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME);
        return value;
    }

    public void setRecentTags(Context context, String value) throws FileNotFoundException, IOException {
        this.setStringToFile(context, Constant.PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME, value);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private String getStringFromFile(Context context, String fileName) throws FileNotFoundException, IOException{
        // at first launch, it will cause exception as file is not created
        // only when first saved 'setStringToFile', it will create the file under /data/data/com.orbital.lead/files/recent_tag.txt
        StringBuilder sb = new StringBuilder();
        String line;
        FileInputStream fis = context.openFileInput(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private void setStringToFile(Context context, String fileName, String newValue) throws FileNotFoundException, IOException{
        FileOutputStream fos = null;
        File mFile = new File(fileName);
        fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(newValue.getBytes());
        fos.flush();
        fos.close();

    }
}
