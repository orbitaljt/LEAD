package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orbital.lead.Parser.Parser;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.logic.FacebookLogic;

/**
 * Created by joseph on 27/6/2015.
 */
public class Album implements Parcelable {
    private Parser mParser = Parser.getInstance();
    private FacebookLogic mFacebookLogic = FacebookLogic.getInstance();

    private String _userID;
    private String _albumID;
    private String pictureCoverID;
    private EnumPictureType pictureCoverType;
    private String _thumbnailUrl; // small image url
    private String _actualUrl;
    private String _title;
    private String _description;
    private String _createdDate;
    private String _createdTime;
    private String lastModifiedDate;
    private String lastModifiedTime;
    private int numofPicture;
    private PictureList _list;
    private TagList tagList;
    private boolean isFromFacebook;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._userID);
        dest.writeString(this._albumID);
        dest.writeString(this.pictureCoverID);
        dest.writeString((this.pictureCoverType == null) ? "" : this.pictureCoverType.name());
        dest.writeString(this._thumbnailUrl);
        dest.writeString(this._actualUrl);
        dest.writeString(this._title);
        dest.writeString(this._description);
        dest.writeString(this._createdDate);
        dest.writeString(this._createdTime);
        dest.writeString(this.lastModifiedDate);
        dest.writeString(this.lastModifiedTime);
        dest.writeInt(this.numofPicture);
        dest.writeParcelable(_list, flags);
        dest.writeParcelable(tagList, flags);
        dest.writeInt(this.isFromFacebook ? 1 : 0);
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>(){
        public Album createFromParcel(Parcel pc){
            return new Album(pc);
        }
        public Album[] newArray(int size){
            return new Album[size];
        }
    };


    public Album(String userID, String albumID, String picCoverID, String picCoverType, String title, String desc,
                 String createdDate, String createdTime, String lastModDate, String lastModTime,
                 PictureList list, TagList taglist, boolean isFromFacebook){
        this._userID = userID;
        this._albumID = albumID;
        this.pictureCoverID = picCoverID;
        this.pictureCoverType = getParser().getPictureType(picCoverType);
        if(isFromFacebook){
            this._thumbnailUrl = ParserFacebook.createFacebookPictureThumbnailUrl(mFacebookLogic.getCurrentFacebookAccessTokenString(), albumID);
            this._thumbnailUrl = ParserFacebook.createFacebookPictureNormalUrl(mFacebookLogic.getCurrentFacebookAccessTokenString(), albumID);

        }else{
            this._thumbnailUrl = this.getParser().createPictureThumbnailUrl(this.pictureCoverID, picCoverType, userID);
            this._actualUrl = this.getParser().createPictureNormalUrl(this.pictureCoverID, picCoverType, userID);
        }

        this._title = title;
        this._description = desc;
        this._createdDate = createdDate;
        this._createdTime = createdTime;
        this.lastModifiedDate = lastModDate;
        this.lastModifiedTime = lastModTime;
        if(list != null) {
            this.numofPicture = list.size();
        }else{
            this.numofPicture = 0;
        }
        this._list = list;
        this.tagList = taglist;
        this.isFromFacebook = isFromFacebook;
    }


    public String getUserID() {
        return this._userID;
    }

    public String getAlbumID(){
        return this._albumID;
    }

    public String getThumbnailUrl() {
        return this._thumbnailUrl;
    }

    public String getAcutalUrl() {
        return this._actualUrl;
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

    public String getLastModifiedDate(){
        return this.lastModifiedDate;
    }

    public String getLastModifiedTime(){
        return this.lastModifiedTime;
    }

    public int getNumberOfPicture() {
        return this.numofPicture;
    }

    public PictureList getPictureList() {
        return this._list;
    }

    public TagList getTagList() {
        return this.tagList;
    }

    public boolean getIsFromFacebook() {
        return this.isFromFacebook;
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

    public void setNumberOfPicture(int val) {
        this.numofPicture = val;
    }

    public void setPictureList(PictureList list) {
        this._list = list;
    }

    public void setTagList(TagList list) {
        this.tagList = list;
    }

    public void setIsFromFacebook(boolean val) {
        this.isFromFacebook = val;
    }

    private Parser getParser() {
        return mParser;
    }

    private Album(Parcel pc){
        this._userID = pc.readString();
        this._albumID = pc.readString();
        this.pictureCoverID = pc.readString();
        try {
            this.pictureCoverType = EnumPictureType.valueOf(pc.readString());
        } catch (IllegalArgumentException x) {
            this.pictureCoverType = null;
        }
        this._thumbnailUrl = pc.readString();
        this._actualUrl = pc.readString();
        this._title = pc.readString();
        this._description = pc.readString();
        this._createdDate = pc.readString();
        this._createdTime = pc.readString();
        this.lastModifiedDate = pc.readString();
        this.lastModifiedTime = pc.readString();
        this.numofPicture = pc.readInt();
        this._list = (PictureList) pc.<PictureList> readParcelable(PictureList.class.getClassLoader());
        this.tagList = (TagList) pc.<TagList> readParcelable(TagList.class.getClassLoader());
        this.isFromFacebook = (pc.readInt() == 0) ? false : true;
    }

}
