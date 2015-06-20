package com.orbital.lead.model;

/**
 * Created by joseph on 6/6/2015.
 */
public class Constant {

    // Fragment names to add back stack
    public static final String FRAGMENT_LOGIN_NAME = "FragmentLogin";

    // Title
    public static final String TITLE_LEAD = "Lead";


    // URLs
    public static final String URL_CLIENT_SERVER = "http://52.74.189.72/lead/client/client.php";

    // URL post parameters
    public static final String URL_POST_PARAMETER_TAG_QUERY_TYPE = "type";
    public static final String URL_POST_PARAMETER_TAG_USERNAME = "u";
    public static final String URL_POST_PARAMETER_TAG_PASSWORD = "pw";
    public static final String URL_POST_PARAMETER_TAG_USER_ID = "uid";

    // default type values
    public static final String TYPE_LOGIN = "100";
    public static final String TYPE_LOGOUT = "101";
    public static final String TYPE_GET_USER_PROFILE = "300";
    public static final String TYPE_UPDATE_USER_PROFILE = "301";
    public static final String TYPE_CREATE_USER_PROFILE = "302";

    // Message json tag {'code'='', 'message':''}
    public static final String MESSAGE_JSON_CODE_TAG = "code";
    public static final String MESSAGE_JSON_MESSAGE_TAG = "message";
    public static final String MESSAGE_JSON_USER_ID_TAG = "userID"; //only appear if login successfully
    public static final String MESSAGE_JSON_DETAIL_TAG = "detail";
    public static final String MESSAGE_JSON_FACEBOK_ID_TAG = "facebookID";
    public static final String MESSAGE_JSON_LEAD_USER_ID_TAG = "leadUserID";
    public static final String MESSAGE_JSON_PICTURE_PROFILE_ID_TAG = "pictureProfileID";
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


    // Message code
    public static final String MESSAGE_SUCCESS_TYPE = ".1";
    public static final String MESSAGE_FAILURE_TYPE = ".2";


    //Bundle paremeters
    public static final String BUNDLE_PARAM_IS_FACEBOOK_LOGIN = "is_facebook_login";
    public static final String BUNDLE_PARAM_FACEBOOK_USER_ID = "fb_user_id";
    public static final String BUNDLE_PARAM_LEAD_USER_ID = "lead_user_id";
    public static final String BUNDLE_PARAM_USERNAME = "username";
    public static final String BUNDLE_PARAM_PASSWORD= "password";


    // Intent Service Extra tag name
    public static final String INTENT_SERVICE_EXTRA_RECEIVER_TAG = "receiver";


}
