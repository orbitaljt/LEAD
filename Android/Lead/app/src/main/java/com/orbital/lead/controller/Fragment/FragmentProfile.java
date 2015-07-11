package com.orbital.lead.controller.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.R;
import com.orbital.lead.controller.CustomApplication;
import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.logic.Logic;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.orbital.lead.model.CurrentLoginUser;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomLogging mLogging;
    private Logic mLogic;
    private Parser mParser;

    private CircularImageView profileImage;
    private ViewAnimator animatorProfileLoading;
    private EditText editTextFirstName;
    private EditText editTextMiddleName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextContact;
    private EditText editTextAddress;
    private TextView textCountry;
    private TextView textBirthday;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private PopupWindow mPopupWindow;

    private Animation inAnim;
    private Animation outAnim;
    private DisplayImageOptions mOptions;

    private String currentFirstName;
    private String currentMiddleName;
    private String currentLastName;
    private String currentBirthday; // in database format
    private String currentEmail;
    private String currentContact;
    private String currentAddress;
    private String currentCountry;

    private int birthYear = 0;
    private int birthMonth = 0;
    private int birthDay = 0;

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

        this.initLogging();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        this.initLogic();
        this.initParser();
        this.initDisplayImageOptions();
        this.initOnDateSetListener();
        this.initProfileImage(rootView);
        this.initAnimatorProfileLoading(rootView);
        this.initEditTextFistName(rootView);
        this.initEditTextMiddleName(rootView);
        this.initEditTextLastName(rootView);
        this.initEditTextEmail(rootView);
        this.initEditTextContact(rootView);
        this.initEditTextAddress(rootView);
        this.initTextCountry(rootView);
        this.initTextBirthday(rootView);

        this.generateCountryList();

        if(CurrentLoginUser.getUser() != null) {

            this.currentFirstName = CurrentLoginUser.getUser().getFirstName();
            this.currentMiddleName = CurrentLoginUser.getUser().getMiddleName();
            this.currentLastName = CurrentLoginUser.getUser().getLastName();
            this.currentBirthday = CurrentLoginUser.getUser().getBirthday(); // in database format
            this.currentEmail = CurrentLoginUser.getUser().getEmail();
            this.currentContact = CurrentLoginUser.getUser().getContact();
            this.currentAddress = CurrentLoginUser.getUser().getAddress();
            this.currentCountry = CurrentLoginUser.getUser().getCountry();


            this.setProfileImage(CurrentLoginUser.getUser().getProfilePicUrl());
            this.setEditTextFirstName(this.currentFirstName);
            this.setEditTextMiddleName(this.currentMiddleName);
            this.setEditTextLastName(this.currentLastName);
            this.setEditTextEmail(this.currentEmail);
            this.setEditTextContact(this.currentContact);
            this.setEditTextAddress(this.currentAddress);
            this.setTextBirthday(this.convertToDisplayDate(this.currentBirthday));
            this.setTextCountry(this.currentCountry);

            mLogging.debug(TAG, "CurrentLoginUser.getUser().getBirthday() => " + CurrentLoginUser.getUser().getBirthday());

            this.birthYear = FormatDate.getYear(CurrentLoginUser.getUser().getBirthday(), FormatDate.DATABASE_FORMAT);
            this.birthMonth = FormatDate.getMonth(CurrentLoginUser.getUser().getBirthday(), FormatDate.DATABASE_FORMAT);
            this.birthDay = FormatDate.getDay(CurrentLoginUser.getUser().getBirthday(), FormatDate.DATABASE_FORMAT);

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

    public String getNewFirstName() {
        return this.editTextFirstName.getText().toString();
    }

    public String getNewMiddleName() {
        return this.editTextMiddleName.getText().toString();
    }

    public String getNewLastName() {
        return this.editTextLastName.getText().toString();
    }

    public String getNewEmail() {
        return this.editTextEmail.getText().toString();
    }

    public String getNewContact() {
        return this.editTextContact.getText().toString();
    }

    public String getNewBirthday() {
        String displayDate = this.textBirthday.getText().toString();
        String databaseDate = FormatDate.parseDate(displayDate, FormatDate.DISPLAY_DATE_TO_DATABASE_DATE, FormatDate.DATABASE_FORMAT);

        return databaseDate;
    }

    public String getNewAddress() {
        return this.editTextAddress.getText().toString();
    }

    public String getNewCountry() {
        return this.textCountry.getText().toString();
    }


    public boolean hasChanges(){
        mLogging.debug(TAG, this.currentFirstName + " <=> " + getNewFirstName());
        mLogging.debug(TAG, currentMiddleName + " <=> " + getNewMiddleName());
        mLogging.debug(TAG, currentLastName + " <=> " + getNewLastName());
        mLogging.debug(TAG, currentBirthday + " <=> " + getNewBirthday());
        mLogging.debug(TAG, currentEmail + " <=> " + getNewEmail());
        mLogging.debug(TAG, currentContact + " <=> " + getNewContact());
        mLogging.debug(TAG, currentAddress + " <=> " + getNewAddress());
        mLogging.debug(TAG, currentCountry + " <=> " + getNewCountry());

        if (this.mParser.compareBothString(this.currentFirstName, this.getNewFirstName())
            && this.mParser.compareBothString(this.currentMiddleName, this.getNewMiddleName())
            && this.mParser.compareBothString(this.currentLastName, this.getNewLastName())
            && this.mParser.compareBothString(this.currentBirthday, this.getNewBirthday())
            && this.mParser.compareBothString(this.currentEmail, this.getNewEmail())
            && this.mParser.compareBothString(this.currentContact, this.getNewContact())
            && this.mParser.compareBothString(this.currentAddress, this.getNewAddress())
            && this.mParser.compareBothString(this.currentCountry, this.getNewCountry())
                ){

            return false; // no changes as all are the same
        }
        return true;
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    private void initLogic() {
        this.mLogic = Logic.getInstance();
    }

    private void initParser() {
        this.mParser =  Parser.getInstance();
    }

    private void initProfileImage(View v) {
        this.profileImage = (CircularImageView) v.findViewById(R.id.user_profile_image);
    }

    private void initAnimatorProfileLoading(View v) {
        this.animatorProfileLoading = (ViewAnimator) v.findViewById(R.id.animator_profile_image_loading);
    }

    private void initEditTextFistName(View v) {
        this.editTextFirstName = (EditText) v.findViewById(R.id.editTextFirstName);
    }

    private void initEditTextMiddleName(View v) {
        this.editTextMiddleName = (EditText) v.findViewById(R.id.editTextMiddleName);
    }

    private void initEditTextLastName(View v) {
        this.editTextLastName = (EditText) v.findViewById(R.id.editTextLastName);
    }

    private void initEditTextEmail(View v) {
        this.editTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
    }

    private void initEditTextContact(View v) {
        this.editTextContact = (EditText) v.findViewById(R.id.editTextContact);
    }

    private void initEditTextAddress(View v) {
        this.editTextAddress = (EditText) v.findViewById(R.id.editTextAddress);
    }

    private void initTextCountry(View v) {
        this.textCountry = (TextView) v.findViewById(R.id.textCountry);
        this.textCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogic.showCountryListPopUpMenu(getActivity(), v, generateCountryList());
            }
        });

    }

    private void initTextBirthday(View v) {
        this.textBirthday = (TextView) v.findViewById(R.id.textBirthday);
        this.textBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // month in java starts from 0
                showDatePickerDialog(birthYear, birthMonth, birthDay);
            }
        });
    }

    private void initDisplayImageOptions() {
        this.mOptions = CustomApplication.getDisplayImageOptions();
    }

    private void setProfileImage(String url) {
        this.getAnimatorProfileLoading().setDisplayedChild(1);

        ImageLoader.getInstance()
                .displayImage(url, this.getProfileImage(), mOptions,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                getAnimatorProfileLoading().setDisplayedChild(2);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                getAnimatorProfileLoading().setDisplayedChild(0);
                            }
                        },
                        new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            }
                        });
    }


    private void setEditTextFirstName(String val) {
        this.editTextFirstName.setText(val);
    }

    private void setEditTextMiddleName(String val) {
        this.editTextMiddleName.setText(val);
    }

    private void setEditTextLastName(String val) {
        this.editTextLastName.setText(val);
    }

    private void setEditTextEmail(String val) {
        this.editTextEmail.setText(val);
    }

    private void setEditTextContact(String val) {
        this.editTextContact.setText(val);
    }

    private void setEditTextAddress(String val) {
        this.editTextAddress.setText(val);
    }

    private void setTextBirthday(String val) {
        this.textBirthday.setText(val);
    }

    private void setTextCountry(String val) {
        this.textCountry.setText(val);
    }

    private void setBirthday(int newYear, int newMonth, int newDay) {
        if(this.birthYear != newYear){
            this.birthYear = newYear;
        }

        if(this.birthMonth != newMonth){
            this.birthMonth = newMonth;
        }

        if(this.birthDay != newDay){
            this.birthDay = newDay;
        }
    }


    private CircularImageView getProfileImage() {
        return this.profileImage;
    }

    private ViewAnimator getAnimatorProfileLoading() {
        return this.animatorProfileLoading;
    }

    private ArrayList<String> generateCountryList(){
        ArrayList<String> countries = new ArrayList<String>();

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);

        return countries;
    }


    /*=============== DIALOGS ==========*/
    private void initOnDateSetListener() {
        this.datePickerListener =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                mLogging.debug(TAG, "onDateSet monthOfYear => " + monthOfYear);

                //monthOfYear starts from 0
                String databaseFormatDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                setBirthday(year, monthOfYear, dayOfMonth);
                setTextBirthday(convertToDisplayDate(databaseFormatDate));
            }
        };
    }

    private void showDatePickerDialog(int year, int month, int day){
        mLogging.debug(TAG, "showDatePickerDialog month => " + month);
        this.datePickerDialog = new DatePickerDialog(getActivity(),
                this.getDatePickerListener(),
                year,
                month,
                day);
        this.datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getDatePickerListener(){
        return this.datePickerListener;
    }


    /*
    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window_layout_1, null, false);
        mPopupWindow = new PopupWindow(layout, 200, 200, true);
        int x = Math.round(textCountry.getX());
        int y = Math.round(textCountry.getY());

        Rect location = mLogic.locateView(textCountry);

        mPopupWindow.showAtLocation(layout, Gravity.TOP|Gravity.LEFT,
                location.left, location.top);

    }
    */

    private String convertToDisplayDate(String oldDate) {
        return FormatDate.parseDate(oldDate, FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
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
