package com.orbital.lead.model;

/**
 * Created by joseph on 19/6/2015.
 */
public enum EnumOpenPictureActivityType {
    OPEN_FRAGMENT_LIST_PICTURES ("openFragmentListPictures"), // fragment that shows a list of all albums
    OPEN_FRAGMENT_ALBUM ("openFragmentAlbum"), // fragment that shows all pictures of an album
    SELECT_FACEBOOK_ALBUM ("selectFacebookAlbum"),// open FragmentAlbum that shows a list all facebook albums, for selecting purpose
    SELECT_FACEBOOK_PICTURE ("selectFacebookPicture"); // open FragmentPictures, allow user to select a picture.

    private String text;

    EnumOpenPictureActivityType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static EnumOpenPictureActivityType fromString(String text) {
        if (text != null) {
            for (EnumOpenPictureActivityType b : EnumOpenPictureActivityType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }


}
