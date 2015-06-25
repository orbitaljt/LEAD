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

    // Drawer List
    public static final String DRAWER_LIST_PROFILE_NAME = "Profile";
    public static final String DRAWER_LIST_BADGE_NAME = "Badge";
    public static final String DRAWER_LIST_MILESTONE_NAME = "Milestone";
    public static final String DRAWER_LIST_JOURNAL_NAME = "Journal";
    public static final String DRAWER_LIST_EXPERIENCE_NAME = "Experience";
    public static final String DRAWER_LIST_SETTING_NAME = "Setting";
    public static final int DRAWER_LIST_PROFILE_ICON = R.mipmap.ic_profile;
    public static final int DRAWER_LIST_BADGE_ICON = R.mipmap.ic_badge;
    public static final int DRAWER_LIST_MILESTONE_ICON = R.mipmap.ic_milestone;
    public static final int DRAWER_LIST_JOURNAL_ICON = R.mipmap.ic_journal;
    public static final int DRAWER_LIST_EXPERIENCE_ICON = R.mipmap.ic_experience;
    public static final int DRAWER_LIST_SETTING_ICON = R.mipmap.ic_setting;


    // URLs
    public static final String URL_CLIENT_SERVER = "http://52.74.189.72/lead/client/client.php";
    public static final String URL_NORMAL_PICTURE = "https://s3-ap-southeast-1.amazonaws.com/leadimage/[user_id]/picture/[file_name]";
    public static final String URL_SMALL_PICTURE = "https://s3-ap-southeast-1.amazonaws.com/leadimage/[user_id]/picture/small/[file_name]";
    public static final String URL_DUMMY_USER_ID = "[user_id]";
    public static final String URL_DUMMY_FILE_NAME = "[file_name]";

    // URL post parameters
    public static final String URL_POST_PARAMETER_TAG_QUERY_TYPE = "type";
    public static final String URL_POST_PARAMETER_TAG_USERNAME = "u";
    public static final String URL_POST_PARAMETER_TAG_PASSWORD = "pw";
    public static final String URL_POST_PARAMETER_TAG_USER_ID = "uid";
    public static final String URL_POST_PARAMETER_TAG_PICTURE_QUERY_QUANTITY = "pic_qq";
    public static final String URL_POST_PARAMETER_TAG_USER_JOURNAL_LIST_ID = "jlid";
    public static final String URL_POST_PARAMETER_TAG_USER_JOURNAL_ID = "jid";

    // default type values
    public static final String TYPE_LOGIN = "100";
    public static final String TYPE_LOGOUT = "101";
    public static final String TYPE_GET_USER_PROFILE = "300";
    public static final String TYPE_UPDATE_USER_PROFILE = "301";
    public static final String TYPE_CREATE_USER_PROFILE = "302";
    public static final String TYPE_GET_USER_ALL_JOURNAL = "400";
    public static final String TYPE_GET_USER_SPECIFIC_JOURNAL = "401";
    public static final String TYPE_GET_USER_PICTURE = "900";

    public static final String PICTURE_QUERY_QUANTITY_PROFILE = "profile";
    public static final String PICTURE_QUERY_QUANTITY_ALBUM = "album";
    public static final String PICTURE_QUERY_QUANTITY_ALL = "all";

    // Message json tag {'code'='', 'message':''}
    public static final String MESSAGE_JSON_CODE_TAG = "code";
    public static final String MESSAGE_JSON_MESSAGE_TAG = "message";
    public static final String MESSAGE_JSON_USER_ID_TAG = "userID"; //only appear if login successfully
    public static final String MESSAGE_JSON_DETAIL_TAG = "detail";
    public static final String MESSAGE_JSON_FACEBOK_ID_TAG = "facebookID";
    public static final String MESSAGE_JSON_LEAD_USER_ID_TAG = "leadUserID";
    public static final String MESSAGE_JSON_PICTURE_PROFILE_ID_TAG = "pictureProfileID";
    public static final String MESSAGE_JSON_PICTURE_PROFILE_TYPE_TAG = "pictureProfileType";
    public static final String MESSAGE_JSON_JOURNAL_LIST_ID_TAG = "journalListID";
    public static final String MESSAGE_JSON_EXPERIENCE_LIST_TAG = "experienceListID";
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
    public static final String MESSAGE_JSON_CREATED_DATE_TAG = "createdDate";
    public static final String MESSAGE_JSON_CREATED_TIME_TAG = "createdTime";
    public static final String MESSAGE_JSON_LAST_LOGIN_DATE_TAG = "lastLoginDate";
    public static final String MESSAGE_JSON_LAST_LOGIN_TIME_TAG = "lastLoginTime";
    public static final String MESSAGE_JSON_JOURNAL_ID_TAG = "journalID";
    public static final String MESSAGE_JSON_PICTURE_COVER_ID_TAG = "pictureCoverID";
    public static final String MESSAGE_JSON_PICTURE_COVER_TYPE_TAG = "pictureCoverType";
    public static final String MESSAGE_JSON_PICTURE_ALBUM_ID_TAG = "pictureAlbumID";
    public static final String MESSAGE_JSON_TITLE_TAG = "title";
    public static final String MESSAGE_JSON_CONTENT_TAG = "content";
    public static final String MESSAGE_JSON_JOURNAL_DATE_TAG = "journalDate";
    public static final String MESSAGE_JSON_JOURNAL_TIME_TAG = "journalTime";
    public static final String MESSAGE_JSON_LAST_MODIFIED_DATE_TAG = "lastModifiedDate";
    public static final String MESSAGE_JSON_LAST_MODIFIED_TIME_TAG = "lastModifiedTime";
    public static final String MESSAGE_JSON_IS_PUBLISHED_TAG = "isPublished";
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

    // Message code
    public static final String MESSAGE_SUCCESS_TYPE = ".1";
    public static final String MESSAGE_FAILURE_TYPE = ".2";

    //Bundle paremeters
    public static final String BUNDLE_PARAM_USERNAME = "username";
    public static final String BUNDLE_PARAM_PASSWORD= "password";
    public static final String BUNDLE_PARAM_IS_FACEBOOK_LOGIN = "is_facebook_login";
    public static final String BUNDLE_PARAM_FACEBOOK_USER_ID = "fb_user_id";
    public static final String BUNDLE_PARAM_LEAD_USER_ID = "lead_user_id";
    public static final String BUNDLE_PARAM_JOURNAL_ID = "journal_id";
    public static final String BUNDLE_PARAM_JOURNAL_IMAGE_URL = "journal_image_url";
    public static final String BUNDLE_PARAM_JOURNAL = "journal";


    // Intent Service Extra tag name
    public static final String INTENT_SERVICE_EXTRA_RECEIVER_TAG = "receiver";
    public static final String INTENT_SERVICE_EXTRA_USER_ID_TAG = "uid";
    public static final String INTENT_SERVICE_EXTRA_USER_JOURNAL_ID_TAG = "jid";
    public static final String INTENT_SERVICE_EXTRA_USER_JOURNAL_LIST_ID_TAG = "jlid";
    public static final String INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_ID_TAG = "u_profile_pic_id";
    public static final String INTENT_SERVICE_EXTRA_USER_PROFILE_PICTURE_FILENAME_TAG = "u_profile_pic_filename";
    public static final String INTENT_SERVICE_EXTRA_TYPE_TAG = "type";
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
}
