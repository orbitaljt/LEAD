package com.orbital.lead.model;

import com.orbital.lead.R;

/**
 * Created by joseph on 6/6/2015.
 */
public class Constant {

    // Fragment names to add back stack
    public static final String FRAGMENT_LOGIN_NAME = "FragmentLogin";

    // Title
    public static final String TITLE_LEAD = "Lead";
    public static final String TITLE_SPECIFIC_JOURNAL = "My Journal";
    public static final String TITLE_FRAGMENT_ALBUM = "Album";
    public static final String TITLE_FRAGMENT_PICTURES = "Pictures";
    public static final String TITLE_FRAGMENT_PROFILE = "Profile";
    public static final String TITLE_ADD_NEW_JOURNAL = "New Journal";

    // Drawer List
    public static final String DRAWER_LIST_PROFILE_NAME = "Profile";
    public static final String DRAWER_LIST_ALBUM_NAME = "Albums";
    public static final String DRAWER_LIST_BADGE_NAME = "Badge";
    public static final String DRAWER_LIST_MILESTONE_NAME = "Milestone";
    public static final String DRAWER_LIST_JOURNAL_NAME = "Journal";
    public static final String DRAWER_LIST_EXPERIENCE_NAME = "Experience";
    public static final String DRAWER_LIST_SETTING_NAME = "Setting";
    public static final int DRAWER_LIST_PROFILE_ICON = R.mipmap.ic_profile;
    public static final int DRAWER_LIST_ALBUM_ICON = R.mipmap.ic_album;
    public static final int DRAWER_LIST_BADGE_ICON = R.mipmap.ic_badge;
    public static final int DRAWER_LIST_MILESTONE_ICON = R.mipmap.ic_milestone;
    public static final int DRAWER_LIST_JOURNAL_ICON = R.mipmap.ic_journal;
    public static final int DRAWER_LIST_EXPERIENCE_ICON = R.mipmap.ic_experience;
    public static final int DRAWER_LIST_SETTING_ICON = R.mipmap.ic_setting;


    // Fragment name
    public static final String FRAGMENT_JOURNAL_LIST = "FragmentJournalList";
    public static final String FRAGMENT_PICTURES = "FragmentPictures";
    public static final String FRAGMENT_ALBUM = "FragmentAlbum";
    public static final String FRAGMENT_PROFILE = "FragmentProfile";


    // URLs
    public static final String URL_CLIENT_SERVER = "http://52.74.189.72/lead/client/client.php";
    public static final String URL_NORMAL_PICTURE = "https://s3-ap-southeast-1.amazonaws.com/leadimage/[user_id]/picture/[file_name]";
    public static final String URL_SMALL_PICTURE = "https://s3-ap-southeast-1.amazonaws.com/leadimage/[user_id]/picture/small/[file_name]";
    public static final String URL_DUMMY_FACEBOOK_USER_ID = "[fb_user_id]";
    public static final String URL_DUMMY_USER_ID = "[user_id]";
    public static final String URL_DUMMY_FILE_NAME = "[file_name]";
    public static final String URL_DUMMY_ALBUM_ID = "[album_id]";
    public static final String URL_DUMMY_ACCESS_TOKEN = "[access_token]";
    public static final String URL_FACEBOOK_PROFILE_PICTURE = "https://graph.facebook.com/[fb_user_id]/picture?width=640&height=640";
    public static final String URL_FACEBOOK_ALBUM_PHOTO_COVER = "https://graph.facebook.com/[album_id]/picture?type=album&access_token=[access_token]";
    public static final String URL_FACEBOOK_ALBUM_PICTURES = "https://graph.facebook.com/[album_id]/photos?access_token=[access_token]";

