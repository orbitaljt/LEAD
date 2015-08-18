package com.orbital.lead.model;

/**
 * Created by joseph on 19/6/2015.
 */
public enum EnumPictureServiceType {
    GET_SPECIFIC_ALBUM ("get_specific_album"),
    GET_ALL_ALBUM ("get_all_album"),
    DELETE_ALBUM("delete_album"),
    UPLOAD_PROFILE_IMAGE_URL ("upload_profile_image_url"),
    UPLOAD_IMAGE_FILE ("upload_image_file"),
    UPLOAD_FACEBOOK_IMAGE ("upload_facebook_image"),
    SET_ALBUM_COVER ("set_album_cover");

    private String text;

    EnumPictureServiceType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static EnumPictureServiceType fromString(String text) {
        if (text != null) {
            for (EnumPictureServiceType b : EnumPictureServiceType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }



}
