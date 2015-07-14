package com.orbital.lead.controller.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerDividerItemDecoration;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerProjectListAdapter;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerTagListAdapter;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;

import java.util.ArrayList;

public class AddNewSpecificJournalActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private final int EDIT_TAG = 0;
    private final int EDIT_PROJECT = 1;

    private View mToolbarView;
    private TextView mToolbarTitle;
    private TextView mTextJournalDate;
    private TextView mTextTag;
    private TextView mTextProject;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private AlertDialog mDialogOption;

    private Journal mJournal;
    private TagList mHistoryTagList;
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
        getLayoutInflater().inflate(R.layout.activity_add_new_specific_journal, getBaseFrameLayout());

        this.initToolbar();
        this.initToolbarTitle();
        this.setToolbarTitle(Constant.TITLE_ADD_NEW_JOURNAL);
        this.pushToolbarToActionbar();
        this.restoreCustomActionbar();
        this.restoreDrawerHeaderValues();

        this.initTextTitle();
        this.initTextContent();
        this.initTextJournalDate();
        this.initTextTag();
        this.initTextProject();
        this.initOnDateSetListener();

        getLogic().retrieveUnusedTagList(this);

        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_add_new_specific_journal, menu);
            this.restoreCustomActionbar();
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

        }else if(id == android.R.id.home){
            // save the current new journal
            onBackPressed();
            return true;

        }else if(id == R.id.action_image) {
            getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_LIST_PICTURES, null);
            return true;
        }

        return false;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarTitle(String title){
        this.mToolbarTitle.setText(title);
    }


    private void restoreCustomActionbar(){
        // disable the home button and onClick to open navigation drawer
        // enable the back arrow button
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_done);
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

    private void initToolbarTitle() {
        this.mToolbarTitle = (TextView) findViewById(R.id.toolbar_text_title);
    }

    private View getToolbar() {
        return this.mToolbarView;
    }

    private void pushToolbarToActionbar() {
        setSupportActionBar((Toolbar) this.getToolbar());
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

    private void initTextTag() {
        this.mTextTag = (TextView) findViewById(R.id.text_journal_tag);
        this.mTextTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagDialog(getJournal().getTagList());
            }
        });
    }

    private void initTextProject() {
        this.mTextProject = (TextView) findViewById(R.id.text_journal_project);
        this.mTextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectDialog(CurrentLoginUser.getUser().getProjectList());
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

    private void initRecyclerDialogTagAdapter(TagList currentUsedTagList){
        getCustomLogging().debug(TAG, "initRecyclerDialogTagAdapter");
        //this.mRecyclerDialogTagAdapter = new RecyclerJournalListAdapter(headerView, list, CurrentLoginUser.getUser());
        this.mRecyclerDialogTagAdapter = new RecyclerTagListAdapter(currentUsedTagList, null);
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
        this.mRecyclerDialogProjectAdapter = new RecyclerProjectListAdapter(list, "");
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

    private void setTextTag(String value){
        this.mTextTag.setText(value);
    }

    private void setTextProject(String value){
        this.mTextProject.setText(value);
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

   private void showTagDialog(TagList currentUsedTagList){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();

       final View dialogView = inflater.inflate(R.layout.dialog_tag, null);

       this.initRecyclerDialogTagAdapter(currentUsedTagList);
       this.initDialogTagRecyclerView(dialogView);

       ImageView addNewTag = (ImageView) dialogView.findViewById(R.id.image_toolbar_add_new_tag);
       addNewTag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //showAddTagProjectDialog(EnumDialogEditJournalType.ADD_TAG, "");
               getLogic().showAddTagProjectDialog(AddNewSpecificJournalActivity.this, EnumDialogEditJournalType.ADD_TAG, "");
           }
       });

       builder.setView(dialogView)
               .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });

       builder.create().show();
   }

    private void showProjectDialog(ProjectList projectList){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_project, null);

        this.initRecyclerProjectAdapter(projectList);
        this.initDialogProjectRecyclerView(dialogView);

        ImageView addNewProject = (ImageView) dialogView.findViewById(R.id.image_toolbar_add_new_project);
        addNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogic().showAddTagProjectDialog(AddNewSpecificJournalActivity.this, EnumDialogEditJournalType.ADD_PROJECT, "");
            }
        });

        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }







    private TagList initRecentHistoryTagList(){
        //String tags = getLogic().retrieveUnusedTagList(this);
        return null;
    }





    private TagList createDummyTagList(){
        TagList mTagList = new TagList();
        ArrayList<Tag> list = new ArrayList<Tag>();
        Tag mTag = new Tag("1", "1", "Demo", true);
        list.add(mTag);
        mTag = new Tag("2", "2", "MiddleEast", false);
        list.add(mTag);
        mTag = new Tag("3", "3", "Richest", false);
        list.add(mTag);
        mTag = new Tag("4", "4", "AbuDhabi", true);
        list.add(mTag);

        mTagList.setList(list);

        return mTagList;
    }

}
