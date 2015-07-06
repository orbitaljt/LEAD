package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by joseph on 30/6/2015.
 */
public class TagList implements Parcelable{
    private ArrayList<Tag> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public static final Parcelable.Creator<TagList> CREATOR = new Parcelable.Creator<TagList>(){
        public TagList createFromParcel(Parcel pc){
            return new TagList(pc);
        }
        public TagList[] newArray(int size){
            return new TagList[size];
        }
    };


    public TagList(){
        this.list = new ArrayList<Tag>();
    }

    public void addTag(Tag newTag){
        if(this.list != null){
            this.list.add(newTag);
        }
    }

    public void removeTag(String name){
        Iterator<Tag> iter = this.list.iterator();
        while (iter.hasNext()) {
            Tag j = iter.next();
            if(j.getName().equals(name)){
                iter.remove();
            }
        }
    }

    public int size(){
        if(this.list != null){
            return this.list.size();
        }
        return 0;
    }

    public ArrayList<Tag> getList(){
        return this.list;
    }

    public void setList(ArrayList<Tag> newList){
        this.list = new ArrayList<Tag>(newList);
        newList = null;
    }

    public String toString() {
        if(this.isEmpty()) { return ""; }
        if(this.size() == 1) { return this.list.get(1).getName().toString(); }

        String str = "";
        for(int i=0; i<= this.size() - 2; i++){
            str += this.list.get(i).getName() + ", "; //first to second last add with comma
        }

        str += this.list.get(this.size() - 1).getName(); //add the last element without comma
        return str;
    }

    private boolean isEmpty(){
        return this.size() == 0;
    }

    private TagList(Parcel pc){
        this.list = pc.createTypedArrayList(Tag.CREATOR);
    }

}
