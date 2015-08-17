package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by joseph on 20/6/2015.
 */
public class PictureList implements Parcelable {

    private ArrayList<Picture> _arrayPictures;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(_arrayPictures);
    }

    public static final Parcelable.Creator<PictureList> CREATOR = new Parcelable.Creator<PictureList>(){
        public PictureList createFromParcel(Parcel pc){
            return new PictureList(pc);
        }
        public PictureList[] newArray(int size){
            return new PictureList[size];
        }
    };

    public PictureList(){
        this.initArray();
    }

    public void addPicture(Picture pic){
        if(this._arrayPictures == null){
            this.initArray();
        }
        this._arrayPictures.add(pic);
    }

    public void addPicture(PictureList list){
        if(this._arrayPictures == null){
            this._arrayPictures = new ArrayList<Picture>(list.getList());
        }else{
            this._arrayPictures.addAll(list.getList());
        }
    }

    public void overrideList(PictureList list) {
        if(this._arrayPictures == null){
            this._arrayPictures = new ArrayList<Picture>(list.getList());
        }else{
            this._arrayPictures.clear();
            this._arrayPictures.addAll(list.getList());
        }
    }


    public ArrayList<Picture> getList(){
        return this._arrayPictures;
    }

    public int size() {
        if(this._arrayPictures == null){
            return 0;
        }
        return this._arrayPictures.size();
    }

    public void clearAll() {
        this._arrayPictures = null;
        this.initArray();
    }

    private void initArray(){
        this._arrayPictures = new ArrayList<Picture>();
    }

    private PictureList(Parcel pc){
       // pc.readTypedList(this._arrayPictures, Picture.CREATOR);
        this._arrayPictures = pc.createTypedArrayList(Picture.CREATOR);
    }

}
