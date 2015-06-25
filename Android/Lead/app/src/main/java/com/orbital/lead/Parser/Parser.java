package com.orbital.lead.Parser;

import android.graphics.Bitmap;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumMessageType;
import com.orbital.lead.model.EnumPictureType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.Message;
import com.orbital.lead.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joseph on 14/6/2015.
 */
public class Parser {
    private static Parser mParser = new Parser();

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private Parser(){}

    public static Parser getInstance(){
        mParser.initLogging();
        return mParser;
    }

    public Message parseJsonToMessage(String json){
        try{
            mLogging.debug(TAG, "parseJsonToMessage");
            JSONObject obj = new JSONObject(json);
            String code = obj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = obj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMsg = new Message(code, msg);
            return mMsg;

        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String parseUserIDFromJson(String json){
        try{
            mLogging.debug(TAG, "parseUserIDFromJson");
            JSONObject obj = new JSONObject(json);
            String id = obj.getString(Constant.MESSAGE_JSON_USER_ID_TAG);

            return id;
        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public User parseJsonToUser(String json){
        User mUser = null;
        try{
            mLogging.debug(TAG, "parseJsonToUser");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

            mUser = new User(detailObj.getString(Constant.MESSAGE_JSON_FACEBOK_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_LEAD_USER_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_TYPE_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_EXPERIENCE_LIST_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_FIRST_NAME_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_MIDDLE_NAME_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_lAST_NAME_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_BIRTHDAY_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_ADDRESS_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_CITY_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_STATE_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_EMAIL_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_LAST_LOGIN_DATE_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_LAST_LOGIN_TIME_TAG),
                    this.convertStringToInteger(detailObj.getString(Constant.MESSAGE_JSON_AGE_TAG)));

            return mUser;
        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public String parseJsonToProfilePictureLink(String json){

        try{
            mLogging.debug(TAG, "parseJsonToProfilePictureLink");
            JSONObject topObj = new JSONObject(json);
            //String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            //String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);
            String link = topObj.getString(Constant.MESSAGE_JSON_DETAIL_TAG);

            return link;
        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public JournalList parseJsonToJournalList(String json){
        JournalList list = null;
        try {
            mLogging.debug(TAG, "parseJsonToJournalList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(isMessageSuccess(mMessage)){
                list = new JournalList();

                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) {
                    JSONObject journalObj = detailArray.getJSONObject(i);

                    Journal mJournal = new Journal(journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_ALBUM_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CONTENT_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_IS_PUBLISHED_TAG));

                    list.addJournal(mJournal);

                }
            }

            return list;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String createPictureCoverUrl(String pictureCoverID, String pictureCoverType, String userID){
        return Constant.URL_SMALL_PICTURE
                .replace(Constant.URL_DUMMY_USER_ID, userID)
                .replace(Constant.URL_DUMMY_FILE_NAME,
                        this.generateFilename(pictureCoverID, pictureCoverType));
    }


    public boolean isMessageSuccess(Message msg){
        if(msg.getType() == EnumMessageType.SUCCESS){
            return true;
        }else{
            return false;
        }
    }

    public boolean isStringEmpty(String val){
        if(val.trim().equals("") || val.trim().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isBitmapEmpty(Bitmap bmp){
        if(bmp == null){
            return true;
        }
        return false;
    }

    public EnumPictureType getType(String value){
        if(value.toLowerCase().trim().equals("png")){
            return EnumPictureType.PNG;
        }else if(value.toLowerCase().trim().equals("jpeg")){
            return EnumPictureType.JPEG;
        }else if(value.toLowerCase().trim().equals("jpg")){
            return EnumPictureType.JPEG;
        }else{
            return EnumPictureType.NONE;
        }
    }

    public String generateFilename(String name, String ext){
        return name + "." + ext;
    }




    public int convertStringToInteger(String val){
        return Integer.parseInt(val);
    }

    public boolean convertStringToBoolean(String val){
        if(val.equals("false") || val.equals("0") ){
            return false;
        }else if (val.equals("true") || val.equals("1")){
            return true;
        }else{
            return false;
        }
    }

    public String convertBooleanToString(boolean val){
        return String.valueOf(val);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

}
