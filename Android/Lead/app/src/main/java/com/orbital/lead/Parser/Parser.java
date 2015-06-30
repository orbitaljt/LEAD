package com.orbital.lead.Parser;

import android.graphics.Bitmap;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumMessageType;
import com.orbital.lead.model.EnumPictureType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.Message;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;
import com.orbital.lead.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joseph on 14/6/2015.
 */
public class Parser {
    private static Parser mParser = new Parser();

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;

    private Parser(){}

    public static Parser getInstance(){
        mParser.initLogging();
        return mParser;
    }

    public Message parseJsonToMessage(String json){
        try{
            mLogging.debug(TAG, "parseJsonToMessage");
            JSONObject obj = new JSONObject(json);
            String code = obj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = obj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMsg = new Message(code, msg);
            return mMsg;

        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String parseUserIDFromJson(String json){
        try{
            mLogging.debug(TAG, "parseUserIDFromJson");
            JSONObject obj = new JSONObject(json);
            String id = obj.getString(Constant.MESSAGE_JSON_USER_ID_TAG);

            return id;
        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public User parseJsonToUser(String json){
        User mUser = null;
        try{
            mLogging.debug(TAG, "parseJsonToUser");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){

                JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

                mUser = new User(detailObj.getString(Constant.MESSAGE_JSON_FACEBOOK_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LEAD_USER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_TYPE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_FIRST_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_MIDDLE_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_lAST_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_BIRTHDAY_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_ADDRESS_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CITY_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_STATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_EMAIL_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_LOGIN_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_LOGIN_TIME_TAG),
                        this.convertStringToInteger(detailObj.getString(Constant.MESSAGE_JSON_AGE_TAG)));
            }

            return mUser;
        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public String parseJsonToProfilePictureLink(String json){

        try{
            mLogging.debug(TAG, "parseJsonToProfilePictureLink");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){
                String link = topObj.getString(Constant.MESSAGE_JSON_DETAIL_TAG);
                return link;
            }
            mLogging.debug(TAG, "parseJsonToProfilePictureLink no details found");

            return "";

        }catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public JournalList parseJsonToJournalList(String json){
        JournalList list = null;
        try {
            mLogging.debug(TAG, "parseJsonToJournalList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){
                list = new JournalList();

                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) {
                    JSONObject journalObj = detailArray.getJSONObject(i);

                    Journal mJournal = new Journal(journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_ALBUM_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CONTENT_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_IS_PUBLISHED_TAG));

                    list.addJournal(mJournal);

                }
            }

            return list;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Album parseJsonToAlbum(String json){
        Album mAlbum = null;
        PictureList pList = null;

        try {
            mLogging.debug(TAG, "parseJsonToJournalList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){

                JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);
                JSONArray listOfPictureArray = detailObj.getJSONArray(Constant.MESSAGE_JSON_LIST_OF_PICTURES_TAG);

                pList = new PictureList();

                for(int i=0; i < listOfPictureArray.length(); i++) {
                    JSONObject picObj = listOfPictureArray.getJSONObject(i);

                    Picture picElement = new Picture(picObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                                        picObj.getString(Constant.MESSAGE_JSON_PICTURE_ID_TAG),
                                        picObj.getString(Constant.MESSAGE_JSON_PICTURE_TYPE_TAG));
                    picElement.setTitle(picObj.getString(Constant.MESSAGE_JSON_TITLE_TAG));
                    picElement.setDescription(picObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG));
                    picElement.setHashTag(picObj.getString(Constant.MESSAGE_JSON_HASH_TAG_TAG));
                    picElement.setCreatedDate(picObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG));
                    picElement.setCreatedTime(picObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG));

                    pList.addPicture(picElement);
                }

                mAlbum = new Album(detailObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_ALBUM_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_HASH_TAG_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                        pList);

            }//end if

            return mAlbum;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String userObjectToJson(User user){
        JSONObject obj = new JSONObject();
        try{
            obj.put(Constant.MESSAGE_JSON_FACEBOOK_ID_TAG, user.getFacebookID());
            obj.put(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG, user.getProfilePictureID());
            obj.put(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG, user.getJournalListID());
            obj.put(Constant.MESSAGE_JSON_FIRST_NAME_TAG, user.getFirstName());
            obj.put(Constant.MESSAGE_JSON_MIDDLE_NAME_TAG, user.getMiddleName());
            obj.put(Constant.MESSAGE_JSON_lAST_NAME_TAG, user.getLastName());
            obj.put(Constant.MESSAGE_JSON_BIRTHDAY_TAG, user.getBirthday());
            obj.put(Constant.MESSAGE_JSON_AGE_TAG, user.getAge());
            obj.put(Constant.MESSAGE_JSON_ADDRESS_TAG, user.getAddress());
            obj.put(Constant.MESSAGE_JSON_CITY_TAG, user.getCity());
            obj.put(Constant.MESSAGE_JSON_STATE_TAG, user.getState());
            obj.put(Constant.MESSAGE_JSON_COUNTRY_TAG, user.getCountry());
            obj.put(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG, user.getCountryCode());
            obj.put(Constant.MESSAGE_JSON_EMAIL_TAG, user.getEmail());

            mLogging.debug(TAG, "user.getLastName() => " + user.getLastName());
            mLogging.debug(TAG, "user.getEmail() => " + user.getEmail());

            return obj.toString();

        } catch (JSONException e){
            mLogging.debug(TAG, "userObjectToJson error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }




    public String createPictureCoverUrl(String pictureCoverID, String pictureCoverType, String userID){
        return getSmallPictureUrl(pictureCoverID, pictureCoverType, userID);
    }

    public String createPictureThumbnailUrl(String pictureCoverID, String pictureCoverType, String userID){
        return getSmallPictureUrl(pictureCoverID, pictureCoverType, userID);
    }

    public String createPictureNormalUrl(String pictureCoverID, String pictureCoverType, String userID) {
        return getNormalPictureUrl(pictureCoverID, pictureCoverType, userID);
    }


    public EnumMessageType getMessageType(Message msg){
       return msg.getType();
    }

    public boolean isStringEmpty(String val){
        if(val.trim().equals("") || val.trim().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isBitmapEmpty(Bitmap bmp){
        if(bmp == null){
            return true;
        }
        return false;
    }

    public EnumPictureType getType(String value){
        if(value.toLowerCase().trim().equals("png")){
            return EnumPictureType.PNG;
        }else if(value.toLowerCase().trim().equals("jpeg")){
            return EnumPictureType.JPEG;
        }else if(value.toLowerCase().trim().equals("jpg")){
            return EnumPictureType.JPEG;
        }else{
            return EnumPictureType.NONE;
        }
    }

    public String generateFilename(String name, String ext){
        return name + "." + ext;
    }




    public int convertStringToInteger(String val){
        return Integer.parseInt(val);
    }

    public String convertIntegerToString(int val){
        return String.valueOf(val);
    }

    public boolean convertStringToBoolean(String val){
        if(val.equals("false") || val.equals("0") ){
            return false;
        }else if (val.equals("true") || val.equals("1")){
            return true;
        }else{
            return false;
        }
    }

    public String convertBooleanToString(boolean val){
        return String.valueOf(val);
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private String getSmallPictureUrl(String pictureID, String pictureCoverType, String userID){
        return Constant.URL_SMALL_PICTURE
                .replace(Constant.URL_DUMMY_USER_ID, userID)
                .replace(Constant.URL_DUMMY_FILE_NAME,
                        this.generateFilename(pictureID, pictureCoverType));
    }

    private String getNormalPictureUrl(String pictureID, String pictureCoverType, String userID) {
        return Constant.URL_NORMAL_PICTURE
                .replace(Constant.URL_DUMMY_USER_ID, userID)
                .replace(Constant.URL_DUMMY_FILE_NAME,
                        this.generateFilename(pictureID, pictureCoverType));
    }

}
