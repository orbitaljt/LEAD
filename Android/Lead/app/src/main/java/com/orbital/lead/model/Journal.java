package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orbital.lead.Parser.Parser;

/**
 * Created by joseph on 22/6/2015.
 */
public class Journal implements Parcelable {
    // each journal has an album
    private Parser mParser;
    private Album album;
    private Project project;
    private TagList tagList;
    private String journalID;
    private String pictureCoverID;
    private EnumPictureType pictureCoverType;
    private String albumID;
    private String title;
    private String content;
    private String countryCode;
    private String journalDate;
    private String journalTime;
    private String lastModifiedDate;
    private String lastModifiedTime;
    private String createdDate;
    private String createdTime;
    private boolean isPublished;
    private boolean hasDetail;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.journalID);
        dest.writeString(this.pictureCoverID);
        dest.writeString((this.pictureCoverType == null) ? "" : this.pictureCoverType.name());
        dest.writeString(this.albumID);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.countryCode);
        dest.writeString(this.journalDate);
        dest.writeString(this.journalTime);
        dest.writeString(this.lastModifiedDate);
        dest.writeString(this.lastModifiedTime);
        dest.writeString(this.createdDate);
        dest.writeString(this.createdTime);
        dest.writeInt(this.isPublished ? 1 : 0);
        dest.writeParcelable(this.album, flags);
        dest.writeParcelable(this.project, flags);
        dest.writeParcelable(this.tagList, flags);
    }

    public static final Parcelable.Creator<Journal> CREATOR = new Parcelable.Creator<Journal>(){
        public Journal createFromParcel(Parcel pc){
            return new Journal(pc);
        }
        public Journal[] newArray(int size){
            return new Journal[size];
        }
    };

    public Journal() {}

    public Journal(String jID, String picCoverID, String picCoverType, String albumID,
                   String title, String content, String cc, String jDate, String jTime, String lastModDate, String lastModTime,
                   String createdDate, String createdTime, String isPub){
        this.initParser();

        this.journalID = jID;
        this.pictureCoverID = picCoverID;
        this.pictureCoverType = this.getParser().getPictureType(picCoverType);
        this.albumID = albumID;
        this.title = title;
        this.content = content;
        this.countryCode = cc;
        this.journalDate = jDate;
        this.journalTime = jTime;
        this.lastModifiedDate = lastModDate;
        this.lastModifiedTime = lastModTime;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.isPublished = this.getParser().convertStringToBoolean(isPub);
    }

    public Journal(Journal j) {
        this.journalID = j.getJournalID();
        this.pictureCoverID = j.getPictureCoverID();
        this.pictureCoverType = j.getPictureCoverType();
        this.albumID = j.getAlbumID();
        this.title = j.getTitle();
        this.content = j.getContent();
        this.countryCode = j.getCountryCode();
        this.journalDate = j.getJournalDate();
        this.journalTime = j.getJournalTime();
        this.lastModifiedDate = j.getLastModifiedDate();
        this.lastModifiedTime = j.getLastModifiedTime();
        this.createdDate = j.getCreatedDate();
        this.createdTime = j.getCreatedTime();
        this.isPublished = j.getIsPublished();
        this.album = j.getAlbum();
        this.project = j.getProject();
        this.tagList = j.getTagList();

    }

    public Album getAlbum(){
        return this.album;
    }

    public Project getProject() {
        if(this.project == null) {
            this.project = new Project();
        }
        return this.project;
    }

    public TagList getTagList() {
        if(this.tagList == null){
            this.tagList = new TagList();
        }
        return this.tagList;
    }

    public String getJournalID(){
        return this.journalID;
    }

    public String getPictureCoverID(){
        return this.pictureCoverID;
    }

    public EnumPictureType getPictureCoverType() {
        return this.pictureCoverType;
    }

    public String getAlbumID(){
        return this.albumID;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }

    public String getCountryCode(){
        return this.countryCode;
    }

    public String getJournalDate() {
        return this.journalDate;
    }

    public String getJournalTime(){
        return this.journalTime;
    }

    public String getLastModifiedDate(){
        return this.lastModifiedDate;
    }

    public String getLastModifiedTime(){
        return this.lastModifiedTime;
    }

    public String getCreatedDate(){
        return this.createdDate;
    }

    public String getCreatedTime(){
        return this.createdTime;
    }

    public boolean getIsPublished(){
        return this.isPublished;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTagList(TagList list) {
        if(this.tagList == null){
            this.tagList = new TagList();
        }
        this.tagList.setList(list.getList());
    }

    public void setJournalID(String val){
        this.journalID = val;
    }

    public void setPictureCoverID(String val){
        this.pictureCoverID = val;
    }

    public void setAlbumID(String val){
        this.albumID = val;
    }

    public void setTitle(String val){
        this.title = val;
    }

    public void setContent(String val){
        this.content = val;
    }

    public void setCountryCode(String val){
        this.countryCode = val;
    }

    public void setJournalDate(String val) {
        this.journalDate = val;
    }

    public void setJournalTime(String val) {
        this.journalTime = val;
    }

    public void setLastModifiedDate(String val){
        this.lastModifiedDate = val;
    }

    public void setLastModifiedTime(String val){
        this.lastModifiedTime = val;
    }

    public void setCreatedDate(String val){
        this.createdDate = val;
    }

    public void setCreatedTime(String val){
        this.createdTime = val;
    }

    public void setIsPublished(boolean val){
        this.isPublished = val;
    }

    private void initParser(){
        this.mParser = Parser.getInstance();
    }

    private Parser getParser(){
        return this.mParser;
    }

    private Journal(Parcel pc){
        this.journalID = pc.readString();
        this.pictureCoverID = pc.readString();
        try {
            pictureCoverType = EnumPictureType.valueOf(pc.readString());
        } catch (IllegalArgumentException x) {
            pictureCoverType = null;
        }
        this.albumID = pc.readString();
        this.title = pc.readString();
        this.content = pc.readString();
        this.countryCode = pc.readString();
        this.journalDate = pc.readString();
        this.journalTime = pc.readString();
        this.lastModifiedDate = pc.readString();
        this.lastModifiedTime = pc.readString();
        this.createdDate = pc.readString();
        this.createdTime = pc.readString();
        this.isPublished = (pc.readInt() == 0) ? false : true;
        this.album = (Album) pc.<Album> readParcelable(Album.class.getClassLoader());
        this.project = (Project) pc.<Project> readParcelable(Project.class.getClassLoader());
        this.tagList = (TagList) pc.<TagList> readParcelable(TagList.class.getClassLoader());
    }

}
