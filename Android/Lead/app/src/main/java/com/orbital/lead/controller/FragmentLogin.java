package com.orbital.lead.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.orbital.lead.R;
import com.orbital.lead.logic.CustomLogging;

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

    private LoginActivity mLoginActivity;
    // Facebook
    // Controls
    private LoginButton mFacebookLoginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private Profile pendingUpdateForUser;

    private boolean hasLogin;

    private OnFragmentInteractionListener mListener;

    private CustomLogging mLogging;
    private final String TAG_FRAGMENT_LOGIN = this.getClass().getSimpleName();

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
        this.mLogging.debug(TAG_FRAGMENT_LOGIN, "onCreate");

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
        this.mLogging.debug(TAG_FRAGMENT_LOGIN, "onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);



        //this.initFacebookCallbackManager();
        this.initFacebookLoginButton(rootView);
        //this.initProfileTracker();
        //this.initAccessTokenTrack();
        //Profile.fetchProfileForCurrentAccessToken();
        //this.setProfile(Profile.getCurrentProfile());

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
        if(activity instanceof LoginActivity){
            this.initLoginActivity((LoginActivity) activity);
        }

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
        this.mLogging.debug(TAG_FRAGMENT_LOGIN, "onActivityResult");
        this.callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.profileTracker.stopTracking();
        this.accessTokenTracker.startTracking();
    }


    public void initFacebook(){
        FacebookSdk.sdkInitialize(getLoginActivity().getApplicationContext());
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

        this.mFacebookLoginButton = (LoginButton) v.findViewById(R.id.btnLoginWithFacebook);
        this.mFacebookLoginButton.setReadPermissions(mPermission);
        this.mFacebookLoginButton.setFragment(this);

        // Callback registration
        this.mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLogging.debug(TAG_FRAGMENT_LOGIN, "onSuccess");
               // mLogging.debug(TAG_FRAGMENT_LOGIN, "access token -> " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                mLogging.debug(TAG_FRAGMENT_LOGIN, "registerCallback onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                mLogging.debug(TAG_FRAGMENT_LOGIN, "registerCallback onError -> " + exception.getMessage().toString());
            }
        });

    }

    public void initProfileTracker() {
        this.profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                mLogging.debug(TAG_FRAGMENT_LOGIN, "onCurrentProfileChanged");
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
                mLogging.debug(TAG_FRAGMENT_LOGIN, "onCurrentAccessTokenChanged currentAccessToken -> " + currentAccessToken );
                Profile.fetchProfileForCurrentAccessToken();
            }
        };
    }


    public void initLoginActivity(LoginActivity activity){
        this.mLoginActivity = activity;
    }

    public LoginActivity getLoginActivity(){
        return this.mLoginActivity;
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
        this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "restructureFacebookLoginButton");

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
        this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "setProfile");
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
            this.accessUserGraph(at);



            //this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "profile.getId() -> " + profile.getId());
            //this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "profile.getFirstName() -> " + profile.getFirstName());
            //this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "profile.getLastName() -> " + profile.getLastName());
            //this.mLogging.debug(this.TAG_FRAGMENT_LOGIN, "profile.getName() -> " + profile.getName());

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
                        mLogging.debug(TAG_FRAGMENT_LOGIN, "response -> " + response.toString());

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture{url},address,birthday,email,link");
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

    private boolean getHasLogin(){
        return this.hasLogin;
    }

    private void setHasLogin(){
        this.hasLogin = true;
    }

    private void setHasLogout(){
        this.hasLogin = false;
    }

}
