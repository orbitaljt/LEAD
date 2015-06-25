package com.orbital.lead.model;

/**
 * Created by joseph on 21/6/2015.
 */
public enum EnumPictureType {
    PNG ("png"),
    JPEG ("jpg"),
    NONE ("");

    private final String ext;
    private EnumPictureType(String s){
        ext = s;
    }

    @Override
    public String toString(){
        return ext;
    }


}
