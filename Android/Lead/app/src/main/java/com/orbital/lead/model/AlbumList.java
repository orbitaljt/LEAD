package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by joseph on 20/6/2015.
 */
public class AlbumList implements Parcelable {

    private ArrayList<Album> _arrayAlbum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(_arrayAlbum);
    }

    public static final Creator<AlbumList> CREATOR = new Creator<AlbumList>(){
        public AlbumList createFromParcel(Parcel pc){
            return new AlbumList(pc);
        }
        public AlbumList[] newArray(int size){
            return new AlbumList[size];
        }
    };

    public AlbumList(){
        this.initArray();
    }

    public void addAlbum(Album album){
        if(this._arrayAlbum == null){
            this.initArray();
        }
        this._arrayAlbum.add(album);
    }

    public void removePicture(){

    }

    public ArrayList<Album> getList(){
        return this._arrayAlbum;
    }

    public Album getAlbum(int position) {
        if(this.getList() == null){
            return null;
        }

        return this.getList().get(position);
    }

    public int size() {
        if(this._arrayAlbum == null){
            return 0;
        }
        return this._arrayAlbum.size();
    }

    private void initArray(){
        this._arrayAlbum = new ArrayList<Album>();
    }

    private AlbumList(Parcel pc){
       // pc.readTypedList(this._arrayAlbum, Picture.CREATOR);
        this._arrayAlbum = pc.createTypedArrayList(Album.CREATOR);
    }

}
