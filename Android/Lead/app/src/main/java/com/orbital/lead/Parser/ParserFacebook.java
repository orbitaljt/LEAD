package com.orbital.lead.Parser;

/**
 * Created by joseph on 29/6/2015.
 */

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumPictureType;
import com.orbital.lead.model.FacebookUserObject;
import com.orbital.lead.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class will parse all facebook responses
 * All response usually in JSON format
 */

public class ParserFacebook {
    private static final String TAG = "ParserFacebook";
    private static CustomLogging mLogging = CustomLogging.getInstance();

    public static String getFacebookID(String response){
        try{
            //mLogging.debug(TAG, "getFacebookID");
            JSONObject obj = new JSONObject(response);
            String id = obj.getString(Constant.FACEBOOK_JSON_ID_TAG);

            mLogging.debug(TAG, "getFacebookID => " + id);

            return id;

        }catch (JSONException e){
            mLogging.debug(TAG, "getFacebookID error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public static FacebookUserObject getFacebookUserObject(String response){
        try{
            FacebookUserObject fbObject = null;

            //mLogging.debug(TAG, "getFacebookID");
            JSONObject obj = new JSONObject(response);

            String facebookID = obj.getString(Constant.FACEBOOK_JSON_ID_TAG);
            String birthday = "";
            String email = "";
            String firstName = "";
            String middleName = "";
            String lastName = "";
            String rawUrl = "";
            String locationID = "";

            try{
                birthday = obj.getString(Constant.FACEBOOK_JSON_BIRTHDAY_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get birthday Error =>" + e.getMessage());
            }

            try{
                email = obj.getString(Constant.FACEBOOK_JSON_EMAIL_TAG);
                //mLogging.debug(TAG, "getFacebookUserObject email =>" + email);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get email Error =>" + e.getMessage());
            }

            try{
                firstName = obj.getString(Constant.FACEBOOK_JSON_FIRST_NAME_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get fisrt name Error =>" + e.getMessage());
            }

            try{
                middleName = obj.getString(Constant.FACEBOOK_JSON_MIDDLE_NAME_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get middle name Error =>" + e.getMessage());
            }

            try{
                lastName = obj.getString(Constant.FACEBOOK_JSON_LAST_NAME_TAG);
               // mLogging.debug(TAG, "getFacebookUserObject lastName =>" + lastName);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get last name Error =>" + e.getMessage());
            }
            try{
                rawUrl = obj.getJSONObject(Constant.FACEBOOK_JSON_PROFILE_PICTURE_TAG)
                        .getJSONObject(Constant.FACEBOOK_JSON_DATA_TAG)
                        .getString(Constant.FACEBOOK_JSON_URL_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get raw picture URl Error =>" + e.getMessage());
            }

            try{
                locationID = obj.getJSONObject(Constant.FACEBOOK_JSON_LOCATION_TAG)
                        .getString(Constant.FACEBOOK_JSON_ID_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookUserObject Get location ID Error =>" + e.getMessage());
            }

            mLogging.debug(TAG, "getFacebookUserObject rawUrl => " + rawUrl);
            //mLogging.debug(TAG, "getFacebookUserObject email => " + email);
            //mLogging.debug(TAG, "getFacebookUserObject lastName => " + lastName);
            //mLogging.debug(TAG, "getFacebookUserObject middleName => " + middleName);

            fbObject = new FacebookUserObject(rawUrl, facebookID, birthday,
                                            email, locationID,
                                            firstName, middleName, lastName);

            return fbObject;

        }catch (JSONException e){
            mLogging.debug(TAG, "getFacebookUserObject getFacebookID error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String getFacebookImageID(String rawUrl){
        String id = getFacebookImageName(rawUrl);
        id = id.substring(0, id.indexOf("."));
        return id;
        //raw url -> https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xpf1\/v\/t1.0-1\/p200x200\/11026207_1422389354755121_5271937348746703183_n.jpg?oh=0865d519eee0521fbdd4d09e86384e5a&oe=561789D5&__gda__=1445000121_796151e5474ceeed5de9ee2f72a26ae7
    }

    public static EnumPictureType getPictureType(String rawUrl){
        String imageName = getFacebookImageName(rawUrl);
        String[] str = imageName.split("\\.");
        if(str.length == 2){
            String ext = str[1].toLowerCase().trim();

            return EnumPictureType.fromString(ext);
        }
        return null;
    }

    public static String getFacebookImageName(String rawUrl){
        int indexOfQuestionMark = rawUrl.indexOf("?");
        int indexOfLastSlash = rawUrl.lastIndexOf("/");
        String fileName = rawUrl.substring(0,indexOfQuestionMark);
        fileName = fileName.substring(indexOfLastSlash + 1);
        //fileName = fileName.replace("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p200x200/", "");
        return fileName;
    }


}
