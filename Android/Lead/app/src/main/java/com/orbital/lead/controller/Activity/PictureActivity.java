package com.orbital.lead.controller.Activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentAlbum;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.Picture;
import com.orbital.lead.model.PictureList;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureActivity extends BaseActivity implements FragmentAlbum.OnFragmentInteractionListener{
    private final String TAG = this.getClass().getSimpleName();

    public static final String OPEN_FRAGMENT_LIST_PICTURES = "0"; // fragment that shows a list of all albums
    public static final String OPEN_FRAGMENT_ALBUM = "1"; // fragment that shows all pictures of an album

    private String openType;
    private Album mAlbum;
    private ArrayList<Picture> mPictureList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragmentAlbum mFragmentAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getCustomLogging().debug(TAG, "onCreate");

        getLayoutInflater().inflate(R.layout.activity_picture, getBaseFrameLayout());

        this.initFragmentManager();

        Bundle getBundleExtra = getIntent().getExtras();

        if (getBundleExtra != null) {
            if(getBundleExtra.getString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE) != null){
                this.setOpenFragmentType(getBundleExtra.getString(Constant.BUNDLE_PARAM_OPEN_FRAGMENT_TYPE));
            }

            if(getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM)!= null) {
                this.setAlbum((Album) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_ALBUM));
            }

            if(getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST) != null) {
                this.setPictureList((ArrayList) getBundleExtra.getParcelableArrayList(Constant.BUNDLE_PARAM_PICTURE_LIST));
            }


            switch(this.getOpenFragmentType()){

                case PictureActivity.OPEN_FRAGMENT_ALBUM:
                    this.displayFragmentAlbum();
                    break;

                case PictureActivity.OPEN_FRAGMENT_LIST_PICTURES:

                    break;

            }

        }
    }


    private void initFragmentManager(){

        this.fragmentManager = getSupportFragmentManager();
        /*
        this.fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    getNavigationDrawerToggle().setDrawerIndicatorEnabled(false);
                } else {
                    getNavigationDrawerToggle().setDrawerIndicatorEnabled(true);
                    getNavigationDrawerToggle().setToolbarNavigationClickListener(getNavigationDrawerFragment().getOriginalToolbarNavigationClickListener());
                }
            }
        });
        */
    }

    private void displayFragmentAlbum(){
        this.getCustomLogging().debug(TAG, "displayFragmentAlbum");
        this.mFragmentAlbum = FragmentAlbum.newInstance(this, this.getAlbum(), this.getPictureList());
        this.replaceFragment(this.mFragmentAlbum, Constant.FRAGMENT_ALBUM);
    }

    private void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .replace(R.id.container_fragment, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }

    private String getOpenFragmentType() {
        return this.openType;
    }

    private Album getAlbum() {
        return this.mAlbum;
    }

    private ArrayList<Picture> getPictureList() {
        return this.mPictureList;
    }

    private void setOpenFragmentType(String type) {
        this.openType = type;
    }

    private void setAlbum(Album a) {
        this.mAlbum = a;
    }

    private void setPictureList(ArrayList<Picture> pList) {
        this.mPictureList = pList;
    }


    @Override
    public void onFragmentAlbumInteraction(Uri uri) {

    }
}
