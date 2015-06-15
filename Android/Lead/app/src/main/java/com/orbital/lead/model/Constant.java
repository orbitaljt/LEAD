package com.orbital.lead.model;

/**
 * Created by joseph on 6/6/2015.
 */
public class Constant {

    // Fragment names to add back stack
    public static final String FRAGMENT_LOGIN_NAME = "FragmentLogin";


    // URLs
    public static final String URL_CLIENT_SERVER = "http://52.74.189.72/lead/client/client.php";

    // URL post parameters
    public static final String URL_POST_PARAMETER_TAG_QUERY_TYPE = "type";
    public static final String URL_POST_PARAMETER_TAG_USERNAME = "u";
    public static final String URL_POST_PARAMETER_TAG_PASSWORD = "pw";

    // default values
    public static final String TYPE_LOGIN = "100";
    public static final String TYPE_LOGOUT = "101";


    // Message json tag {'code'='', 'message':''}
    public static final String MESSAGE_JSON_CODE_TAG = "code";
    public static final String MESSAGE_JSON_MESSAGE_TAG = "message";

    // Message code
    public static final String MESSAGE_SUCCESS_TYPE = ".1";
    public static final String MESSAGE_FAILURE_TYPE = ".2";

}
