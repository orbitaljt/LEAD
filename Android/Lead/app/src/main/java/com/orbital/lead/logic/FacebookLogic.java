package com.orbital.lead.logic;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.controller.Activity.PictureActivity;
import com.orbital.lead.model.AlbumList;

import org.json.JSONObject;

/**
 * Created by joseph on 6/7/2015.
 */
public class FacebookLogic {

    private static FacebookLogic mFacebookLogic = new FacebookLogic();

    private final String TAG = this.getClass().getSimpleName();
    private CustomLogging mLogging;
    private Parser mParser;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private Profile pendingUpdateForUser;

    private boolean isFacebookLogin = false;

    private FacebookLogic() {}


    public static FacebookLogic getInstance() {
        mFacebookLogic.initLogging();
        mFacebookLogic.initParser();
        return mFacebookLogic;
    }

    public void setIsFacebookLogin(boolean val) {
        this.isFacebookLogin = val;
    }

    public boolean getIsFacebookLogin() {
        return this.isFacebookLogin;
    }

    public String getCurrentFacebookAccessTokenString() {
        try {
            return AccessToken.getCurrentAccessToken().getToken();
        }catch(NullPointerException e){
            return "";
        }
    }

    public AccessToken getCurrentFacebookAccessToken() {
        try {
            return AccessToken.getCurrentAccessToken();
        }catch(NullPointerException e){
            return null;
        }
    }

    public AlbumList getFacebookAllAlbum(final Context context, String param) {
        mLogging.debug(TAG, "getFacebookAllAlbum");

        boolean finishLoop = false;

        /*
        while(!finishLoop){
        }
        */
        GraphRequest request = GraphRequest.newMeRequest(
                this.getCurrentFacebookAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        mLogging.debug(TAG, "response getRawResponse -> " + response.getRawResponse());

                        //set that user is login using facebook
                        String data = response.getRawResponse();
                        if(!mParser.isStringEmpty(data)){

                            AlbumList facebookAlbumList = ParserFacebook.getFacebookAlbumList(data);

                            if(context instanceof PictureActivity) {
                                if(facebookAlbumList != null){
                                    ((PictureActivity) context).updateFragmentAlbumGridAdapter(facebookAlbumList);
                                }else{
                                    mLogging.debug(TAG, "facebookAlbumList is null");
                                }

                            }

                        }else{
                            mLogging.debug(TAG, "unable to get respose data. get error -> " + response.getError().toString());
                        }

                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", param);
        request.setParameters(parameters);
        request.executeAsync();

        return null;

    }







    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initParser() {
        this.mParser = Parser.getInstance();
    }


}
