package com.orbital.lead.model;

/**
 * Created by joseph on 20/6/2015.
 */
public class Picture {

    private String _userID;
    private String _pictureID;
    private String _pictureType;
    private String _albumID;
    private String _title;
    private String _description;
    private String _hashTag;
    private String _createdDate;
    private String _createdTime;
    private boolean _fromFacebook;
    private boolean _fromLead;

    public Picture(String userID, String picID, String type){
        this._userID = userID;
        this._pictureID = picID;
        this._pictureType = type;
    }

    public String getAlbumID(){
        return this._albumID;
    }

    public String getType(){
        return this._pictureType;
    }

    public String getTitle(){
        return this._title;
    }

    public String getDescription(){
        return this._description;
    }

    public String getHashTag(){
        return this._hashTag;
    }

    public String getCreatedDate(){
        return this._albumID;
    }

    public String getCreatedTime(){
        return this._albumID;
    }

    public boolean isFromFacebook(){
        return this._fromFacebook;
    }

    public boolean isFromLead(){
        return this._fromLead;
    }

    public void setType(String value){
        this._pictureType = value;
    }

    public void setAlbumID(String id){
        this._albumID = id;
    }

    public void setTitle(String value){
        this._title = value;
    }

    public void setDescription(String value){
        this._description = value;
    }

    public void setHashTag(String value){
        this._hashTag = value;
    }

    public void setCreatedDate(String value){
        this._createdDate = value;
    }

    public void setCreatedTime(String value){
        this._createdTime = value;
    }

    public void setIsFromFacebook(boolean value){
        this._fromFacebook = value;
    }

    public void setIsFromLead(boolean value){
        this._fromLead = value;
    }

/*
    public static enum Type{
        PNG, JPEG, NONE
    }
*/

}