    // URL post parameters
    public static final String URL_POST_PARAMETER_TAG_QUERY_TYPE = "type";
    public static final String URL_POST_PARAMETER_TAG_USERNAME = "u";
    public static final String URL_POST_PARAMETER_TAG_PASSWORD = "pw";
    public static final String URL_POST_PARAMETER_TAG_USER_ID = "uid";
    public static final String URL_POST_PARAMETER_TAG_FACEBOOK_USER_ID = "fb_uid";
    public static final String URL_POST_PARAMETER_TAG_USER_ALBUM_ID = "aid";
    public static final String URL_POST_PARAMETER_TAG_PICTURE_QUERY_QUANTITY = "pic_qq";
    //public static final String URL_POST_PARAMETER_TAG_USER_JOURNAL_LIST_ID = "jlid";
    public static final String URL_POST_PARAMETER_TAG_USER_JOURNAL_ID = "jid";
    public static final String URL_POST_PARAMETER_TAG_DETAIL = "detail";
    public static final String URL_POST_PARAMETER_TAG_URL = "url";
    public static final String URL_POST_PARAMETER_TAG_FILE_NAME = "filename";
    public static final String URL_POST_PARAMETER_TAG_FILE_TYPE = "filetype";
    public static final String URL_POST_PARAMETER_TAG_FROM_FACEBOOK = "from_fb";
    public static final String URL_POST_PARAMETER_TAG_FROM_LEAD = "from_lead";
    public static final String URL_POST_PARAMETER_TAG_FACEBOOK_ACCESS_TOKEN = "access_token";

    // default type values
    public static final String TYPE_LOGIN = "100";
    public static final String TYPE_LOGOUT = "101";
    public static final String TYPE_LOGIN_WITH_FACEBOOK = "200";
    public static final String TYPE_GET_USER_PROFILE = "300";
    public static final String TYPE_GET_USER_PROFILE_USING_FACEBOOK = "301";
    public static final String TYPE_UPDATE_USER_PROFILE = "302";
    public static final String TYPE_CREATE_USER_PROFILE = "303";
    public static final String TYPE_GET_USER_ALL_JOURNAL = "400";
    public static final String TYPE_UPDATE_USER_JOURNAL = "401";
    public static final String TYPE_GET_USER_PICTURE = "900";
    public static final String TYPE_GET_USER_SPECIFIC_ALBUM = "901";
    public static final String TYPE_GET_USER_ALL_ALBUM = "902";
    public static final String TYPE_GET_ALL_PROJECT = "1000";
    public static final String TYPE_UPLOAD_IMAGE_URL = "2000";
    public static final String TYPE_FACEBOOK = "1";


    public static final String PICTURE_QUERY_QUANTITY_PROFILE = "profile";
    public static final String PICTURE_QUERY_QUANTITY_ALBUM = "album";
    public static final String PICTURE_QUERY_QUANTITY_ALL = "all";

