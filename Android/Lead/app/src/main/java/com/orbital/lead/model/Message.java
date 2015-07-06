package com.orbital.lead.model;

/**
 * Created by joseph on 14/6/2015.
 */
public class Message {

    private String _code;
    private String _msg;
    private EnumMessageType _type;

    public Message(String code, String msg){
        this._code = code;
        this._msg = msg;
        this.checkType(code);
    }

    public String getCode(){
        return this._code;
    }

    public String getMessage(){
        return this._msg;
    }

    public EnumMessageType getType(){
        return this._type;
    }

    private void checkType(String code){
        if(code.equals(Constant.MESSAGE_HAS_RECORD)){
            this._type = EnumMessageType.HAS_RECORD;
            return;
        }else if(code.equals(Constant.MESSAGE_NO_RECORD)){
            this._type = EnumMessageType.NO_RECORD;
            return;
        }

        if(code.contains(Constant.MESSAGE_SUCCESS_TYPE)){
            this._type = EnumMessageType.SUCCESS;
            return;
        }else{
            this._type = EnumMessageType.FAILURE;
        }
    }

}
