package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joseph on 12/7/2015.
 */
public class TagMap implements Parcelable {

    private HashMap<String, Boolean> map;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(map.size()); // write the size first
        for(Map.Entry<String, Boolean> entry : map.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeInt(entry.getValue() ? 1 : 0);
        }
    }


    public static final Parcelable.Creator<TagMap> CREATOR = new Parcelable.Creator<TagMap>(){
        public TagMap createFromParcel(Parcel pc){
            return new TagMap(pc);
        }
        public TagMap[] newArray(int size){
            return new TagMap[size];
        }
    };

    public TagMap(){
        this.initTagMap();
    }

    public TagMap(TagList list) {
        this.initTagMap();
        for(Tag tag : list.getList()) {
            this.addTag(tag);
        }
    }

    public TagMap(HashMap<String, Boolean> map) {
        this.initTagMap(map);
    }

    public void addTagList(TagList list) {
        this.initTagMap();
        if(list != null){
            for(Tag tag : list.getList()) {
                this.addTag(tag);
            }
        }

    }

    public void addTag(Tag tag){
        if(this.map == null) {
            this.initTagMap();
        }
        this.setValue(tag.getName(), false);
    }

    public TagList getTagList() {
        TagList list = new TagList();
        for(Map.Entry<String, Boolean> entry : map.entrySet()){
            Tag tag = new Tag(entry.getKey());
            tag.setIsChecked(entry.getValue());
            list.addTag(tag);
        }
        return list;
    }

    public HashMap<String, Boolean> getMap() {
        return this.map;
    }

    public boolean isTagNameExist(String name) {
        return this.map.containsKey(name);
    }

    public void setValue(String key, boolean value){
        this.map.put(key, value);
    }

    private void initTagMap() {
        this.map = new HashMap<String, Boolean>();
    }

    private void initTagMap(HashMap<String, Boolean> map) {
        if(map == null){
           this.initTagMap();
        }else{
            this.map = new HashMap<String, Boolean>(map);
        }

    }

    private TagMap(Parcel pc){
        this.initTagMap();
        int size = pc.readInt();
        for(int i = 0; i < size; i++){
            String key = pc.readString();
            boolean value = (pc.readInt() == 0) ? false : true;
            map.put(key,value);
        }
    }


}
