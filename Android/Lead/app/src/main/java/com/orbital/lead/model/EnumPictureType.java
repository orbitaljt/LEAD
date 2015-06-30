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

    public static EnumPictureType fromString(String text) {
        if (text != null) {
            for (EnumPictureType b : EnumPictureType.values()) {
                if (text.equalsIgnoreCase(b.ext)) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return ext;
    }


}
