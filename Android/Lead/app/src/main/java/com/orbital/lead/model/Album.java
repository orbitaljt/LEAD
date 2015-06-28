package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orbital.lead.Parser.Parser;

/**
 * Created by joseph on 27/6/2015.
 */
public class Album implements Parcelable {
    private String _userID;
    private String _albumID;
    private String _title;
    private String _description;
    private String _hashTag;
    private String _createdDate;
    private String _createdTime;
    private String lastModifiedDate;
    private String lastModifiedTime;
    private PictureList _list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._userID);
        dest.writeString(this._albumID);
        dest.writeString(this._title);
        dest.writeString(this._description);
        dest.writeString(this._hashTag);
        dest.writeString(this._createdDate);
        dest.writeString(this._createdTime);
        dest.writeString(this.lastModifiedDate);
        dest.writeString(this.lastModifiedTime);
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>(){
        public Album createFromParcel(Parcel pc){
            return new Album(pc);
        }
        public Album[] newArray(int size){
            return new Album[size];
        }
    };


    public Album(String userID, String albumID, String title, String desc, String hashTag,
                 String createdDate, String createdTime, String lastModDate, String lastModTime,
                 PictureList list){
        this._userID = userID;
        this._albumID = albumID;
        this._title = title;
        this._description = desc;
        this._hashTag = hashTag;
        this._createdDate = createdDate;
        this._createdTime = createdTime;
        this.lastModifiedDate = lastModDate;
        this.lastModifiedTime = lastModTime;
        this._list = list;
    }


    public String getUserID() {
        return this._userID;
    }

    public String getAlbumID(){
        return this._albumID;
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

    public String getLastModifiedDate(){
        return this.lastModifiedDate;
    }

    public String getLastModifiedTime(){
        return this.lastModifiedTime;
    }

    public PictureList getPictureList() {
        return this._list;
    }

    public void setUserID(String id) {
        this._userID = id;
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

    public void setLastModifiedDate(String val){
        this.lastModifiedDate = val;
    }

    public void setLastModifiedTime(String val){
        this.lastModifiedTime = val;
    }

    public void setPictureList(PictureList list) {
        this._list = list;
    }

    private Album(Parcel pc){
        this._userID = pc.readString();
        this._albumID = pc.readString();
        this._title = pc.readString();
        this._description = pc.readString();
        this._hashTag = pc.readString();
        this._createdDate = pc.readString();
        this._createdTime = pc.readString();
        this.lastModifiedDate = pc.readString();
        this.lastModifiedTime = pc.readString();
    }

}
