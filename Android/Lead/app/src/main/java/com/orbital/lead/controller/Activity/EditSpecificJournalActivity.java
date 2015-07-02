package com.orbital.lead.controller.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.R;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerDividerItemDecoration;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerProjectListAdapter;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerTagListAdapter;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;

import java.util.ArrayList;

public class EditSpecificJournalActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private final int EDIT_TAG = 0;
    private final int EDIT_PROJECT = 1;

    private Context mContext;
    private View mToolbarView;
    private TextView mTextJournalDate;
    private TextView mTextHashTag;
    private TextView mTextProject;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;

    private Journal mJournal;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private RecyclerView mRecyclerViewTagList;
    private RecyclerView mRecyclerViewProjectList;
    private RecyclerView.Adapter mRecyclerDialogTagAdapter;
    private RecyclerView.Adapter mRecyclerDialogProjectAdapter;

    private int mYear;
    private int mMonth;
    private int mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_specific_journal, getBaseFrameLayout());

        this.setContext(this);
        this.initToolbar();
        this.pushToolbarToActionbar();
        //this.restoreActionBar();
        this.restoreCustomActionbar();
        this.restoreDrawerHeaderValues();

        this.initTextTitle();
        this.initTextContent();
        this.initTextJournalDate();
        this.initTextHashTag();
        this.initTextProject();
        this.initOnDateSetListener();


        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {
            this.setJournal((Journal) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_JOURNAL));

            this.setEditTextTitle(this.getJournal().getTitle());
            this.setEditTextContent(this.getJournal().getContent());
            this.setTextJournalDate(FormatDate.parseDate(this.getJournal().getJournalDate(),
                                    FormatDate.DATABASE_DATE_TO_DISPLAY_DATE,
                                    FormatDate.DISPLAY_FORMAT));

            this.mYear = FormatDate.getYear(this.getJournal().getJournalDate(), FormatDate.DATABASE_FORMAT);
            this.mMonth = FormatDate.getMonth(this.getJournal().getJournalDate(), FormatDate.DATABASE_FORMAT);
            this.mDay = FormatDate.getDay(this.getJournal().getJournalDate(), FormatDate.DATABASE_FORMAT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_edit_specific_journal, menu);
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

    private View getToolbar() {
        return this.mToolbarView;
    }

    private void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
    }

    private void restoreDrawerHeaderValues() {
        this.getNavigationDrawerFragment().setImageUserProfile(CurrentLoginUser.getUser().getProfilePicUrl());
        this.getNavigationDrawerFragment().setTextUserName(CurrentLoginUser.getUser().getFirstName(),
                CurrentLoginUser.getUser().getMiddleName(),
                CurrentLoginUser.getUser().getLastName());
        this.getNavigationDrawerFragment().setmTextUserEmail(CurrentLoginUser.getUser().getEmail());
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private void initTextTitle() {
        this.mEditTextTitle = (EditText) findViewById(R.id.edit_text_title);
    }

    private void initTextContent(){
        this.mEditTextContent = (EditText) findViewById(R.id.edit_text_content);
    }

    private void initTextJournalDate() {
        this.mTextJournalDate = (TextView) findViewById(R.id.text_journal_date);
        this.mTextJournalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void initOnDateSetListener() {
        this.datePickerListener =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                // Display Selected date in textbox
                /*
                txtDate.setText(dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year);
                */
            }
        };
    }

    private void initTextHashTag() {
        this.mTextHashTag = (TextView) findViewById(R.id.text_journal_tag);
        this.mTextHashTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHashTagDialog();
            }
        });
    }

    private void initTextProject() {
        this.mTextProject = (TextView) findViewById(R.id.text_journal_project);
        this.mTextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectDialog();
            }
        });
    }

    private void initDialogTagRecyclerView(View v){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.mRecyclerViewTagList = (RecyclerView) v.findViewById(R.id.recycler_edit_tag);
        this.mRecyclerViewTagList.setLayoutManager(layoutManager);
        this.mRecyclerViewTagList.setHasFixedSize(false);
        this.mRecyclerViewTagList.addItemDecoration(new RecyclerDividerItemDecoration(this, RecyclerDividerItemDecoration.VERTICAL_LIST));
        this.mRecyclerViewTagList.setAdapter(this.getRecyclerDialogTagAdapter());
    }

    private void initDialogProjectRecyclerView(View v){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.mRecyclerViewProjectList = (RecyclerView) v.findViewById(R.id.recycler_edit_project);
        this.mRecyclerViewProjectList.setLayoutManager(layoutManager);
        this.mRecyclerViewProjectList.setHasFixedSize(false);
        this.mRecyclerViewProjectList.addItemDecoration(new RecyclerDividerItemDecoration(this, RecyclerDividerItemDecoration.VERTICAL_LIST));
        this.mRecyclerViewProjectList.setAdapter(this.getRecyclerDialogProjectAdapter());
    }

    private void initRecyclerDialogTagAdapter(TagList list){
        getCustomLogging().debug(TAG, "initRecyclerDialogTagAdapter");
        //this.mRecyclerDialogTagAdapter = new RecyclerJournalListAdapter(headerView, list, CurrentLoginUser.getUser());
        this.mRecyclerDialogTagAdapter = new RecyclerTagListAdapter(list);
        /*
        ((RecyclerTagListAdapter) mRecyclerDialogTagAdapter).setOnItemClickListener(new RecyclerTagListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getCustomLogging().debug(TAG, "initRecyclerDialogTagAdapter onItemClick position -> " + position);
            }
        });
        */
    }

    private void initRecyclerProjectAdapter(ProjectList list){
        getCustomLogging().debug(TAG, "initRecyclerProjectAdapter");
        //this.mRecyclerDialogTagAdapter = new RecyclerJournalListAdapter(headerView, list, CurrentLoginUser.getUser());
        this.mRecyclerDialogProjectAdapter = new RecyclerProjectListAdapter(list);
        /*
        ((RecyclerTagListAdapter) mRecyclerDialogTagAdapter).setOnItemClickListener(new RecyclerTagListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getCustomLogging().debug(TAG, "initRecyclerDialogTagAdapter onItemClick position -> " + position);
            }
        });
        */
    }


    private RecyclerView.Adapter getRecyclerDialogTagAdapter(){
        return this.mRecyclerDialogTagAdapter;
    }

    private RecyclerView.Adapter getRecyclerDialogProjectAdapter(){
        return this.mRecyclerDialogProjectAdapter;
    }

    private Journal getJournal() {
        return this.mJournal;
    }

    private void setJournal(Journal j){
        this.mJournal = j;
    }

    private void setEditTextTitle(String value){
        this.mEditTextTitle.setText(value);
    }

    private void setEditTextContent(String value){
        this.mEditTextContent.setText(value);
    }

    private void setTextJournalDate(String value) {
        this.mTextJournalDate.setText(value);
    }


    /*=============== DIALOGS ==========*/
    private void showDatePickerDialog(){
        this.datePickerDialog = new DatePickerDialog(this,
                this.getDatePickerListener(),
                this.mYear,
                this.mMonth,
                this.mDay);
        this.datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getDatePickerListener(){
        return this.datePickerListener;
    }

   private void showHashTagDialog(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();

       final View dialogView = inflater.inflate(R.layout.dialog_tag, null);

       this.initRecyclerDialogTagAdapter(createDummyTagList());
       this.initDialogTagRecyclerView(dialogView);

       builder.setView(dialogView)
               .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });

       builder.create().show();
   }

    private void showProjectDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_project, null);

        this.initRecyclerProjectAdapter(createDummyProjectList());
        this.initDialogProjectRecyclerView(dialogView);

        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private TagList createDummyTagList(){
        TagList mTagList = new TagList();
        ArrayList<Tag> list = new ArrayList<Tag>();
        Tag mTag = new Tag("Demo", true);
        list.add(mTag);
        mTag = new Tag("MiddleEast", false);
        list.add(mTag);
        mTag = new Tag("Richest", false);
        list.add(mTag);
        mTag = new Tag("AbuDhabi", true);
        list.add(mTag);

        mTagList.setList(list);

        return mTagList;
    }

    private ProjectList createDummyProjectList(){
        ProjectList mProjectList = new ProjectList();
        ArrayList<Project> list = new ArrayList<Project>();
        Project p = new Project("Project Alpha", true);
        list.add(p);
        p = new Project("Project Beta", false);
        list.add(p);
        p = new Project("Project Delta", false);
        list.add(p);
        p = new Project("Project XYZ", false);
        list.add(p);

        mProjectList.setList(list);

        return mProjectList;
    }

}