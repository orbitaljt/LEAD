package com.orbital.lead.controller.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;

public class AddNewSpecificJournalActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private View mToolbarView;
    private TextView mToolbarTitle;
    private TextView mTextJournalDate;
    private TextView mTextTag;
    private TextView mTextProject;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private AlertDialog mDialogOption;

    private Journal mJournal;
    private TagList newTagList;
    private ProjectList newProjectList;
    private Project newProject;


    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private RecyclerView mRecyclerViewTagList;
    private RecyclerView mRecyclerViewProjectList;
    private RecyclerView.Adapter mRecyclerDialogTagAdapter;
    private RecyclerView.Adapter mRecyclerDialogProjectAdapter;

    private String newJournalID;
    private String newAlbumID;

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean toggleRefresh = false;

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

        this.initJournalReceiver();
        this.retrieveNewJournalAlbumID();

        this.initNewJournal();
        this.initTextTitle();
        this.initTextContent();
        this.initTextJournalDate();
        this.initTextTag();
        this.initTextProject();
        this.initNewTagList();
        this.initNewProject();
        this.initOnDateSetListener();

        this.initTagSet();
        this.retrievePreferenceTagSet();

        this.newProjectList = new ProjectList();
        this.newProjectList.setList(this.getCurrentUser().getProjectList());

        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getNavigationDrawerFragment().isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_add_new_specific_journal, menu);
            this.restoreCustomActionbar();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home: // save the current new journal
                //onBackPressed();
                this.uploadNewJournal();
                return true;

            case R.id.action_image:
                getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_LIST_PICTURES, this.getJournal().getAlbum(), this.getJournal().getJournalID());
                return true;

        }

        return false;
    }

    @Override
    public void setToolbarTitle(String title){
        this.mToolbarTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.BUNDLE_PARAM_JOURNAL_LIST_TOGGLE_REFRESH, toggleRefresh);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
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
                getLogging().debug(TAG, "mToolbarView setNavigationOnClickListener onClick");
            }
        });
    }

    public void refreshRecyclerDialogTagAdapter(){
        if(this.mRecyclerDialogTagAdapter != null) {
            this.mRecyclerDialogTagAdapter.notifyDataSetChanged();
        }
    }

    public void setToggleRefresh(boolean val) {
        this.toggleRefresh = val;
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

    private void retrieveNewJournalAlbumID() {
        this.getLogic().getNewJournaAlbumlID(this);
    }

    private void initNewJournal() {
        this.mJournal = new Journal();
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

                String databaseFormatDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                setTextJournalDate(convertToDisplayDate(databaseFormatDate));
            }
        };
    }

    private void initTextTag() {
        this.mTextTag = (TextView) findViewById(R.id.text_journal_tag);
        this.mTextTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagDialog(getTagSet().getTagList());
            }
        });
    }

    private void initTextProject() {
        this.mTextProject = (TextView) findViewById(R.id.text_journal_project);
        this.mTextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectDialog(getCurrentUser().getProjectList());
            }
        });
    }

    private void initNewTagList() {
        this.newTagList = new TagList();
    }

    private void initNewProject() {
        this.newProject = new Project();
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
        getLogging().debug(TAG, "initRecyclerDialogTagAdapter");
        this.mRecyclerDialogTagAdapter = new RecyclerTagListAdapter(currentUsedTagList);
        /*
        ((RecyclerTagListAdapter) mRecyclerDialogTagAdapter).setOnItemClickListener(new RecyclerTagListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getLogging().debug(TAG, "initRecyclerDialogTagAdapter onItemClick position -> " + position);
            }
        });
        */
    }

    private void initRecyclerProjectAdapter(ProjectList list){
        getLogging().debug(TAG, "initRecyclerProjectAdapter");
        //this.mRecyclerDialogTagAdapter = new RecyclerJournalListAdapter(headerView, list, CurrentLoginUser.getUser());
        if(this.newProject != null) {
            this.mRecyclerDialogProjectAdapter = new RecyclerProjectListAdapter(list, this.newProject.getProjectID());
        }else{
            this.mRecyclerDialogProjectAdapter = new RecyclerProjectListAdapter(list, "");
        }
    }

    private RecyclerView.Adapter getRecyclerDialogTagAdapter(){
        return this.mRecyclerDialogTagAdapter;
    }

    private RecyclerView.Adapter getRecyclerDialogProjectAdapter(){
        return this.mRecyclerDialogProjectAdapter;
    }

    public void setNewJournalID(String id) {
        this.getLogging().debug(TAG, "setNewJournalID id => " + id);
        this.newJournalID = id;
    }

    public void setNewAlbumID(String id) {
        this.getLogging().debug(TAG, "setNewAlbumID id => " + id);
        this.newAlbumID = id;
    }

    public void setJournal(Journal j){
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

    private void setNewProject(Project project) {
        this.newProject = project;
    }

    private String getEditTextTitle() {
        return this.mEditTextTitle.getText().toString();
    }

    private String getTextJournalDate() {
        return this.mTextJournalDate.getText().toString();
    }

    private String getTextTags() {
        return this.mTextTag.getText().toString();
    }

    private String getTextProject() {
        return this.mTextProject.getText().toString();
    }

    private String getEditTextContent() {
        return this.mEditTextContent.getText().toString();
    }

    private TagList getTagList() {
        return this.newTagList;
    }

    private Journal getJournal() {
        return this.mJournal;
    }

    private void updateNewTagList(TagList list) {
        this.newTagList.replaceWithTagList(list);
    }

    private String getNewJournalID() {
        return this.newJournalID;
    }

    private String getNewAlbumID() {
        return this.newAlbumID;
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

   private void showTagDialog(TagList list){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();

       final View dialogView = inflater.inflate(R.layout.dialog_tag, null);

       this.initRecyclerDialogTagAdapter(list);
       this.initDialogTagRecyclerView(dialogView);

       ImageView addNewTag = (ImageView) dialogView.findViewById(R.id.image_toolbar_add_new_tag);
       addNewTag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getLogic().showAddTagProjectDialog(AddNewSpecificJournalActivity.this, EnumDialogEditJournalType.ADD_TAG, "");
           }
       });

       builder.setView(dialogView)
               .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       TagList list = ((RecyclerTagListAdapter) getRecyclerDialogTagAdapter()).getTagList();
                       updateNewTagList(list); // update new tag list

                       for (Tag t : newTagList.getList()) {
                           getLogging().debug(TAG, "after update newTagList t.getName() => " + t.getName() + " checked => " + t.getIsChecked());
                       }

                       setTextTag(newTagList.getCheckedToString()); // update the tag text, show only all checked tags
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
                        Project selectedProject = ((RecyclerProjectListAdapter) getRecyclerDialogProjectAdapter()).getSelectedProject();

                        newProject = selectedProject;
                        newProjectList.resetList();
                        if (selectedProject != null) { // sometimes user will not choose a project
                            newProjectList.updateProject(selectedProject);
                        }

                        setTextProject(selectedProject != null ? selectedProject.getName() : "");
                        setNewProject(selectedProject); // update the new project
                    }
                });

        builder.create().show();
    }


    private void uploadNewJournal() {
        if(!this.isValidJournal()) {
            this.getLogging().debug(TAG, "uploadNewJournal it is not a valid new journal");
            return;
        }

        this.getJournal().setJournalID(this.getNewJournalID());
        this.getJournal().setTitle(this.getEditTextTitle());
        this.getJournal().setContent(this.getEditTextContent());
        this.getJournal().setJournalDate(this.convertToDatabaseDate(this.getTextJournalDate()));
        this.getJournal().setTagList(this.newTagList);
        this.getJournal().setProject(this.newProject);

        String detail = this.getParser().uploadNewJournalToJson(this.getJournal());
        this.getLogging().debug(TAG, "uploadNewJournal detail => " + detail);

        this.getLogic().insertNewJournal(this, this.getCurrentUser().getUserID(), this.getNewJournalID(), this.getNewAlbumID(), detail);
    }

    private boolean isValidJournal() {
        // Check title and content
        if(getParser().isStringEmpty(this.getEditTextTitle()) &&
                getParser().isStringEmpty(this.getEditTextContent())) {
            return false; // if both empty, means it is not suitable to be uploaded as a new journal
        }
        return true;
    }




}