    // Message json tag {'code'='', 'message':''}
    public static final String MESSAGE_JSON_ID_TAG = "id";
    public static final String MESSAGE_JSON_CODE_TAG = "code";
    public static final String MESSAGE_JSON_MESSAGE_TAG = "message";
    public static final String MESSAGE_JSON_USER_ID_TAG = "userID"; //only appear if login successfully
    public static final String MESSAGE_JSON_DETAIL_TAG = "detail";
    public static final String MESSAGE_JSON_FACEBOOK_ID_TAG = "facebookID";
    public static final String MESSAGE_JSON_LEAD_USER_ID_TAG = "leadUserID";
    public static final String MESSAGE_JSON_PICTURE_PROFILE_ID_TAG = "pictureProfileID";
    public static final String MESSAGE_JSON_PICTURE_PROFILE_TYPE_TAG = "pictureProfileType";
   // public static final String MESSAGE_JSON_JOURNAL_LIST_ID_TAG = "journalListID";
   // public static final String MESSAGE_JSON_EXPERIENCE_LIST_TAG = "experienceListID";
    public static final String MESSAGE_JSON_FIRST_NAME_TAG = "firstName";
    public static final String MESSAGE_JSON_MIDDLE_NAME_TAG = "middleName";
    public static final String MESSAGE_JSON_lAST_NAME_TAG = "lastName";
    public static final String MESSAGE_JSON_AGE_TAG = "age";
    public static final String MESSAGE_JSON_BIRTHDAY_TAG = "birthday";
    public static final String MESSAGE_JSON_ADDRESS_TAG = "address";
    public static final String MESSAGE_JSON_CITY_TAG = "city";
    public static final String MESSAGE_JSON_STATE_TAG = "state";
    public static final String MESSAGE_JSON_COUNTRY_TAG = "country";
    public static final String MESSAGE_JSON_COUNTRY_CODE_TAG = "countryCode";
    public static final String MESSAGE_JSON_EMAIL_TAG = "email";
    public static final String MESSAGE_JSON_CONTACT_TAG = "contact";
    public static final String MESSAGE_JSON_CREATED_DATE_TAG = "createdDate";
    public static final String MESSAGE_JSON_CREATED_TIME_TAG = "createdTime";
    public static final String MESSAGE_JSON_LAST_LOGIN_DATE_TAG = "lastLoginDate";
    public static final String MESSAGE_JSON_LAST_LOGIN_TIME_TAG = "lastLoginTime";
    public static final String MESSAGE_JSON_JOURNAL_ID_TAG = "journalID";
    public static final String MESSAGE_JSON_PICTURE_ID_TAG = "pictureID";
    public static final String MESSAGE_JSON_PICTURE_COVER_ID_TAG = "pictureCoverID";
    public static final String MESSAGE_JSON_PICTURE_COVER_TYPE_TAG = "pictureCoverType";
    public static final String MESSAGE_JSON_PICTURE_TYPE_TAG = "pictureType";
    public static final String MESSAGE_JSON_ALBUM_ID_TAG = "albumID";
    public static final String MESSAGE_JSON_TITLE_TAG = "title";
    public static final String MESSAGE_JSON_CONTENT_TAG = "content";
    public static final String MESSAGE_JSON_DESCRIPTION_TAG = "description";
    public static final String MESSAGE_JSON_JOURNAL_DATE_TAG = "journalDate";
    public static final String MESSAGE_JSON_JOURNAL_TIME_TAG = "journalTime";
    public static final String MESSAGE_JSON_LAST_MODIFIED_DATE_TAG = "lastModifiedDate";
    public static final String MESSAGE_JSON_LAST_MODIFIED_TIME_TAG = "lastModifiedTime";
    public static final String MESSAGE_JSON_IS_PUBLISHED_TAG = "isPublished";
    public static final String MESSAGE_JSON_LIST_OF_PICTURES_TAG = "listOfPictures";
    public static final String MESSAGE_JSON_TAGS_TAG = "tags";
    public static final String MESSAGE_JSON_REMOVE_TAGS_TAG = "removeTags";
    public static final String MESSAGE_JSON_TAG_ID_TAG = "tagID";
    public static final String MESSAGE_JSON_TAG_NAME_TAG = "tagName";
    public static final String MESSAGE_JSON_PROJECT_TAG = "project";
    public static final String MESSAGE_JSON_PROJECT_ID_TAG = "projectID";
    public static final String MESSAGE_JSON_PROJECT_JOURNAL_RELATION_ID_TAG = "projectJournalRelationID";
    public static final String MESSAGE_JSON_PROJECT_START_DATE_TAG = "projectStartDate";
    public static final String MESSAGE_JSON_PROJECT_START_TIME_TAG = "projectStartTime";
    public static final String MESSAGE_JSON_PROJECT_END_DATE_TAG = "projectEndDate";
    public static final String MESSAGE_JSON_PROJECT_END_TIME_TAG = "projectEndTime";
    public static final String MESSAGE_JSON_PROJECT_YEAR_TAG = "projectYear";
    public static final String MESSAGE_JSON_NAME_TAG = "name";


    public static final String MESSAGE_JSON_JOURNAL_CONTENT_ID_TAG = "journalContentID";
    public static final String MESSAGE_JSON_JOURNAL_QUESTION_ID_TAG = "journalQuestionID";
    public static final String MESSAGE_JSON_JOURNAL_QUESTION_TAG = "contentQuestion";
    public static final String MESSAGE_JSON_JOURNAL_ANSWER_TAG = "contentAnswer";
    public static final String MESSAGE_JSON_JOURNAL_QUESTION_ORDER_TAG = "questionOrder";
    /*
    public static final String MESSAGE_JSON = "";
    public static final String MESSAGE_JSON = "";
    public static final String MESSAGE_JSON = "";
    public static final String MESSAGE_JSON = "";
    public static final String MESSAGE_JSON = "";
    public static final String MESSAGE_JSON = "";
    */

