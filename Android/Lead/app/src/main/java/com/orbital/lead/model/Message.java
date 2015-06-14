package com.orbital.lead.model;

/**
 * Created by joseph on 14/6/2015.
 */
public class Message {

    private String _code;
    private String _msg;

    public Message(String code, String msg){
        this._code = code;
        this._msg = msg;
    }

    public String getCode(){
        return this._code;
    }

    public String getMessage(){
        return this._msg;
    }


}
