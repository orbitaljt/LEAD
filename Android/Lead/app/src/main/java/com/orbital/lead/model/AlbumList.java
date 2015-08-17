package com.orbital.lead.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

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

    public void addAlbum(AlbumList list){
        if(this._arrayAlbum == null){
            this._arrayAlbum = new ArrayList<Album>(list.getList());
        }else{
            this._arrayAlbum.addAll(list.getList());
        }
    }

    public void removePicture(){

    }

    public ArrayList<Album> getList(){
        return this._arrayAlbum;
    }

    public Album get(int position) {
        if(this.getList() == null){
            return null;
        }

        return this.getList().get(position);
    }

    public Album get(String albumID) {
        if(this.getList() == null){
            return null;
        }

        Iterator<Album> iter = this.getList().iterator();
        while (iter.hasNext()) {
            Album a = iter.next();
            if(a.getAlbumID().equals(albumID)){
                return a;
            }
        }

        return null;

    }

    public int size() {
        if(this._arrayAlbum == null){
            return 0;
        }
        return this._arrayAlbum.size();
    }

    public void clearAll() {
        this._arrayAlbum = null;
        this.initArray();
    }

    private void initArray(){
        this._arrayAlbum = new ArrayList<Album>();
    }

    private AlbumList(Parcel pc){
       // pc.readTypedList(this._arrayAlbum, Picture.CREATOR);
        this._arrayAlbum = pc.createTypedArrayList(Album.CREATOR);
    }

}
