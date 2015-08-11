package com.orbital.lead.Parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.Country;
import com.orbital.lead.model.CountryList;
import com.orbital.lead.model.EnumMessageType;
import com.orbital.lead.model.EnumPictureType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.model.Message;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;
import com.orbital.lead.model.TagSet;
import com.orbital.lead.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

                //detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_LIST_ID_TAG),
                //  detailObj.getString(Constant.MESSAGE_JSON_CITY_TAG),
               // detailObj.getString(Constant.MESSAGE_JSON_STATE_TAG),
                mUser = new User(detailObj.getString(Constant.MESSAGE_JSON_FACEBOOK_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LEAD_USER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_PROFILE_TYPE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_FIRST_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_MIDDLE_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_lAST_NAME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_BIRTHDAY_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_ADDRESS_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_EMAIL_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CONTACT_TAG),
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
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS || this.getMessageType(mMessage) == EnumMessageType.HAS_RECORD){
                list = new JournalList();

                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) {
                    JSONObject journalObj = detailArray.getJSONObject(i);

                    JSONArray listOfTagArray = journalObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);
                    TagList tagList = parseJsonToTagList(listOfTagArray);

                    Journal mJournal = new Journal(journalObj.getString(Constant.MESSAGE_JSON_JOURNAL_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                            journalObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG),
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

                    JSONObject projectObj = journalObj.getJSONObject(Constant.MESSAGE_JSON_PROJECT_TAG);
                    String projectJournalRelationID = projectObj.getString(Constant.MESSAGE_JSON_PROJECT_JOURNAL_RELATION_ID_TAG);
                    String projectID = projectObj.getString(Constant.MESSAGE_JSON_PROJECT_ID_TAG);

                    Project project = new Project();
                    project.setProjectJournalRelationID(projectJournalRelationID);
                    project.setProjectID(projectID);

                    mJournal.setTagList(tagList);
                    mJournal.setProject(project);

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

    public Journal parseJsonToSpecificJournal(String json) {
        Journal journal = null;
        Album album = null;
        TagList albumTagList = null;
        Project project = null;

        try {
            mLogging.debug(TAG, "parseJsonToSpecificJournal");
            JSONObject topObj = new JSONObject(json);

            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS || this.getMessageType(mMessage) == EnumMessageType.HAS_RECORD) {

                JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

                journal = new Journal(detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CONTENT_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_IS_PUBLISHED_TAG));

                // Album Tags
                JSONArray tagArray = detailObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);
                albumTagList = parseJsonToTagList(tagArray);

                // Project
                JSONObject projectObj = detailObj.getJSONObject(Constant.MESSAGE_JSON_PROJECT_TAG);
                String projectJournalRelationID = projectObj.getString(Constant.MESSAGE_JSON_PROJECT_JOURNAL_RELATION_ID_TAG);
                String projectID = projectObj.getString(Constant.MESSAGE_JSON_PROJECT_ID_TAG);
                project = new Project();
                project.setProjectJournalRelationID(projectJournalRelationID);
                project.setProjectID(projectID);

                // Album and its objects (include list of pictures and album tags)
                JSONObject albumObj = detailObj.getJSONObject(Constant.MESSAGE_JSON_ALBUM_TAG);
                album = this.parseJsonAlbumObject(albumObj);


                journal.setTagList(albumTagList);
                journal.setAlbum(album);
                journal.setProject(project);

            }

            return journal;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public ProjectList parseJsonToProjectList(String json){
        ProjectList list = null;
        try {
            mLogging.debug(TAG, "parseJsonToProjectList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){
                list = new ProjectList();

                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) {
                    JSONObject projectObj = detailArray.getJSONObject(i);

                    Project mProject = new Project(projectObj.getString(Constant.MESSAGE_JSON_PROJECT_ID_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_NAME_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_CONTENT_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_COUNTRY_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_PROJECT_START_DATE_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_PROJECT_START_TIME_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_PROJECT_END_DATE_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_PROJECT_END_TIME_TAG),
                            projectObj.getString(Constant.MESSAGE_JSON_PROJECT_YEAR_TAG));


                    list.addProject(mProject);

                }
            }

            return list;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public CountryList parseJsonToCountryList(String json){
        CountryList list = null;
        try {
            mLogging.debug(TAG, "parseJsonToCountryList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){
                list = new CountryList();

                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) {
                    JSONObject countryObj = detailArray.getJSONObject(i);

                    Country mCountry = new Country(countryObj.getString(Constant.MESSAGE_JSON_COUNTRY_TAG),
                            countryObj.getString(Constant.MESSAGE_JSON_REGION_TAG),
                            countryObj.getString(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG));


                    list.addCountry(mCountry);

                }
            }

            return list;

        } catch (JSONException e){
            mLogging.debug(TAG, "error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }




    public Album parseJsonToSpecificAlbum(String json){
        Album mAlbum = null;
        PictureList pList = null;
        TagList tagList = null;

        try {
            mLogging.debug(TAG, "parseJsonToSpecificAlbum");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if(this.getMessageType(mMessage) == EnumMessageType.SUCCESS){

                JSONObject detailObj = topObj.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

                mAlbum = this.parseJsonAlbumObject(detailObj);
                /*
                JSONArray listOfPictureArray = detailObj.getJSONArray(Constant.MESSAGE_JSON_LIST_OF_PICTURES_TAG);
                JSONArray listOfTagArray = detailObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);

                tagList = parseJsonToTagList(listOfTagArray);
                pList = this.parseJsonToPictureList(listOfPictureArray);

                mAlbum = new Album(detailObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                        detailObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                        pList,
                        tagList,
                        false);
                */

            }//end if

            return mAlbum;

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToSpecificAlbum error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Album parseJsonAlbumObject(JSONObject albumObj) throws JSONException {
        Album album = null;
        PictureList pList = null;
        TagList tagList = null;

        JSONArray listOfPictureArray = albumObj.getJSONArray(Constant.MESSAGE_JSON_LIST_OF_PICTURES_TAG);
        JSONArray listOfTagArray = albumObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);

        tagList = parseJsonToTagList(listOfTagArray);
        pList = this.parseJsonToPictureList(listOfPictureArray);

        album = new Album(albumObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                albumObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                pList,
                tagList,
                false);

        return album;
    }


    public AlbumList parseJsonToAlbumList(String json){
        AlbumList mAlbumList = new AlbumList();
        Album mAlbum = null;
        PictureList pList = null;
        TagList albumTagList = null;

        try {
            mLogging.debug(TAG, "parseJsonToAlbumList");
            JSONObject topObj = new JSONObject(json);
            String code = topObj.getString(Constant.MESSAGE_JSON_CODE_TAG);
            String msg = topObj.getString(Constant.MESSAGE_JSON_MESSAGE_TAG);

            Message mMessage = new Message(code, msg);
            if (this.getMessageType(mMessage) == EnumMessageType.SUCCESS) {
                JSONArray detailArray = topObj.getJSONArray(Constant.MESSAGE_JSON_DETAIL_TAG);

                for(int i=0; i < detailArray.length(); i++) { // each detail element is 1 album object
                    // each album
                    // array of pictures
                    // array of tags
                    JSONObject albumObj = detailArray.getJSONObject(i);


                    JSONArray listOfPictureArray = albumObj.getJSONArray(Constant.MESSAGE_JSON_LIST_OF_PICTURES_TAG);
                    JSONArray listOfTagArray = albumObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);

                    albumTagList = parseJsonToTagList(listOfTagArray);
                    pList = this.parseJsonToPictureList(listOfPictureArray);

                    mAlbum = new Album(albumObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_ID_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_PICTURE_COVER_TYPE_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_TITLE_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_DATE_TAG),
                            albumObj.getString(Constant.MESSAGE_JSON_LAST_MODIFIED_TIME_TAG),
                            pList,
                            albumTagList,
                            false);

                    mAlbumList.addAlbum(mAlbum);
                }// end for

                return mAlbumList;
            }//end if

            return null;

        }catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToAlbumList error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public PictureList parseJsonToPictureList(JSONArray listOfPictureArray){
        PictureList pList = new PictureList();
        try {
            for(int i=0; i < listOfPictureArray.length(); i++) {
                JSONObject picObj = listOfPictureArray.getJSONObject(i);
                JSONArray picTagArray = picObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);

                TagList pictureTagList = parseJsonToTagList(picTagArray); // each picture has an array of tags

                Picture picElement = new Picture(picObj.getString(Constant.MESSAGE_JSON_USER_ID_TAG),
                                            picObj.getString(Constant.MESSAGE_JSON_PICTURE_ID_TAG),
                                            picObj.getString(Constant.MESSAGE_JSON_PICTURE_TYPE_TAG));
                picElement.setTitle(picObj.getString(Constant.MESSAGE_JSON_TITLE_TAG));
                picElement.setDescription(picObj.getString(Constant.MESSAGE_JSON_DESCRIPTION_TAG));
                picElement.setCreatedDate(picObj.getString(Constant.MESSAGE_JSON_CREATED_DATE_TAG));
                picElement.setCreatedTime(picObj.getString(Constant.MESSAGE_JSON_CREATED_TIME_TAG));
                picElement.setTagList(pictureTagList);

                pList.addPicture(picElement);
            }

            return pList;

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToTagList error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public TagList parseJsonToTagList(JSONArray tagArray){

        if(tagArray == null) {
            mLogging.debug(TAG, "parseJsonToTagList tagArray is null");
            return null;
        }

        TagList tagList = new TagList();
        try {
            for(int i=0; i < tagArray.length(); i++) {
                JSONObject tagObj = tagArray.getJSONObject(i);
                String id  = tagObj.getString(Constant.MESSAGE_JSON_ID_TAG);
                String tagID = tagObj.getString(Constant.MESSAGE_JSON_TAG_ID_TAG);
                String tagName = tagObj.getString(Constant.MESSAGE_JSON_TAG_NAME_TAG);
                Tag tag = new Tag(id, tagID, tagName, true);

                tagList.addTag(tag);
            }

            return tagList;

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToTagList error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public TagSet parseJsonToTagSet(String json){
        // format -> [{“tagName”:”xxxx”}, {“tagName”:yyyy},….]
        TagSet set = new TagSet();
        try {
            JSONArray tagArray = new JSONArray(json);
            for(int i=0; i < tagArray.length(); i++) {
                JSONObject tagObj = tagArray.getJSONObject(i);
                String tagName = tagObj.getString(Constant.MESSAGE_JSON_TAG_NAME_TAG);

                set.add(tagName);
            }

            return set;

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToTagSet error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public String parseTagSetToJson(TagSet set) {
        // format -> [{“tagName”:”xxxx”}, {“tagName”:yyyy},….]
        try {
            JSONArray tagArray = new JSONArray();
            if(set != null) {
                for(String tagName : set.getList()) {
                    JSONObject tagObj = new JSONObject();
                    tagObj.put(Constant.MESSAGE_JSON_TAG_NAME_TAG, tagName);

                    tagArray.put(tagObj);
                }

                return tagArray.toString();

            }else{
                return "";
            }


        } catch (JSONException e){
            mLogging.debug(TAG, "parseTagSetToJson error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }


    }

    public String updateJournalDetailToJson(Journal journal, TagList deleteTagList) {
        JSONObject root = new JSONObject();

        try{
            root.put(Constant.MESSAGE_JSON_TITLE_TAG, journal.getTitle());
            root.put(Constant.MESSAGE_JSON_CONTENT_TAG, journal.getContent());
            root.put(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG, journal.getCountryCode());
            root.put(Constant.MESSAGE_JSON_JOURNAL_DATE_TAG, journal.getJournalDate());
            root.put(Constant.MESSAGE_JSON_JOURNAL_TIME_TAG, journal.getJournalTime());
            root.put(Constant.MESSAGE_JSON_CREATED_DATE_TAG, journal.getCreatedDate());
            root.put(Constant.MESSAGE_JSON_CREATED_TIME_TAG, journal.getCreatedTime());
            root.put(Constant.MESSAGE_JSON_IS_PUBLISHED_TAG, this.convertBooleanToString(journal.getIsPublished()));

            JSONArray tagArray = new JSONArray();
            JSONArray removeTagArray = new JSONArray();
            for(Tag tag : journal.getTagList().getList()){

                JSONObject tagObj = new JSONObject();
                tagObj.put(Constant.MESSAGE_JSON_ID_TAG, tag.getTempID());
                tagObj.put(Constant.MESSAGE_JSON_TAG_ID_TAG, tag.getID());
                tagObj.put(Constant.MESSAGE_JSON_TAG_NAME_TAG, tag.getName());

                if(tag.getIsChecked()){
                    // true, means still under the journal
                    tagArray.put(tagObj);
                }else if(!tag.getIsChecked() && !isStringEmpty(tag.getID())){
                    // not checked and has tag ID
                    removeTagArray.put(tagObj);
                }

            }

            if(deleteTagList != null) {
                for(Tag tag : deleteTagList.getList()) {
                    JSONObject tagObj = new JSONObject();
                    tagObj.put(Constant.MESSAGE_JSON_ID_TAG, tag.getTempID());
                    tagObj.put(Constant.MESSAGE_JSON_TAG_ID_TAG, tag.getID());
                    tagObj.put(Constant.MESSAGE_JSON_TAG_NAME_TAG, tag.getName());

                    removeTagArray.put(tagObj);
                }
            }



            root.put(Constant.MESSAGE_JSON_TAGS_TAG, tagArray); // tags to be added
            root.put(Constant.MESSAGE_JSON_REMOVE_TAGS_TAG, removeTagArray); // tags to be removed


            JSONObject projectObj = new JSONObject();
            projectObj.put(Constant.MESSAGE_JSON_PROJECT_JOURNAL_RELATION_ID_TAG, journal.getProject().getProjectJournalRelationID());
            projectObj.put(Constant.MESSAGE_JSON_PROJECT_ID_TAG, journal.getProject().getProjectID());

            root.put(Constant.MESSAGE_JSON_PROJECT_TAG, projectObj); // current assigned project

            return root.toString();

        } catch (JSONException e){
            mLogging.debug(TAG, "updateJournalDetailToJson error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }

    public String uploadNewJournalToJson(Journal journal) {
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.MESSAGE_JSON_TITLE_TAG, journal.getTitle());
            root.put(Constant.MESSAGE_JSON_CONTENT_TAG, journal.getContent());
            root.put(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG, journal.getCountryCode());
            root.put(Constant.MESSAGE_JSON_JOURNAL_DATE_TAG, journal.getJournalDate());
            root.put(Constant.MESSAGE_JSON_JOURNAL_TIME_TAG, journal.getJournalTime());
            root.put(Constant.MESSAGE_JSON_CREATED_DATE_TAG, journal.getCreatedDate());
            root.put(Constant.MESSAGE_JSON_CREATED_TIME_TAG, journal.getCreatedTime());
            root.put(Constant.MESSAGE_JSON_IS_PUBLISHED_TAG, this.convertBooleanToString(journal.getIsPublished()));

            JSONArray tagArray = new JSONArray();
            //JSONArray removeTagArray = new JSONArray();
            for(Tag tag : journal.getTagList().getList()){

                JSONObject tagObj = new JSONObject();
                tagObj.put(Constant.MESSAGE_JSON_ID_TAG, tag.getTempID());
                tagObj.put(Constant.MESSAGE_JSON_TAG_ID_TAG, tag.getID());
                tagObj.put(Constant.MESSAGE_JSON_TAG_NAME_TAG, tag.getName());

                if(tag.getIsChecked()){
                    // true, means still under the journal
                    tagArray.put(tagObj);
                }

            }

            root.put(Constant.MESSAGE_JSON_TAGS_TAG, tagArray); // tags to be added
            //root.put(Constant.MESSAGE_JSON_REMOVE_TAGS_TAG, removeTagArray); // tags to be removed


            JSONObject projectObj = new JSONObject();
            projectObj.put(Constant.MESSAGE_JSON_PROJECT_JOURNAL_RELATION_ID_TAG, journal.getProject().getProjectJournalRelationID());
            projectObj.put(Constant.MESSAGE_JSON_PROJECT_ID_TAG, journal.getProject().getProjectID());

            root.put(Constant.MESSAGE_JSON_PROJECT_TAG, projectObj); // current assigned project

            return root.toString();

        } catch (JSONException e){
            mLogging.debug(TAG, "uploadNewJournalToJson error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


    public String parseJsonToJournalID(String response) {
        try{
            JSONObject root = new JSONObject(response);
            JSONObject detailObj = root.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

            return detailObj.getString(Constant.MESSAGE_JSON_JOURNAL_ID_TAG);

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToJournalID error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }

    public String parseJsonToAlbumID(String response) {
        try{
            JSONObject root = new JSONObject(response);
            JSONObject detailObj = root.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

            return detailObj.getString(Constant.MESSAGE_JSON_ALBUM_ID_TAG);

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToAlbumID error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }

    public JSONArray parseJsonToTagArray(String response) {
        try{
            JSONObject root = new JSONObject(response);
            JSONObject detailObj = root.getJSONObject(Constant.MESSAGE_JSON_DETAIL_TAG);

            return detailObj.getJSONArray(Constant.MESSAGE_JSON_TAGS_TAG);

        } catch (JSONException e){
            mLogging.debug(TAG, "parseJsonToTagArray error => " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


    public String userObjectToJson(User user){
        JSONObject obj = new JSONObject();
        try{
            obj.put(Constant.MESSAGE_JSON_FACEBOOK_ID_TAG, user.getFacebookID());
            obj.put(Constant.MESSAGE_JSON_PICTURE_PROFILE_ID_TAG, user.getProfilePictureID());
            obj.put(Constant.MESSAGE_JSON_FIRST_NAME_TAG, user.getFirstName());
            obj.put(Constant.MESSAGE_JSON_MIDDLE_NAME_TAG, user.getMiddleName());
            obj.put(Constant.MESSAGE_JSON_lAST_NAME_TAG, user.getLastName());
            obj.put(Constant.MESSAGE_JSON_BIRTHDAY_TAG, user.getBirthday());
            obj.put(Constant.MESSAGE_JSON_AGE_TAG, user.getAge());
            obj.put(Constant.MESSAGE_JSON_ADDRESS_TAG, user.getAddress());
            //obj.put(Constant.MESSAGE_JSON_CITY_TAG, user.getCity());
            //obj.put(Constant.MESSAGE_JSON_STATE_TAG, user.getState());
            obj.put(Constant.MESSAGE_JSON_COUNTRY_TAG, user.getCountry());
            obj.put(Constant.MESSAGE_JSON_COUNTRY_CODE_TAG, user.getCountryCode());
            obj.put(Constant.MESSAGE_JSON_EMAIL_TAG, user.getEmail());
            obj.put(Constant.MESSAGE_JSON_CONTACT_TAG, user.getContact());

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
        if(val == null || val.trim().equals("") || val.trim().isEmpty()){
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

    public EnumPictureType getPictureType(String value){
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

    public String bitmapToBase64(Bitmap bitmap) {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            byteArrayOutputStream.close();
            byteArrayOutputStream = null;
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }catch (IOException e) {
            mLogging.debug(TAG, "bitmapToBase64 error => " + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }



    public boolean compareBothString(String a, String b){
        if(isStringEmpty(a) && isStringEmpty(b)){
            return true;
        }

        if(a.trim().toLowerCase().equals(b.trim().toLowerCase())){
            return true;
        }

        return false;
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
        if(this.isStringEmpty(userID) || this.isStringEmpty(pictureID)) {
            return "";
        }
        return Constant.URL_SMALL_PICTURE
                .replace(Constant.URL_DUMMY_USER_ID, userID)
                .replace(Constant.URL_DUMMY_FILE_NAME,
                        this.generateFilename(pictureID, pictureCoverType));
    }

    private String getNormalPictureUrl(String pictureID, String pictureCoverType, String userID) {
        if(this.isStringEmpty(userID) || this.isStringEmpty(pictureID)) {
            return "";
        }
        return Constant.URL_NORMAL_PICTURE
                .replace(Constant.URL_DUMMY_USER_ID, userID)
                .replace(Constant.URL_DUMMY_FILE_NAME,
                        this.generateFilename(pictureID, pictureCoverType));
    }

}