    //public static final String FACEBOOK_DUMMY_AFTER_ID = "[after_id]";
    public static final String FACEBOOK_GRAPH_ALBUM_QUERY = "albums.limit(5){id,cover_photo,name,created_time,updated_time,count,description}";
    public static final String FACEBOOK_GRAPH_ALBUM_PICTURES_QUERY = "created_time,event,id,name,updated_time,source";
    public static final String FACEBOOK_JSON_ID_TAG = "id";
    public static final String FACEBOOK_JSON_PROFILE_PICTURE_TAG = "picture";
    public static final String FACEBOOK_JSON_DATA_TAG = "data";
    public static final String FACEBOOK_JSON_EMAIL_TAG = "email";
    public static final String FACEBOOK_JSON_BIRTHDAY_TAG = "birthday";
    public static final String FACEBOOK_JSON_LOCATION_TAG = "location";
    public static final String FACEBOOK_JSON_URL_TAG = "url";
    public static final String FACEBOOK_JSON_FIRST_NAME_TAG = "first_name";
    public static final String FACEBOOK_JSON_MIDDLE_NAME_TAG = "middle_name";
    public static final String FACEBOOK_JSON_LAST_NAME_TAG = "last_name";
    public static final String FACEBOOK_JSON_ALBUMS_TAG = "albums";
    public static final String FACEBOOK_JSON_NAME_TAG = "name";
    public static final String FACEBOOK_JSON_COVER_PHOTO_TAG = "cover_photo";
    public static final String FACEBOOK_JSON_CREATED_TIME_TAG = "created_time";
    public static final String FACEBOOK_JSON_UPDATED_TIME_TAG = "updated_time";
    public static final String FACEBOOK_JSON_PAGING_TAG = "paging";
    public static final String FACEBOOK_JSON_NEXT_TAG = "next";
    public static final String FACEBOOK_JSON_DESCRIPTION_TAG = "description";
    public static final String FACEBOOK_JSON_COUNT_TAG = "count";
    public static final String FACEBOOK_JSON_SOURCE_TAG = "source";
    //public static final String FACEBOOK_JSON


    // Message code
    public static final String MESSAGE_SUCCESS_TYPE = ".1";
    public static final String MESSAGE_FAILURE_TYPE = ".2";
    public static final String MESSAGE_HAS_RECORD = "1001.1";
    public static final String MESSAGE_NO_RECORD = "1001.2";

    //Bundle paremeters
    public static final String BUNDLE_PARAM_USERNAME = "username";
    public static final String BUNDLE_PARAM_PASSWORD= "password";
    public static final String BUNDLE_PARAM_IS_FACEBOOK_LOGIN = "is_facebook_login";
    public static final String BUNDLE_PARAM_FACEBOOK_USER_ID = "fb_user_id";
    public static final String BUNDLE_PARAM_FACEBOOK_RESPONSE = "fb_response";
    public static final String BUNDLE_PARAM_LEAD_USER_ID = "lead_user_id";
    public static final String BUNDLE_PARAM_IS_REGISTERED = "is_registered";
    public static final String BUNDLE_PARAM_JOURNAL_ID = "journal_id";
    public static final String BUNDLE_PARAM_JOURNAL_IMAGE_URL = "journal_image_url";
    public static final String BUNDLE_PARAM_JOURNAL = "journal";
    public static final String BUNDLE_PARAM_OPEN_FRAGMENT_TYPE = "open_type";
    public static final String BUNDLE_PARAM_ALBUM = "album";
    public static final String BUNDLE_PARAM_PICTURE_LIST = "plist";
    public static final String BUNDLE_PARAM_JOURNAL_TOGGLE_REFRESH = "journal_refresh"; // value is true or false

