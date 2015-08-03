package com.orbital.lead.model;

/**
 * Created by joseph on 19/6/2015.
 */
public enum EnumFileType {
    IMAGE ("image"),
    DOCUMENT ("document");

    private final String ext;
    private EnumFileType(String s){
        ext = s;
    }

    public static EnumFileType fromString(String text) {
        if (text != null) {
            for (EnumFileType b : EnumFileType.values()) {
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
