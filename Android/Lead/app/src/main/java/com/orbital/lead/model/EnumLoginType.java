package com.orbital.lead.model;

/**
 * Created by joseph on 29/6/2015.
 */
public enum EnumLoginType {
    LOGIN_LEAD ("login_lead"),
    LOGIN_FACEBOOK ("login_facebook");

    private String text;

    EnumLoginType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static EnumLoginType fromString(String text) {
        if (text != null) {
            for (EnumLoginType b : EnumLoginType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

}
