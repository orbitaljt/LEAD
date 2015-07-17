package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joseph on 2/7/2015.
 */
public class Project implements Parcelable {

    private String projectJournalRelationID = "";
    private String projectID = "";
    private String createdByUserID = "";
    private String name = "";
    private String content = "";
    private String country = "";
    private String countryCode = "";
    private String lastModifiedDate = "";
    private String lastModifiedTime = "";
    private String createdDate = "";
    private String createdTime = "";
    private String projectStartDate = "";
    private String projectStartTime = "";
    private String projectEndDate = "";
    private String projectEndTime = "";
    private String projectYear = "";
    private boolean isSelected = false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.projectJournalRelationID);
        dest.writeString(this.projectID);
        dest.writeString(this.createdByUserID);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.country);
        dest.writeString(this.countryCode);
        dest.writeString(this.lastModifiedDate);
        dest.writeString(this.lastModifiedTime);
        dest.writeString(this.createdDate);
        dest.writeString(this.createdTime);
        dest.writeString(this.projectStartDate);
        dest.writeString(this.projectStartTime);
        dest.writeString(this.projectEndDate);
        dest.writeString(this.projectEndTime);
        dest.writeString(this.projectYear);
        dest.writeInt(this.isSelected ? 1 : 0);
    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>(){
        public Project createFromParcel(Parcel pc){
            return new Project(pc);
        }
        public Project[] newArray(int size){
            return new Project[size];
        }
    };

    public Project() {
    }

    public Project(String id, String userID, String name, String content, String country,
                   String countryCode, String lastModifiedDate, String lastModifiedTime,
                   String createdDate, String createdTime, String projectStartDate,String projectStartTime,
                   String projectEndDate, String projectEndTime, String projectYear) {

        this.projectID = id;
        this.createdByUserID = userID;
        this.name = name;
        this.content = content;
        this.country = country;
        this.countryCode = countryCode;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedTime = lastModifiedTime;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.projectStartDate = projectStartDate;
        this.projectStartTime = projectStartTime;
        this.projectEndDate = projectEndDate;
        this.projectEndTime = projectEndTime;
        this.projectYear = projectYear;
        this.isSelected = false;
    }

    public Project(Project project){
        this.projectID = project.getProjectID();
        this.createdByUserID = project.getCreatedByUserID();
        this.name = project.getName();
        this.content = project.getContent();
        this.country = project.getCountry();
        this.countryCode = project.getCountryCode();
        this.lastModifiedDate = project.getLastModifiedDate();
        this.lastModifiedTime = project.getLastModifiedTime();
        this.createdDate = project.getCreatedDate();
        this.createdTime = project.getCreatedTime();
        this.projectStartDate = project.getProjectStartDate();
        this.projectStartTime = project.getProjectStartTime();
        this.projectEndDate = project.getProjectEndDate();
        this.projectEndTime = project.getProjectEndTime();
        this.projectYear = project.getProjectYear();
        this.isSelected = project.getIsSelected();
    }

    public String getProjectJournalRelationID() {
        return this.projectJournalRelationID;
    }

    public String getProjectID() {
        return this.projectID;
    }

    public String getCreatedByUserID() {
        return this.createdByUserID;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public String getCountry(){
        return this.country;
    }

    public String getCountryCode(){
        return this.countryCode;
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

    public String getProjectStartDate() {
        return this.projectStartDate;
    }

    public String getProjectStartTime(){
        return this.projectStartTime;
    }

    public String getProjectEndDate() {
        return this.projectEndDate;
    }

    public String getProjectEndTime(){
        return this.projectEndTime;
    }

    public String getProjectYear() {
        return this.projectYear;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setCreatedByUserID(String val) {
        this.createdByUserID = val;
    }

    public void setProjectJournalRelationID(String val) {
        this.projectJournalRelationID = val;
    }

    public void setProjectID(String val) {
        this.projectID = val;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String val){
        this.content = val;
    }

    public void setCountryCode(String val){
        this.countryCode = val;
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

    public void setProjectStartDate(String val) {
        this.projectStartDate = val;
    }

    public void setProjectStartTime(String val) {
        this.projectStartTime = val;
    }

    public void setProjectEndDate(String val) {
        this.projectEndDate = val;
    }

    public void setProjectEndTime(String val) {
        this.projectEndTime = val;
    }

    public void setProjectYear(String val) {
        this.projectYear = val;
    }

    public void setIsSelected(boolean selected) {
        this.isSelected = selected;
    }

    private Project(Parcel pc){
        this.projectJournalRelationID = pc.readString();
        this.projectID = pc.readString();
        this.createdByUserID =  pc.readString();
        this.name =  pc.readString();
        this.content =  pc.readString();
        this.country =  pc.readString();
        this.countryCode =  pc.readString();
        this.lastModifiedDate =  pc.readString();
        this.lastModifiedTime =  pc.readString();
        this.createdDate =  pc.readString();
        this.createdTime =  pc.readString();
        this.projectStartDate =  pc.readString();
        this.projectStartTime =  pc.readString();
        this.projectEndDate =  pc.readString();
        this.projectEndTime =  pc.readString();
        this.projectYear =  pc.readString();
        this.isSelected = (pc.readInt() == 0) ? false : true;;
    }

}
