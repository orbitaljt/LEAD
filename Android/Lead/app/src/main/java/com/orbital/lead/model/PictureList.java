package com.orbital.lead.model;

import java.util.ArrayList;

/**
 * Created by joseph on 20/6/2015.
 */
public class PictureList {

    private ArrayList<Picture> _arrayPictures;


    public PictureList(){
        this.initArray();
    }

    public void addPicture(Picture pic){
        if(this._arrayPictures == null){
            this.initArray();
        }
        this._arrayPictures.add(pic);
    }

    public void removePicture(){

    }

    public ArrayList<Picture> getList(){
        return this._arrayPictures;
    }

    private void initArray(){
        this._arrayPictures = new ArrayList<Picture>();
    }

}
