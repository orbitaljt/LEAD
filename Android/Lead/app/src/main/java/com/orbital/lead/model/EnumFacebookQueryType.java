package com.orbital.lead.model;

/**
 * Created by joseph on 2/7/2015.
 */
public enum EnumFacebookQueryType {
    GET_ALL_ALBUM ("get_all_album"),
    GET_ALL_ALBUM_PICTURES("get_all_album_pictures");

    private final String ext;
    private EnumFacebookQueryType(String s){
        ext = s;
    }

    public static EnumFacebookQueryType fromString(String text) {
        if (text != null) {
            for (EnumFacebookQueryType b : EnumFacebookQueryType.values()) {
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