    // Intent Service Extra tag name
    public static final String INTENT_SERVICE_EXTRA_RECEIVER_TAG = "receiver";
    public static final String INTENT_SERVICE_EXTRA_USER_ID_TAG = "uid";
    public static final String INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG = "jid";
    public static final String INTENT_SERVICE_EXTRA_DETAIL_TAG = "detail";
    //public static final String INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG = "jlid";
    public static final String INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_ID_TAG = "u_profile_pic_id";
    public static final String INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_FILENAME_TAG = "u_profile_pic_filename";
    public static final String INTENT_SERVICE_EXTRA_TYPE_TAG = "type";
    public static final String INTENT_SERVICE_EXTRA_USER_ALBUM_ID_TAG = "aid";

    // intent service return result to main UI
    public static final String INTENT_SERVICE_RESULT_DOWNLOAD_STATUS_TAG = "download_status";
    public static final String INTENT_SERVICE_RESULT_DOWNLOAD_SUCCESS_VALUE = "success";
    public static final String INTENT_SERVICE_RESULT_DOWNLOAD_FAILURE_VALUE = "failure";
    public static final String INTENT_SERVICE_RESULT_DOWNLOAD_PROFILE_PICTURE_FILENAME_TAG = "download_u_profile_pic_filename";
    public static final String INTENT_SERVICE_RESULT_JSON_STRING_TAG = "json";

    // Amazon S3
    public static final String AWS_S3_BUCKET_NAME = "leadimage";
    public static final String AWS_S3_FOLDER_PICTURE_NAME = "picture"; // bucket_name/user_id/picture/xxxx
    public static final String AWS_S3_SLASH = "/";


    // Grid albums
    public static final String GRID_DUMMY_NUMBER_OF_PICTURES = "[num_of_pics]";
    public static final String GRID_ALBUM_NUMBER_OF_PICTURES_FORMAT = "[num_of_pics] Photos";
    public static final String GRID_ALBUM_DEFAULT_UNTITLED = "Untitled Album";

    // Local Storage
    // getExternalStorageDirectory/Lead/Media/Profile/
    // getExternalStorageDirectory/Lead/Media/Picture/
    public static final String STORAGE_APPLICATION_NAME = "Lead";
    public static final String STORAGE_MEDIA_NAME = "Media";
    public static final String STORAGE_PROFILE_NAME = "Profile";
    public static final String STORAGE_PICTURE_NAME = "Picture";
    public static final String STORAGE_SLASH = "/";
    public static final String STORAGE_DOT_EXTENSION = ".";
    public static final String STORAGE_APPLICATION_PROFILE_FOLDER_PATH = STORAGE_SLASH + STORAGE_APPLICATION_NAME + STORAGE_SLASH + STORAGE_MEDIA_NAME + STORAGE_SLASH + STORAGE_PROFILE_NAME + STORAGE_SLASH;
    public static final String STORAGE_APPLICATION_PICTURE_FOLDER_PATH = STORAGE_SLASH + STORAGE_APPLICATION_NAME + STORAGE_SLASH + STORAGE_MEDIA_NAME + STORAGE_SLASH + STORAGE_PICTURE_NAME + STORAGE_SLASH;


    // Dialog title
    public static final String DIALOG_TITLE_ADD_PHOTO = "Add Photo";
    public static final String DIALOG_TITLE_SELECT_PHOTO = "Select Photo";

    public static final int DIALOG_REQUEST_OPEN_INTENT_IMAGES = 1;
    public static final int DIALOG_REQUEST_OPEN_INTENT_CAMERA = 2;
    public static final int DIALOG_REQUEST_OPEN_FACEBOOK_ALBUM = 3;


    // Formatting Strings
    public static final String DUMMY_NUMBER = "[num]";
    public static final String STRING_NUMBER_OF_PICTURES_FORMAT = "All Photos ([num])";


    // History
    public static final String PREFERENCE_HISTORY_RECENT_TAG_FILE_NAME = "recent_tag.txt";
    public static final String PREFERENCE_JSON_TAG_NAME_TAG = "name";



}
