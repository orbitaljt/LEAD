package com.orbital.lead.Parser;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumMessageType;
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

    private final String TAG_PARSER = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private Parser(){}

    public static Parser getInstance(){
        mParser.initLogging();
        return mParser;
    }

    public Message parseJsonToMessage(String json){
        try{
            mLogging.debug(TAG_PARSER, "parseJsonToMessage");
            JSONObject obj = new JSONObject(json);
            String code = obj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = obj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMsg = new Message(code, msg);
            return mMsg;

        }catch (JSONException e){
            mLogging.debug(TAG_PARSER, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String parseUserIDFromJson(String json){
        try{
            mLogging.debug(TAG_PARSER, "parseUserIDFromJson");
            JSONObject obj = new JSONObject(json);
            String id = obj.getString(Constant.MESSAGE_JSON_USER_ID_TAG);

            return id;
        }catch (JSONException e){
            mLogging.debug(TAG_PARSER, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public User parseJsonToUser(String json){
        User mUser = null;
        try{
            mLogging.debug(TAG_PARSER, "parseJsonToUser");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

            mUser = new User(detailObj.getString(Constant.MESSAGE_JSON_FACEBOK_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_LEAD_USER_ID_TAG),
                    detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG),
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
            mLogging.debug(TAG_PARSER, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }


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

    public int convertStringToInteger(String val){
        return Integer.parseInt(val);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

}
