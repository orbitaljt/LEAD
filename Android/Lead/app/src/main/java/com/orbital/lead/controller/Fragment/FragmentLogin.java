package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.R;
import com.orbital.lead.controller.Activity.MainActivity;
import com.orbital.lead.logic.Asynchronous.AsyncLogin;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.LocalStorage.LocalStorage;
import com.orbital.lead.logic.Logic;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumLoginType;
import com.orbital.lead.model.EnumMessageType;
import com.orbital.lead.model.Message;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private LoginActivity mLoginActivity;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mBtnLogin;
    private ProgressDialog prog;

    // Facebook
    // Controls
    private LoginButton mFacebookLoginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private Profile pendingUpdateForUser;

    private OnFragmentInteractionListener mListener;

    private CustomLogging mLogging;
    private Logic mLogic;
    private LocalStorage mLocalStorage;
    private Parser mParser;

    private final String TAG = this.getClass().getSimpleName();
    private boolean hasLogin;
    private boolean isFacebookLogin;
    private String leadUserID;
    private String facebookUserID;

    private String facebookResponse;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLogin.
     */
    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        this.initLogging();
        this.mLogging.debug(TAG, "onCreate");

        this.initFacebook();
        this.initFacebookCallbackManager();
        this.initProfileTracker();
        this.initAccessTokenTrack();
        //if(hasLoginToFacebook()){
        Profile.fetchProfileForCurrentAccessToken();
        this.setProfile(Profile.getCurrentProfile());
        //}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mLogging.debug(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.initLogic();
        //this.initLocalStorage();
        this.initParser();

        this.initFacebookLoginButton(rootView);
        this.initEditTextUsername(rootView);
        this.initEditTextPassword(rootView);
        this.initButtonLogin(rootView);

        // check if lead folder in external storage exist or not
        //this.checkLeadFolder();

        if (this.pendingUpdateForUser != null) {
            setProfile(this.pendingUpdateForUser);
            this.pendingUpdateForUser = null;
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        this.restructureFacebookLoginButton();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        if(activity instanceof LoginActivity){
            this.initLoginActivity((LoginActivity) activity);
        }
        */

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mLogging.debug(TAG, "onActivityResult");
        this.callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.profileTracker.stopTracking();
        this.accessTokenTracker.startTracking();
    }

    public void initEditTextUsername(View v){
        this.mEditTextUsername = (EditText) v.findViewById(R.id.editTextUsername);
    }

    public String getEditTextUsername(){
        return this.mEditTextUsername.getText().toString();
    }

    public void initEditTextPassword(View v){
        this.mEditTextPassword = (EditText) v.findViewById(R.id.editTextPassword);
    }

    public String getEditTextPassword(){
        return this.mEditTextPassword.getText().toString();
    }

    public void initButtonLogin(View v){
        this.mBtnLogin = (Button) v.findViewById(R.id.btnLogin);
        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogging.debug(TAG, "mBtnLogin onClick");
                mLogging.debug(TAG, "getEditTextUsername() -> " + getEditTextUsername());
                mLogging.debug(TAG, "getEditTextPassword() -> " + getEditTextPassword());
                //mLogic.login(getLoginActivity(), getEditTextUsername(), getEditTextPassword());
                setIsNotFacebookLogin();
                runLogin(getEditTextUsername(), getEditTextPassword());
            }
        });
    }


    public void initFacebook(){
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    public void initFacebookCallbackManager(){
        this.callbackManager = CallbackManager.Factory.create();
    }

    public void initFacebookLoginButton(View v){
        List<String> mPermission = new ArrayList<String>();
        mPermission.add("public_profile");
        mPermission.add("email");
        mPermission.add("user_about_me");
        mPermission.add("user_photos");
        mPermission.add("user_birthday");
        mPermission.add("user_location");

        this.mFacebookLoginButton = (LoginButton) v.findViewById(R.id.btnLoginWithFacebook);
        this.mFacebookLoginButton.setReadPermissions(mPermission);
        this.mFacebookLoginButton.setFragment(this);

        // Callback registration
        this.mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLogging.debug(TAG, "onSuccess");
               // mLogging.debug(TAG, "access token -> " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                mLogging.debug(TAG, "registerCallback onCancel");
                setIsNotFacebookLogin();
            }

            @Override
            public void onError(FacebookException exception) {
                mLogging.debug(TAG, "registerCallback onError -> " + exception.getMessage().toString());
                setIsNotFacebookLogin();
            }
        });

    }

    public void initProfileTracker() {
        this.profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                mLogging.debug(TAG, "onCurrentProfileChanged");
                setProfile(currentProfile);
            }
        };
    }

    public void initAccessTokenTrack(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // On AccessToken changes fetch the new profile which fires the event on
                // the ProfileTracker if the profile is different
                mLogging.debug(TAG, "onCurrentAccessTokenChanged currentAccessToken -> " + currentAccessToken );
                Profile.fetchProfileForCurrentAccessToken();
            }
        };
    }

    /*
    public void initLoginActivity(LoginActivity activity){
        this.mLoginActivity = activity;
    }

    public LoginActivity getLoginActivity(){
        return this.mLoginActivity;
    }
    */

    public void initLogic(){
        this.mLogic = Logic.getInstance();
    }

    public void initLocalStorage(){
        this.mLocalStorage = LocalStorage.getInstance();
    }

    public void initParser(){
        if(this.mParser == null) {
            this.mParser = Parser.getInstance();
        }
    }


    public void runLogin(String username, String password){
        HttpAsyncLogin mAsyncLogin = new HttpAsyncLogin(getActivity(), EnumLoginType.LOGIN_LEAD);
        mAsyncLogin.execute(EnumLoginType.LOGIN_LEAD.getText(), username, password);
    }

    public void runLoginUsingFacebook(String facebookUserID){
        HttpAsyncLogin mAsyncLogin = new HttpAsyncLogin(getActivity(), EnumLoginType.LOGIN_FACEBOOK);
        mAsyncLogin.execute(EnumLoginType.LOGIN_FACEBOOK.getText(), facebookUserID);
    }


    public void displayMainActivity(String facebookID, String leadUserID, boolean isRegistered){
        Intent newIntent = new Intent(getActivity(), MainActivity.class);
        Bundle mBundle = new Bundle();

        //mLogging.debug(TAG, "isRegistered => " + isRegistered);

        if(this.isFacebookLogin()){ // if login through facebook, just get ID
            mBundle.putString(Constant.BUNDLE_PARAM_FACEBOOK_USER_ID, facebookID); // "" or value
            mBundle.putString(Constant.BUNDLE_PARAM_FACEBOOK_RESPONSE, this.getFacebookResponse()); // get response usually contain id, name, email etc
        }else{
            mBundle.putString(Constant.BUNDLE_PARAM_USERNAME, getEditTextUsername());
            mBundle.putString(Constant.BUNDLE_PARAM_PASSWORD, getEditTextPassword());
        }

        if(isRegistered){ //if registered, pass lead user ID
            mBundle.putString(Constant.BUNDLE_PARAM_LEAD_USER_ID, leadUserID); // "" or value
        }

        mBundle.putBoolean(Constant.BUNDLE_PARAM_IS_REGISTERED, isRegistered); //true or false
        mBundle.putBoolean(Constant.BUNDLE_PARAM_IS_FACEBOOK_LOGIN, this.isFacebookLogin()); // true or false

        newIntent.putExtras(mBundle);
        getActivity().startActivity(newIntent);
    }

    public void checkLeadFolder(){
        this.mLocalStorage.checkUpdateFolderExist();
    }

    public class HttpAsyncLogin extends AsyncLogin {

        private Context mContext;
        private EnumLoginType loginType;

        public HttpAsyncLogin(Context c, EnumLoginType type){
            this.mContext = c;
            this.loginType = type;
        }

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(this.mContext);
            prog.setMessage("Logging in...");
            prog.show();
        }

        @Override
        protected void onPostExecute(final String result) {
            if (prog != null && prog.isShowing()) {
                prog.dismiss();
            }//end if

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Message msg = mParser.parseJsonToMessage(result);

                    mLogging.debug(TAG, "msg.getMessage() => " + msg.getMessage());

                    if (mParser.getMessageType(msg) == EnumMessageType.SUCCESS){ // success login

                        switch (loginType){
                            case LOGIN_LEAD:
                                setLeadUserID(mParser.parseUserIDFromJson(result));
                                mLogging.debug(TAG, "current login user ID => " + getLeadUserID());
                                // open main activity
                                // pass in current lead user ID
                                displayMainActivity("", getLeadUserID(), true);
                                break;
                            case LOGIN_FACEBOOK:
                                setLeadUserID(mParser.parseUserIDFromJson(result));
                                mLogging.debug(TAG, "current facebook user ID => " + getLeadUserID());
                                // open main activity
                                // pass in current lead user ID
                                setIsFacebookLogin(true);
                                displayMainActivity(getFacebookUserID(), getLeadUserID(), true);
                                break;
                        }


                    }else{ // no such user in database
                        mLogging.debug(TAG, "user is not registered!");

                        switch (loginType) {
                            case LOGIN_LEAD:
                                // confirm cannot go in.. show error
                                break;

                            case LOGIN_FACEBOOK:
                                // go to main activity
                                // create new user profile
                                // set is registered = false
                                setIsFacebookLogin(true);
                                displayMainActivity(getFacebookUserID(), "", false);
                                break;
                        }

                    }
                }

            });

        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentLoginInteraction(Uri uri);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentLoginInteraction(uri);
        }
    }

    private void restructureFacebookLoginButton(){
        this.mLogging.debug(this.TAG, "restructureFacebookLoginButton");

        float fbIconScale = 1.45F;
        Drawable drawable = getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        mFacebookLoginButton.setCompoundDrawables(drawable, null, null, null);
        mFacebookLoginButton.setCompoundDrawablePadding(getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mFacebookLoginButton.setPadding(
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
    }


    private void setProfile(Profile profile) {
        this.mLogging.debug(this.TAG, "setProfile");
        if(!isAdded()){
        //if (userNameView == null || profilePictureView == null || !isAdded()) {
            // Fragment not yet added to the view. So let's store which user was intended
            // for display.
            pendingUpdateForUser = profile;
            return;
        }

        if (profile == null) {
            //profilePictureView.setProfileId(null);
            //userNameView.setText(R.string.greeting_no_user);
        } else {

            AccessToken at = AccessToken.getCurrentAccessToken();
            this.mLogging.debug(this.TAG, "Current Access Token => " + at.getToken());
            this.accessUserGraph(at);



            //this.mLogging.debug(this.TAG, "profile.getId() -> " + profile.getId());
            //this.mLogging.debug(this.TAG, "profile.getFirstName() -> " + profile.getFirstName());
            //this.mLogging.debug(this.TAG, "profile.getLastName() -> " + profile.getLastName());
            //this.mLogging.debug(this.TAG, "profile.getName() -> " + profile.getName());

            //profilePictureView.setProfileId(profile.getId());
            //userNameView.setText(String.format(getString(R.string.greeting_format),
            //        profile.getName()));
        }
    }

    private void accessUserGraph(AccessToken mAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                mAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        mLogging.debug(TAG, "response getRawResponse -> " + response.getRawResponse());

                        //set that user is login using facebook
                        String data = response.getRawResponse();
                        if(!mParser.isStringEmpty(data)){
                            //setIsFacebookLogin(true);


                            // set current facebook user id
                            setFacebookUserID(ParserFacebook.getFacebookID(data));
                            setFacebookResponse(data);
                            runLoginUsingFacebook(getFacebookUserID()); //try login to server

                            //mLogging.debug(TAG, "facebookUserID -> " + facebookUserID);

                            //displayMainActivity(facebookUserID, "");
                        }else{
                            //show error, cannot login to facebook
                            setIsFacebookLogin(false);
                            mLogging.debug(TAG, "unable to get respose data. get error -> " + response.getError().toString());
                        }

                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,middle_name,last_name,picture.type(large){url},address,birthday,email,link,location");
        request.setParameters(parameters);
        request.executeAsync();

    }



    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private boolean hasLoginToFacebook(){
        if(AccessToken.getCurrentAccessToken() != null){
            this.setHasLogin();
            return true;
        }else{
            this.setHasLogout();
            return false;
        }
    }

    private boolean isFacebookLogin() {
        return this.isFacebookLogin;
    }

    private boolean hasLogin(){
        return this.hasLogin;
    }

    private void setHasLogin(){
        this.hasLogin = true;
    }

    private void setHasLogout(){
        this.hasLogin = false;
    }

    private void setIsFacebookLogin(boolean value){
        this.isFacebookLogin = value;
    }

    private void setLeadUserID(String id){
        this.leadUserID = id;
    }

    private void setFacebookUserID(String id){
        this.facebookUserID = id;
    }

    private void setFacebookResponse(String value){
        this.facebookResponse = value;
    }

    private void setIsNotFacebookLogin(){
        this.isFacebookLogin = false;
    }

    private String getLeadUserID(){
        return this.leadUserID;
    }

    private String getFacebookUserID() {
        return this.facebookUserID;
    }

    private String getFacebookResponse() {
        return this.facebookResponse;
    }


}
