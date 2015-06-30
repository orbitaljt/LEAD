package com.orbital.lead.controller.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.FormatTime;
import com.orbital.lead.R;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.controller.Service.PictureReceiver;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumPictureServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.widget.WideImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SpecificJournalActivity extends BaseActivity implements PictureReceiver.Receiver {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private View mToolbarView;
    private WideImageView mImageJournalCover;
    private ViewAnimator mAnimator;
    private TextView mTextPictureCount;
    private TextView mTextDayDigit; //01-31
    private TextView mTextDayName; // Monday - Sunday
    private TextView mTextMonthYear; // January 1990
    private TextView mTextTime;
    private TextView mTextTitle;
    private TextView mTextContent;

    private PictureReceiver mPictureReceiver;

    private Journal mJournal;
    private Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_specific_journal_layout, getBaseFrameLayout());

        this.setContext(this);
        this.initToolbar();
        this.pushToolbarToActionbar();
        //this.restoreActionBar();
        this.restoreCustomActionbar();
        this.setToolbarTitle(Constant.TITLE_SPECIFIC_JOURNAL);
        this.restoreDrawerHeaderValues();

        this.initViewAnimator();
        this.initImageJournalCover();
        this.initTextPictureCount();
        this.initTextDayDigit();
        this.initTextDayName();
        this.initTextMonthYear();
        this.initTextTime();
        this.initTextTitle();
        this.initTextContent();
        this.initPictureReceiver();

        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {

            this.setJournal((Journal) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_JOURNAL));
            this.setImageJournalCover(this.getParser().createPictureCoverUrl(this.getJournal().getPictureCoverID(),
                                    this.getJournal().getPictureCoverType().toString(),
                                    CurrentLoginUser.getUser().getUserID()));



            String time = FormatTime.parseTime(this.getJournal().getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME); // HH:mm
            String date = FormatDate.parseDate(this.getJournal().getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FULL_FORMAT); // dd MMMM yyyy cccc
            String[] dates = date.split(" ");

            this.setTextDayDigit(dates[0]);
            this.setTextMonthYear(dates[1] + " " + dates[2]);
            this.setTextDayName(dates[3]);
            this.setTextTime(time);
            this.setTextTitle(this.getJournal().getTitle());
            this.setTextContent(this.getJournal().getContent());

            // run picture service first
            this.getLogic().getUserSpecificAlbum(this, this.getJournal().getPictureAlbumID());

        } else {
            getCustomLogging().debug(TAG, "No bundle extra from getIntent()");
        }

        //this.setImageJournalCover(this.getJournalCoverUrl());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_specific_journal, menu);
            this.restoreCustomActionbar();
            //this.restoreActionBar();
        }
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            getCustomLogging().debug(TAG, "onOptionsItemSelected");
            onBackPressed();
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    private void restoreCustomActionbar(){
        // disable the home button and onClick to open navigation drawer
        // enable the back arrow button
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }


    public void initToolbar() {
        this.mToolbarView = findViewById(R.id.custom_specific_journal_toolbar);
        ((Toolbar) this.mToolbarView).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                getCustomLogging().debug(TAG, "mToolbarView setNavigationOnClickListener onClick");
            }
        });
    }

    public View getToolbar() {
        return this.mToolbarView;
    }

    public void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }


    /*===================== Journal Intent Service & Receiver ================================*/
    public void initPictureReceiver(){
        mPictureReceiver = new PictureReceiver(new Handler());
        mPictureReceiver.setReceiver(this);
    }

    public PictureReceiver getJournalReceiver(){
        if(this.mPictureReceiver == null){
            this.initPictureReceiver();
        }
        return this.mPictureReceiver;
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
    }

    private void initViewAnimator() {
        this.mAnimator = (ViewAnimator) findViewById(R.id.animator);
    }

    private void initTextPictureCount() {
        this.mTextPictureCount = (TextView) findViewById(R.id.text_photo_count);
        this.mTextPictureCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomLogging().debug(TAG, "mTextPictureCount onClick");
                if(getAlbum() != null){
                    getLogic().displayPictureActivity(getContext(), PictureActivity.OPEN_FRAGMENT_ALBUM, getAlbum(), getAlbum().getPictureList().getList());
                }
            }
        });
    }

    private void initTextDayDigit() {
        this.mTextDayDigit = (TextView) findViewById(R.id.text_day_digit);
    }

    private void initTextDayName() {
        this.mTextDayName = (TextView) findViewById(R.id.text_day_name);
    }

    private void initTextMonthYear() {
        this.mTextMonthYear = (TextView) findViewById(R.id.text_month_year);
    }

    private void initTextTime() {
        this.mTextTime = (TextView) findViewById(R.id.text_time);
    }

    private void initTextTitle() {
        this.mTextTitle = (TextView) findViewById(R.id.text_journal_title);
    }

    private void initTextContent(){
        this.mTextContent = (TextView) findViewById(R.id.text_journal_content);
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private void setImageJournalCover(String url) {
        this.getViewAnimator().setDisplayedChild(1);
        //.error(R.drawable.image_blank_picture)
        //.transform(new RoundedTransformation(10, 0))
        if(!this.getParser().isStringEmpty(url)){
            /*
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
            */
            ImageLoader.getInstance()
                    .displayImage(url, this.getImageJournalCover(), this.getCustomApplication().getDisplayImageOptions(),
                            new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    //holder.progressBar.setProgress(0);
                                    //holder.progressBar.setVisibility(View.VISIBLE);
                                    getCustomLogging().debug(TAG, "onLoadingStarted");
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    getCustomLogging().debug(TAG, "onLoadingFailed");
                                    mAnimator.setDisplayedChild(2);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    //holder.progressBar.setVisibility(View.GONE);
                                    getCustomLogging().debug(TAG, "onLoadingComplete");
                                    mAnimator.setDisplayedChild(0);
                                }
                            },
                            new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    getCustomLogging().debug(TAG, "onProgressUpdate => " + Math.round(100.0f * current / total));
                                    //holder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });

        }else{
            getCustomLogging().debug(TAG, "setImageJournalCover -> Picture URL is empty");
            mAnimator.setDisplayedChild(2);
        }
    }

    private void setTextPictureCount(String numOfPictures) {
        this.mTextPictureCount.setText(Constant.STRING_NUMBER_OF_PICTURES_FORMAT.replace(Constant.DUMMY_NUMBER, numOfPictures));
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

    private void setTextTime(String val) {
        this.mTextTime.setText(val);
    }

    private void setTextTitle(String val) {
        this.mTextTitle.setText(val);
    }

    private void setTextContent(String val) {
        this.mTextContent.setText(val);
    }

    private void setJournal(Journal j){
        this.mJournal = j;
    }

    private void setAlbum(Album a){
        this.mAlbum = a;
    }

    private Context getContext() {
        return this.mContext;
    }

    private Journal getJournal() {
        return this.mJournal;
    }

    private Album getAlbum() {
        return this.mAlbum;
    }

    private ViewAnimator getViewAnimator() {
        return this.mAnimator;
    }

    private WideImageView getImageJournalCover() {
        return this.mImageJournalCover;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumPictureServiceType type = null;
        String jsonResult = "";

        switch (resultCode) {
            case PictureService.STATUS_RUNNING:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_RUNNING");
                break;

            case PictureService.STATUS_FINISHED:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_FINISHED");
                type = (EnumPictureServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case GET_ALBUM_PHOTO:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getCustomLogging().debug(TAG, "onReceiveResult GET_ALBUM_PHOTO -> jsonResult => " + jsonResult);

                        this.setAlbum(this.getParser().parseJsonToAlbum(jsonResult));
                        if(this.getAlbum() != null) {
                            this.setTextPictureCount(this.getParser().convertIntegerToString(this.getAlbum().getPictureList().size()));
                        }
                        break;

                }

                break;

            case JournalService.STATUS_ERROR:
                this.getCustomLogging().debug(TAG, "PictureService.STATUS_ERROR");
                break;
        }
    }

}