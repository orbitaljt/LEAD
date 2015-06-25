package com.orbital.lead.model;

/**
 * Created by joseph on 24/6/2015.
 */
public class CurrentLoginUser{

    private static User mUser;

    public static void setUser(User user){
        mUser = user;
    }

    public static User getUser(){
        return mUser;
    }

}
