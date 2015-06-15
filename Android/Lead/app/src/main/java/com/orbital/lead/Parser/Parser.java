package com.orbital.lead.Parser;

import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumMessageType;
import com.orbital.lead.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joseph on 14/6/2015.
 */
public class Parser {

    private static Parser mParser = new Parser();

    private Parser(){}

    public static Parser getInstance(){
        return mParser;
    }

    public Message parseJsonToMessage(String json){
        try{
            JSONObject obj = new JSONObject(json);
            String code = obj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = obj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMsg = new Message(code, msg);
            return mMsg;

        }catch (JSONException e){
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


}
