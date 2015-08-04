package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by joseph on 2/7/2015.
 */
public class Tag implements Comparable<Tag>, Parcelable {
    private String tempID;
    private String id;
    private String name;
    private boolean isChecked;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tempID);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.isChecked ? 1 : 0);
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>(){
        public Tag createFromParcel(Parcel pc){
            return new Tag(pc);
        }
        public Tag[] newArray(int size){
            return new Tag[size];
        }
    };

    public Tag(String name) {
        this.tempID = "";
        this.id = "";
        this.name = name;
        this.isChecked = false;
    }

    public Tag(String tempID, String id, String name, boolean checked){
        this.tempID = tempID;
        this.id = id;
        this.name = name;
        this.isChecked = checked;
    }

    public void setTempID(String id) {
        this.tempID = id;
    }

    public void setID(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIsChecked(boolean checked){
        this.isChecked = checked;
    }

    public String getTempID() {
        return this.tempID;
    }

    public String getID() {
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIsChecked(){
        return this.isChecked;
    }

    private Tag(Parcel pc){
        this.tempID = pc.readString();
        this.id = pc.readString();
        this.name = pc.readString();
        this.isChecked = (pc.readInt() == 0) ? false : true;
    }


    @Override
    public int compareTo(Tag another) {
        int res = String.CASE_INSENSITIVE_ORDER.compare(this.getName(), another.getName());
        return (res != 0) ? res : this.getName().compareTo(another.getName());
    }
}
