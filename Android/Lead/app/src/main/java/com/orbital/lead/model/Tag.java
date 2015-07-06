package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joseph on 2/7/2015.
 */
public class Tag implements Parcelable {

    private String id;
    private String name;
    private boolean isChecked;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

    public Tag(String id, String name, boolean checked){
        this.id = id;
        this.name = name;
        this.isChecked = checked;
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
        this.id = pc.readString();
        this.name = pc.readString();
        this.isChecked = (pc.readInt() == 0) ? false : true;
    }


}
