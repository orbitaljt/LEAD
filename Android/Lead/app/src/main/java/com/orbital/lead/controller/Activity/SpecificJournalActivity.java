package com.orbital.lead.controller.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.orbital.lead.R;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.JournalList;
import com.orbital.lead.widget.WideImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SpecificJournalActivity extends BaseActivity implements JournalReceiver.Receiver {
    private final String TAG = this.getClass().getSimpleName();

    private View mToolbarView;
    private WideImageView mImageJournalCover;
    private ViewAnimator mAnimator;
    private TextView mTextDayDigit; //01-31
    private TextView mTextDayName; // Monday - Sunday
    private TextView mTextMonthYear; // January 1990

    private JournalReceiver mJournalReceiver;

    private String journalID;
    private String journalCoverUrl;
    private Journal mJournal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_specific_journal_layout, getBaseFrameLayout());

        this.initToolbar();
        this.pushToolbarToActionbar();
        this.restoreActionBar();
        this.setToolbarTitle(Constant.TITLE_SPECIFIC_JOURNAL);
        this.restoreDrawerHeaderValues();

        this.initViewAnimator();
        this.initImageJournalCover();
        this.initTextDayDigit();
        this.initTextDayName();
        this.initTextMonthYear();

        this.initJournalReceiver();

        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {
            //this.journalID = getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_ID, "");
            //this.journalCoverUrl = getBundleExtra.getString(Constant.BUNDLE_PARAM_JOURNAL_IMAGE_URL, "");

            //this.getUserSpecificJournal(this.getJournalID());
            this.setJournal((Journal) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_JOURNAL));
            this.setImageJournalCover(this.getParser().createPictureCoverUrl(this.getJournal().getPictureCoverID(),
                                    this.getJournal().getPictureCoverType().toString(),
                                    CurrentLoginUser.getUser().getUserID()));

        } else {
            getCustomLogging().debug(TAG, "No bundle extra from getIntent()");
        }

        //this.setImageJournalCover(this.getJournalCoverUrl());

    }

    public void initToolbar() {
        this.mToolbarView = findViewById(R.id.custom_specific_journal_toolbar);
    }

    public View getToolbar() {
        return this.mToolbarView;
    }

    public void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }

    /*===================== Journal Intent Service & Receiver ================================*/
    public void initJournalReceiver(){
        mJournalReceiver = new JournalReceiver(new Handler());
        mJournalReceiver.setReceiver(this);
    }

    public JournalReceiver getJournalReceiver(){
        if(this.mJournalReceiver == null){
            this.initJournalReceiver();
        }
        return this.mJournalReceiver;
    }

    /*
    public void getUserSpecificJournal(String journalID){
        this.getLogic().getUserSpecificJournal(this, journalID);
    }
    */

    private void restoreDrawerHeaderValues() {
        this.getNavigationDrawerFragment().setImageUserProfile(CurrentLoginUser.getUser().getProfilePicUrl());
        this.getNavigationDrawerFragment().setTextUserName(CurrentLoginUser.getUser().getFirstName(),
                CurrentLoginUser.getUser().getMiddleName(),
                CurrentLoginUser.getUser().getLastName());
        this.getNavigationDrawerFragment().setmTextUserEmail(CurrentLoginUser.getUser().getEmail());
    }

    private void initImageJournalCover() {
        this.mImageJournalCover = (WideImageView) findViewById(R.id.image_journal_cover);
        /*
        this.mImageJournalCover.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                getCustomLogging().debug(TAG, "right => " + right);
                getCustomLogging().debug(TAG, "left => " + left);
                getCustomLogging().debug(TAG, "top => " + top);
                getCustomLogging().debug(TAG, "bottom => " + bottom);
            }
        });
        */
    }

    private void initViewAnimator() {
        this.mAnimator = (ViewAnimator) findViewById(R.id.animator);
    }

    private void initTextDayDigit() {
        this.mTextDayDigit = (TextView) findViewById(R.id.text_day_digit);
    }

    private void initTextDayName() {
        this.mTextDayDigit = (TextView) findViewById(R.id.text_day_name);
    }

    private void initTextMonthYear() {
        this.mTextDayDigit = (TextView) findViewById(R.id.text_month_year);
    }

    private void setImageJournalCover(String url) {
        this.getViewAnimator().setDisplayedChild(1);
        //.error(R.drawable.image_blank_picture)
        //.transform(new RoundedTransformation(10, 0))
        if(!this.getParser().isStringEmpty(url)){
            Picasso.with(this)
                    .load(url)
                    .noFade()
                    .into(getImageJournalCover(), new Callback() {
                        @Override
                        public void onSuccess() {
                            getCustomLogging().debug(TAG, "setImageJournalCover -> Picasso onSuccess");
                            mAnimator.setDisplayedChild(0); // show actual image
                        }

                        @Override
                        public void onError() {
                            getCustomLogging().debug(TAG, "setImageJournalCover -> Picasso onError");
                            mAnimator.setDisplayedChild(2);
                        }
                    });
        }else{
            getCustomLogging().debug(TAG, "setImageJournalCover -> Picture URL is empty");
            mAnimator.setDisplayedChild(2);
        }
    }

    private void setTextDayDigit(String val) {
        this.mTextDayDigit.setText(val);
    }

    private void setTextDayName(String val) {
        this.mTextDayName.setText(val);
    }

    private void setTextMonthYear(String val) {
        this.mTextMonthYear.setText(val);
    }

    private void setJournal(Journal j){
        this.mJournal = j;
    }

    private Journal getJournal() {
        return this.mJournal;
    }
    /*
    private String getJournalID() {
        return this.journalID;
    }

    private String getJournalCoverUrl() {
        return this.journalCoverUrl;
    }
    */
    private ViewAnimator getViewAnimator() {
        return this.mAnimator;
    }

    private WideImageView getImageJournalCover() {
        return this.mImageJournalCover;
    }



    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumJournalServiceType type = null;
        String jsonResult = "";

        switch (resultCode) {
            case JournalService.STATUS_RUNNING:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_RUNNING");
                break;

            case JournalService.STATUS_FINISHED:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_FINISHED");
                type = (EnumJournalServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_SPECIFIC_JOURNAL:
                        //jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        //this.getCustomLogging().debug(TAG, "onReceiveResult GET_SPECIFIC_JOURNAL -> jsonResult => " + jsonResult);
                        //JournalList list = this.getJournalListFromJson(jsonResult);

                        break;

                }

                break;

            case JournalService.STATUS_ERROR:
                this.getCustomLogging().debug(TAG, "JournalService.STATUS_ERROR");
                break;
        }
    }

}