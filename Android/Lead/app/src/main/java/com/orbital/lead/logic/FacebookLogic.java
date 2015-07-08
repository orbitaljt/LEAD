package com.orbital.lead.logic;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.Parser.ParserFacebook;
import com.orbital.lead.controller.Activity.PictureActivity;
import com.orbital.lead.logic.Asynchronous.AsyncFacebookAllAlbum;
import com.orbital.lead.logic.Asynchronous.AsyncFacebookGetAlbumPictures;
import com.orbital.lead.model.AlbumList;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumFacebookQueryType;
import com.orbital.lead.model.PictureList;

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

    private AlbumList facebookAlbumList;
    private PictureList facebookPictureList;
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

    public void sendGraphRequest(Context context, EnumFacebookQueryType type, String albumID) {
        switch(type){
            case GET_ALL_ALBUM:
                this.initFacebookAlbumList();
                this.getFacebookAllAlbum(context, Constant.FACEBOOK_GRAPH_ALBUM_QUERY);
                break;
            case GET_ALL_ALBUM_PICTURES:
                this.initFacebookPictureList();
                this.getFacebookAlbumPictures(context, albumID, Constant.FACEBOOK_GRAPH_ALBUM_PICTURES_QUERY);
                break;
        }
    }

    private void getFacebookAllAlbum(final Context context, String param) {
        mLogging.debug(TAG, "getFacebookAllAlbum");

        GraphRequest request = GraphRequest.newMeRequest(
                this.getCurrentFacebookAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        mLogging.debug(TAG, "response getRawResponse -> " + response.getRawResponse());

                        String data = response.getRawResponse();
                        if(!mParser.isStringEmpty(data)){

                            AlbumList currentAlbumList = ParserFacebook.getFacebookAlbumList(data);
                            String pageNextUrl = ParserFacebook.getFacebookAlbumListNextPageUrl(data); // include access token and after id

                            getFacebookAlbumList().addAlbum(currentAlbumList);

                            mLogging.debug(TAG, "pageNextUrl -> " + pageNextUrl);

                            if(mParser.isStringEmpty(pageNextUrl)){ //means no next page
                                mLogging.debug(TAG, "no next page!!!");
                                returnFacebookAlbumListToContext(context);

                            }else{
                                mLogging.debug(TAG, "go to next page........");

                                new HttpAsyncFacebookAllAbumNextPage(context, EnumFacebookQueryType.GET_ALL_ALBUM)
                                        .execute(EnumFacebookQueryType.GET_ALL_ALBUM.toString(),
                                                pageNextUrl);

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

    }

    private void getFacebookAlbumPictures(final Context context, final String albumID, String param) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumID + "/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        mLogging.debug(TAG, "getFacebookAlbumPictures getRawResponse -> " + response.getRawResponse());
                        String data = response.getRawResponse();

                        if(!mParser.isStringEmpty(data)) {

                            PictureList list = ParserFacebook.getFacebookPictureList(albumID, data);
                            String pageNextUrl = ParserFacebook.getFacebookPictureListNextPageUrl(data); // include access token and after id

                            getFacebookPictureList().addPicture(list);

                            mLogging.debug(TAG, "pageNextUrl -> " + pageNextUrl);

                            if (mParser.isStringEmpty(pageNextUrl)) { //means no next page
                                mLogging.debug(TAG, "no next page!!!");
                                returnFacebookPictureListToContext(context);

                            } else {
                                mLogging.debug(TAG, "go to next page........");

                                new HttpAsyncFacebookGetAlbumPicturesNextPage(context, albumID)
                                        .execute(pageNextUrl);

                            }

                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", param);
        request.setParameters(parameters);
        request.executeAsync();

    }







    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initParser() {
        this.mParser = Parser.getInstance();
    }

    private void initFacebookAlbumList() {
        this.facebookAlbumList = new AlbumList();
    }

    private void initFacebookPictureList() {
        this.facebookPictureList = new PictureList();
    }

    private AlbumList getFacebookAlbumList() {
        return this.facebookAlbumList;
    }

    private PictureList getFacebookPictureList() {
        return this.facebookPictureList;
    }

    private void returnFacebookAlbumListToContext(Context context) {
        mLogging.debug(TAG, "returnFacebookAlbumListToContext");
        if(context instanceof PictureActivity) {

            if(getFacebookAlbumList() != null){
                mLogging.debug(TAG, "updateFragmentAlbumGridAdapter !!!!");
                ((PictureActivity) context).updateFragmentAlbumGridAdapter(getFacebookAlbumList());
            }else {
                mLogging.debug(TAG, "facebookAlbumList is null");

            }

        }//end if
    }

    private void returnFacebookPictureListToContext(Context context) {
        mLogging.debug(TAG, "returnFacebookPictureListToContext");
        if(context instanceof PictureActivity) {

            if(getFacebookPictureList() != null){
                mLogging.debug(TAG, "updateFragmentAlbumGridAdapter !!!!");
                ((PictureActivity) context).updateFragmentPicturesGridAdapter(getFacebookPictureList());
            }else {
                mLogging.debug(TAG, "facebookAlbumList is null");

            }

        }//end if
    }


    private class HttpAsyncFacebookAllAbumNextPage extends AsyncFacebookAllAlbum {
        private Context mContext;
        private EnumFacebookQueryType mType;

        public HttpAsyncFacebookAllAbumNextPage(Context c, EnumFacebookQueryType type){
            this.mContext = c;
            this.mType = type;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            mLogging.debug(TAG, "HttpAsyncFacebookAllAbumNextPage onPostExecute result => " + result);

            //if(mContext instanceof MainActivity){ // calling from mainactivity

            switch (this.mType) {
                case GET_ALL_ALBUM:

                    if(mParser.isStringEmpty(result)) { // result
                        mLogging.debug(TAG, "HttpAsyncFacebookAllAbumNextPage no result!!!!!!");
                        returnFacebookAlbumListToContext(this.mContext);
                    }else{
                        AlbumList currentAlbumList = ParserFacebook.getFacebookNextPageAlbumList(result);
                        String pageNextUrl = ParserFacebook.getFacebookNextPageAlbumListNextPageUrl(result);

                        getFacebookAlbumList().addAlbum(currentAlbumList);

                        mLogging.debug(TAG, "HttpAsyncFacebookAllAbumNextPage pageNextUrl -> " + pageNextUrl);

                        if(mParser.isStringEmpty(pageNextUrl)){ //means no next page
                            mLogging.debug(TAG, "HttpAsyncFacebookAllAbumNextPage no next page!!!");
                            returnFacebookAlbumListToContext(this.mContext);

                        }else{
                            mLogging.debug(TAG, "HttpAsyncFacebookAllAbumNextPage go to next page........");
                            new HttpAsyncFacebookAllAbumNextPage(this.mContext, EnumFacebookQueryType.GET_ALL_ALBUM)
                                    .execute(EnumFacebookQueryType.GET_ALL_ALBUM.toString(),
                                            pageNextUrl);
                        }
                    }

                    break;
            }
        }//end onPostExecute

    }//end HttpAsyncFacebookAllAbumNextPage

    private class HttpAsyncFacebookGetAlbumPicturesNextPage extends AsyncFacebookGetAlbumPictures{
        private Context mContext;
        private String albumID;
        public HttpAsyncFacebookGetAlbumPicturesNextPage(Context context, String albumID){
            this.mContext = context;
            this.albumID = albumID;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            mLogging.debug(TAG, "HttpAsyncFacebookGetAlbumPicturesNextPage onPostExecute result => " + result);

            if(mParser.isStringEmpty(result)) { // result
                mLogging.debug(TAG, "HttpAsyncFacebookGetAlbumPicturesNextPage no result!!!!!!");

                returnFacebookPictureListToContext(this.mContext);
            }else {

                PictureList list = ParserFacebook.getFacebookPictureList(albumID, result);
                String pageNextUrl = ParserFacebook.getFacebookPictureListNextPageUrl(result); // include access token and after id

                getFacebookPictureList().addPicture(list);

                if (mParser.isStringEmpty(pageNextUrl)) { //means no next page
                    mLogging.debug(TAG, "HttpAsyncFacebookGetAlbumPicturesNextPage no next page!!!");

                    returnFacebookPictureListToContext(this.mContext);

                } else {
                    mLogging.debug(TAG, "HttpAsyncFacebookGetAlbumPicturesNextPage go to next page........");
                    new HttpAsyncFacebookGetAlbumPicturesNextPage(this.mContext, this.albumID)
                            .execute(pageNextUrl);
                }
            }


        }//end onPostExecute

    }//end HttpAsyncFacebookAllAbumNextPage

}
