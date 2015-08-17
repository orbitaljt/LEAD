package com.orbital.lead.controller.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.Parser.FormatTime;
import com.orbital.lead.R;
import com.orbital.lead.controller.Service.PictureReceiver;
import com.orbital.lead.controller.Service.PictureService;
import com.orbital.lead.model.Album;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumOpenPictureActivityType;
import com.orbital.lead.model.EnumPictureServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.Project;
import com.orbital.lead.widget.WideImageView;

public class SpecificJournalActivity extends BaseActivity implements PictureReceiver.Receiver {
    //public static final int START_EDIT_SPECIFIC_JOURNAL_ACTIVITY = 1;
    private final String TAG = this.getClass().getSimpleName();


    private Context mContext;
    private View mToolbarView;
    private WideImageView mImageJournalCover;
    private ViewAnimator mAnimator;
    private TextView mTextPictureCount;
    private TextView mTextTag;
    private TextView mTextProject;
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
        this.initTextTag();
        this.initTextProject();
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
            this.setJournalDetails(this.getJournal());

        } else {
            getLogging().debug(TAG, "No bundle extra from getIntent()");
        }

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
        if(id == android.R.id.home) {
            getLogging().debug(TAG, "onOptionsItemSelected");
            onBackPressed();
            return true;
        }else if (id == R.id.action_edit_journal){
            this.displayEditSpecificJournal();

        }else if (id == R.id.action_settings) {
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getLogging().debug(TAG, "onRestoreInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        getLogging().debug(TAG, "onResume");
    }

    @Override
        public void onPause() {
        super.onPause();
        getLogging().debug(TAG, "onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getLogging().debug(TAG, "onSaveInstanceState");
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
                getLogging().debug(TAG, "mToolbarView setNavigationOnClickListener onClick");
            }
        });
    }

    public View getToolbar() {
        return this.mToolbarView;
    }

    public void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }


    /*===================== Picture Intent Service & Receiver ================================*/
    public void initPictureReceiver(){
        mPictureReceiver = new PictureReceiver(new Handler());
        mPictureReceiver.setReceiver(this);
    }

    public PictureReceiver getPictureReceiver(){
        if(this.mPictureReceiver == null){
            this.initPictureReceiver();
        }
        return this.mPictureReceiver;
    }


    private void initImageJournalCover() {
        this.mImageJournalCover = (WideImageView) findViewById(R.id.image_journal_cover);
        this.mImageJournalCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogging().debug(TAG, "mImageJournalCover onClick");
                if(getAlbum() != null){
                    getLogic().displayPictureActivity(getContext(), EnumOpenPictureActivityType.OPEN_FRAGMENT_LIST_PICTURES, getAlbum(), getJournal().getJournalID());
                }
            }
        });
    }

    private void initViewAnimator() {
        this.mAnimator = (ViewAnimator) findViewById(R.id.animator);
    }

    private void initTextPictureCount() {
        this.mTextPictureCount = (TextView) findViewById(R.id.text_photo_count);
        this.mTextPictureCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogging().debug(TAG, "mTextPictureCount onClick");
                if(getAlbum() != null){
                    getLogic().displayPictureActivity(getContext(), EnumOpenPictureActivityType.OPEN_FRAGMENT_LIST_PICTURES, getAlbum(), getJournal().getJournalID());
                }
            }
        });
    }

    private void initTextTag() {
        this.mTextTag = (TextView) findViewById(R.id.text_tag);
    }

    private void initTextProject() {
        this.mTextProject = (TextView) findViewById(R.id.text_project);
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

    private void setJournalDetails(Journal displayJournal) {
        this.setImageJournalCover(this.getParser().createPictureCoverUrl(displayJournal.getPictureCoverID(),
                displayJournal.getPictureCoverType().toString(),
                CurrentLoginUser.getUser().getUserID()));

        String time = FormatTime.parseTime(displayJournal.getJournalTime(), FormatTime.DATABASE_TIME_TO_DISPLAY_TIME); // HH:mm
        String date = FormatDate.parseDate(displayJournal.getJournalDate(), FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FULL_FORMAT); // dd MMMM yyyy cccc
        String[] dates = date.split(" ");

        this.setTextTag(displayJournal.getTagList().getCheckedToString());
        this.setTextDayDigit(dates[0]);
        this.setTextMonthYear(dates[1] + " " + dates[2]);
        this.setTextDayName(dates[3]);
        this.setTextTime(time);
        this.setTextTitle(displayJournal.getTitle());
        this.setTextContent(displayJournal.getContent());

        Project project = getCurrentUser().getProjectList().findProject(displayJournal.getProject().getProjectID());
        this.setTextProject(project != null ? project.getName() : "");

        // run picture service first
        this.getLogic().getUserSpecificAlbum(this, displayJournal.getAlbum().getAlbumID());
    }


    private void setContext(Context context) {
        this.mContext = context;
    }

    private void setImageJournalCover(String url) {
        this.getViewAnimator().setDisplayedChild(1);
        //.error(R.drawable.image_blank_picture)
        //.transform(new RoundedTransformation(10, 0))
        if(!this.getParser().isStringEmpty(url)){
            this.getLogic().showPicture(this, mAnimator, this.getImageJournalCover(), url);
        }else{
            getLogging().debug(TAG, "setImageJournalCover -> Picture URL is empty");
            mAnimator.setDisplayedChild(2);
        }
    }

    private void setTextPictureCount(String numOfPictures) {
        this.mTextPictureCount.setText(Constant.STRING_NUMBER_OF_PICTURES_FORMAT.replace(Constant.DUMMY_NUMBER, numOfPictures));
    }

    private void setTextTag(String val){
        this.mTextTag.setText(val);
    }

    private void setTextProject(String val){
        this.mTextProject.setText(val);
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

    private void displayEditSpecificJournal() {
        this.getLogic().displayEditSpecificJournalActivity(this, this.getJournal());
    }

    public void refreshJournal(String journalID){
        getLogging().debug(TAG, "refreshJournal...");
        Journal updatedJournal = CurrentLoginUser.getUser().getJournalList().get(journalID);
        if(updatedJournal != null) {
            this.setJournalDetails(updatedJournal);
            this.setJournal(updatedJournal);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case START_EDIT_SPECIFIC_JOURNAL_ACTIVITY :
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();
                    if (b != null) {
                        String journalID = b.getString(Constant.BUNDLE_PARAM_JOURNAL_ID);
                        boolean refresh = b.getBoolean(Constant.BUNDLE_PARAM_JOURNAL_TOGGLE_REFRESH);

                        getLogging().debug(TAG, "onActivityResult journalID => " + journalID);
                        getLogging().debug(TAG, "onActivityResult refresh => " + refresh);

                        if(refresh) {
                            this.refreshJournal(journalID);
                        }
                    }
                }
                break;
        }
    }




    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumPictureServiceType type = null;
        String jsonResult = "";

        try{
            type = (EnumPictureServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);
        }catch(Exception e){
            this.getLogging().debug(TAG, "onReceiveResult -> Invalid type. Either empty or invalid for this activity");
            e.printStackTrace();
            return;
        }

        switch (resultCode) {
            case PictureService.STATUS_RUNNING:
                this.getLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_RUNNING");
                switch(type){
                    case GET_SPECIFIC_ALBUM:
                        this.setTextPictureCount(Constant.STRING_LOADING_PICTURE);
                        break;
                }

                break;

            case PictureService.STATUS_FINISHED:
                this.getLogging().debug(TAG, "onReceiveResult -> PictureService.STATUS_FINISHED");

                switch(type){
                    case GET_SPECIFIC_ALBUM:
                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getLogging().debug(TAG, "onReceiveResult GET_SPECIFIC_ALBUM -> jsonResult => " + jsonResult);

                        Album album = this.getParser().parseJsonToSpecificAlbum(jsonResult);

                        this.getJournal().setAlbum(album);
                        this.setAlbum(album);

                        if(this.getAlbum() != null) {
                            this.setTextPictureCount(this.getParser().convertIntegerToString(this.getAlbum().getPictureList().size()));
                        }
                        break;
                }

                break;

            case PictureService.STATUS_ERROR:
                this.getLogging().debug(TAG, "PictureService.STATUS_ERROR");
                break;
        }
    }

}