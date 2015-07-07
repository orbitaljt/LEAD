package com.orbital.lead.Parser;

/**
 * Created by joseph on 29/6/2015.
 */

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumPictureType;
import com.orbital.lead.model.FacebookUserObject;
import com.orbital.lead.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static AlbumList getFacebookAlbumList(String response) {
        AlbumList list = new AlbumList();

        try{
            JSONObject obj = new JSONObject(response);

            JSONObject albumObj = obj.getJSONObject(Constant.FACEBOOK_JSON_ALBUMS_TAG);
            JSONArray albumArray = albumObj.getJSONArray(Constant.FACEBOOK_JSON_DATA_TAG);

            for(int i=0; i< albumArray.length(); i++){
                Album album = getFacebookSpecificAlbum(albumArray.getJSONObject(i));

                if(album != null) {
                    list.addAlbum(album);
                }else{
                    mLogging.debug(TAG, "getFacebookAlbumList Album at " + i + " is null!!!");
                }
            }

            return list;

        } catch (JSONException e) {
            mLogging.debug(TAG, "getFacebookAlbumList error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Album getFacebookSpecificAlbum(JSONObject dataObj) {
        try{
            String albumCoverPictureType = EnumPictureType.JPEG.toString();

            String albumID = "";
            String albumCoverPictureID = "";
            String albumName = "";
            String createdTimeStamp = "";
            String updatedTimeStamp = "";
            String description = "";
            String createdDate = "";
            String createdTime = "";
            String updatedDate = "";
            String updatedTime = "";

            int numofPictures = 0;

            try{
                albumCoverPictureID = dataObj.getString(Constant.FACEBOOK_JSON_COVER_PHOTO_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookSpecificAlbum albumCoverPictureID error =>" + e.getMessage());
                albumCoverPictureID = "";
            }

            try{
                albumName = dataObj.getString(Constant.FACEBOOK_JSON_ALBUM_NAME_TAG);
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookSpecificAlbum albumName error =>" + e.getMessage());
                albumName = "";
            }

            try{
                description = dataObj.getString(Constant.FACEBOOK_JSON_DESCRIPTION_TAG); // some albums doesn't have description
            }catch (JSONException e){
                mLogging.debug(TAG, "getFacebookSpecificAlbum description error =>" + e.getMessage());
                description = "";
            }

            albumID = dataObj.getString(Constant.FACEBOOK_JSON_ID_TAG);
            createdTimeStamp = dataObj.getString(Constant.FACEBOOK_JSON_CREATED_TIME_TAG);
            updatedTimeStamp = dataObj.getString(Constant.FACEBOOK_JSON_UPDATED_TIME_TAG);
            numofPictures = dataObj.getInt(Constant.FACEBOOK_JSON_COUNT_TAG);

            createdDate = getDatabaseDateFromFacebookTimeStamp(createdTimeStamp);
            createdTime = getDatabaseTimeFromFacebookTimeStamp(createdTimeStamp);
            updatedDate = getDatabaseDateFromFacebookTimeStamp(updatedTimeStamp);
            updatedTime = getDatabaseTimeFromFacebookTimeStamp(updatedTimeStamp);

            Album newAlbum = new Album(CurrentLoginUser.getUser().getUserID(),
                    albumID,
                    albumCoverPictureID,
                    albumCoverPictureType,
                    albumName,
                    description,
                    createdDate,
                    createdTime,
                    updatedDate,
                    updatedTime,
                    null,
                    null,
                    true);

            newAlbum.setNumberOfPicture(numofPictures);

            return newAlbum;

        } catch (JSONException e) {
            mLogging.debug(TAG, "getFacebookAlbumList error => " + e.getMessage());
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

    public static String createFacebookPictureThumbnailUrl(String accessToken, String albumID){
        return getFacebookPictureCoverUrl(accessToken, albumID);
    }

    public static String createFacebookPictureNormalUrl(String accessToken, String albumID) {
        return getFacebookPictureCoverUrl(accessToken, albumID);
    }

    private static String getDatabaseDateFromFacebookTimeStamp(String timeStamp) {
        // facebook time format is yyyy-MM-dd'T'HH:mm:ssZ
        // return date in yyyy-MM-dd
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            DateFormat outputFormat = new SimpleDateFormat(FormatDate.DATABASE_FORMAT);

            Date parsedDate = dateFormat.parse(timeStamp);
            String outputText = outputFormat.format(parsedDate);

            //mLogging.debug(TAG, "getDateFromFacebookTimeStamp parsed Date => " + outputText);

            return outputText;

        }catch(Exception e){
            mLogging.debug(TAG, "getDateFromFacebookTimeStamp error => " + e.getMessage());
            return "";
        }
    }

    private static String getDatabaseTimeFromFacebookTimeStamp(String timeStamp) {
        // facebook time format is yyyy-MM-dd'T'HH:mm:ssZ
        // return date in yyyy-MM-dd
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");

            Date parsedTime = dateFormat.parse(timeStamp);
            String outputText = outputFormat.format(parsedTime);

            //mLogging.debug(TAG, "getDateFromFacebookTimeStamp parsed time => " + outputText);

            return outputText;

        }catch(Exception e){
            mLogging.debug(TAG, "getDateFromFacebookTimeStamp error => " + e.getMessage());
            return "";
        }
    }

    private static String getFacebookPictureCoverUrl(String accessToken, String albumID){
        return Constant.URL_FACEBOOK_ALBUM_PHOTO_COVER
                .replace(Constant.URL_DUMMY_ALBUM_ID, albumID)
                .replace(Constant.URL_DUMMY_ACCESS_TOKEN, accessToken);
    }



}
