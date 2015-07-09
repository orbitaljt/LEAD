package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.orbital.lead.model.CurrentLoginUser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomLogging mLogging;
    private Logic mLogic;

    private CircularImageView profileImage;
    private ViewAnimator animatorProfileLoading;
    private Animation inAnim;
    private Animation outAnim;
    private DisplayImageOptions mOptions;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.initLogging();

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        this.initLogic();
        this.initDisplayImageOptions();
        this.initProfileImage(rootView);
        this.initAnimatorProfileLoading(rootView);

        if(CurrentLoginUser.getUser() != null) {
            this.setProfileImage(CurrentLoginUser.getUser().getProfilePicUrl());
        }

        return rootView;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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


    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }

    private void initProfileImage(View v) {
        this.profileImage = (CircularImageView) v.findViewById(R.id.user_profile_image);
    }

    private void initAnimatorProfileLoading(View v) {
        this.animatorProfileLoading = (ViewAnimator) v.findViewById(R.id.animator_profile_image_loading);
    }

    private void initDisplayImageOptions() {
        this.mOptions = CustomApplication.getDisplayImageOptions();
    }

    private void setProfileImage(String url) {
        this.getAnimatorProfileLoading().setDisplayedChild(1);

        ImageLoader.getInstance()
                .displayImage(url, this.getProfileImage(), mOptions,
                        new SimpleImageLoadingListener(){
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                //holder.progressBar.setProgress(0);
                                //holder.progressBar.setVisibility(View.VISIBLE);
                                //mLogging.debug(TAG, "onLoadingStarted");
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                //holder.progressBar.setVisibility(View.GONE);
                                //mLogging.debug(TAG, "onLoadingFailed");
                                getAnimatorProfileLoading().setDisplayedChild(2);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                //holder.progressBar.setVisibility(View.GONE);
                                // mLogging.debug(TAG, "onLoadingComplete");
                                getAnimatorProfileLoading().setDisplayedChild(0);
                            }
                        },
                        new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                //mLogging.debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                            }
                        });
    }

    private CircularImageView getProfileImage() {
        return this.profileImage;
    }

    private ViewAnimator getAnimatorProfileLoading() {
        return this.animatorProfileLoading;
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
        public void onFragmentProfileInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentProfileInteraction(uri);
        }
    }

}
