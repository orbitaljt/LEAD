package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orbital.lead.Parser.Parser;

/**
 * Created by joseph on 20/6/2015.
 */
public class Picture implements Parcelable {

    private Parser mParser = Parser.getInstance();

    private String _userID;
    private String _pictureID;
    private EnumPictureType _pictureType;
    private String _albumID;
    private String _title;
    private String _description;
    private String _createdDate;
    private String _createdTime;
    private String _thumbnailUrl; // small image url
    private String _actualUrl;
    private boolean _fromFacebook;
    private boolean _fromLead;
    private TagList tagList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._userID);
        dest.writeString(this._pictureID);
        dest.writeString((this._pictureType == null) ? "" : this._pictureType.name());
        dest.writeString(this._albumID);
        dest.writeString(this._title);
        dest.writeString(this._description);
        dest.writeString(this._createdDate);
        dest.writeString(this._createdTime);
        dest.writeString(this._thumbnailUrl);
        dest.writeString(this._actualUrl);
        dest.writeInt(this._fromFacebook ? 1 : 0);
        dest.writeInt(this._fromLead ? 1 : 0);
        dest.writeParcelable(tagList, flags);
    }

    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>(){
        public Picture createFromParcel(Parcel pc){
            return new Picture(pc);
        }
        public Picture[] newArray(int size){
            return new Picture[size];
        }
    };



    public Picture(String userID, String picID, String type){
        this._userID = userID;
        this._pictureID = picID;
        this._pictureType = getParser().getPictureType(type);
        this._thumbnailUrl = this.getParser().createPictureThumbnailUrl(picID, type, userID);
        this._actualUrl = this.getParser().createPictureNormalUrl(picID, type, userID);
    }

    public String getPictureID () {
        return this._pictureID;
    }

    public String getAlbumID(){
        return this._albumID;
    }

    public EnumPictureType getType(){
        return this._pictureType;
    }

    public String getTitle(){
        return this._title;
    }

    public String getDescription(){
        return this._description;
    }

    public String getCreatedDate(){
        return this._albumID;
    }

    public String getCreatedTime(){
        return this._albumID;
    }

    public String getThumbnailUrl() {
        return this._thumbnailUrl;
    }

    public String getAcutalUrl() {
        return this._actualUrl;
    }

    public TagList getTagList() {
        return this.tagList;
    }

    public boolean isFromFacebook(){
        return this._fromFacebook;
    }

    public boolean isFromLead(){
        return this._fromLead;
    }

    public void setType(EnumPictureType value){
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

    public void setCreatedDate(String value){
        this._createdDate = value;
    }

    public void setCreatedTime(String value){
        this._createdTime = value;
    }

    public void setThumbnailUrl(String value) {
        this._thumbnailUrl = value;
    }

    public void setActualUrl(String value) {
        this._actualUrl = value;
    }

    public void setIsFromFacebook(boolean value){
        this._fromFacebook = value;
    }

    public void setIsFromLead(boolean value){
        this._fromLead = value;
    }

    public void setTagList(TagList list) {
        this.tagList = list;
    }

    private Parser getParser() {
        return mParser;
    }


    private Picture(Parcel pc){
        this._userID = pc.readString();
        this._pictureID = pc.readString();
        try {
            this._pictureType = EnumPictureType.valueOf(pc.readString());
        } catch (IllegalArgumentException x) {
            this._pictureType = null;
        }
        this._albumID = pc.readString();
        this._title = pc.readString();
        this._description = pc.readString();
        this._createdDate = pc.readString();
        this._createdTime = pc.readString();
        this._thumbnailUrl = pc.readString();
        this._actualUrl = pc.readString();
        this._fromFacebook = (pc.readInt() == 0) ? false : true;
        this._fromLead = (pc.readInt() == 0) ? false : true;
        this.tagList = (TagList) pc.<TagList> readParcelable(TagList.class.getClassLoader());
    }

/*
    public static enum Type{
        PNG, JPEG, NONE
    }
*/

}
