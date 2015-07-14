package com.orbital.lead.controller.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.orbital.lead.Parser.FormatDate;
import com.orbital.lead.R;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerDividerItemDecoration;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerProjectListAdapter;
import com.orbital.lead.controller.RecyclerViewAdapter.RecyclerTagListAdapter;
import com.orbital.lead.controller.Service.JournalReceiver;
import com.orbital.lead.controller.Service.JournalService;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;
import com.orbital.lead.model.EnumDialogEditJournalType;
import com.orbital.lead.model.EnumJournalServiceType;
import com.orbital.lead.model.Journal;
import com.orbital.lead.model.Project;
import com.orbital.lead.model.ProjectList;
import com.orbital.lead.model.Tag;
import com.orbital.lead.model.TagList;
import com.orbital.lead.model.TagMap;

import java.util.ArrayList;

public class EditSpecificJournalActivity extends BaseActivity implements
        JournalReceiver.Receiver{

    private final String TAG = this.getClass().getSimpleName();

    private final int EDIT_TAG = 0;
    private final int EDIT_PROJECT = 1;

    private Context mContext;
    private View mToolbarView;
    private TextView mTextJournalDate;
    private TextView mTextTag;
    private TextView mTextProject;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;

    private Journal mJournal;
    private TagMap recentTagMap;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private RecyclerView mRecyclerViewTagList;
    private RecyclerView mRecyclerViewProjectList;
    private RecyclerView.Adapter mRecyclerDialogTagAdapter;
    private RecyclerView.Adapter mRecyclerDialogProjectAdapter;
    private JournalReceiver mJournalReceiver;

    private String currentTitle = "";
    private String currentContent = "";
    private String currentTags = "";
    private String currentProject = "";
    private String currentJournalDate = "";
    private TagList currentTagList;

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
        this.initTextTag();
        this.initTextProject();
        this.initOnDateSetListener();

        this.initUnusedTagMap();
        this.setRecentTagMap(getLogic().retrieveUnusedTagList(this));

        Bundle getBundleExtra = getIntent().getExtras();
        if (getBundleExtra != null) {

            this.setJournal((Journal) getBundleExtra.getParcelable(Constant.BUNDLE_PARAM_JOURNAL));

            this.currentTitle = this.getJournal().getTitle();
            this.currentContent = this.getJournal().getContent();
            this.currentTags = this.getJournal().getTagList().getCheckedToString();
            this.currentJournalDate = this.getJournal().getJournalDate();

            Project project = CurrentLoginUser.getUser().getProjectList().findProject(this.getJournal().getProject().getProjectID());
            this.currentProject = project != null ? project.getName() : "" ;
            this.currentTagList = new TagList();
            this.currentTagList.addList(this.getJournal().getTagList());

            getCustomLogging().debug(TAG, "getJournal().getTagList().size() => " + getJournal().getTagList().size());
            getCustomLogging().debug(TAG, "currentTagList.size() => " + currentTagList.size());


            this.setEditTextTitle(this.currentTitle);
            this.setEditTextContent(this.currentContent);
            this.setTextTag(this.currentTags);
            this.setTextProject(this.currentProject);
            this.setTextJournalDate(FormatDate.parseDate(this.currentJournalDate,
                                                        FormatDate.DATABASE_DATE_TO_DISPLAY_DATE,
                                                        FormatDate.DISPLAY_FORMAT));

            this.mYear = FormatDate.getYear(this.currentJournalDate, FormatDate.DATABASE_FORMAT);
            this.mMonth = FormatDate.getMonth(this.currentJournalDate, FormatDate.DATABASE_FORMAT);
            this.mDay = FormatDate.getDay(this.currentJournalDate, FormatDate.DATABASE_FORMAT);
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

        switch(id) {
            case android.R.id.home:
                getCustomLogging().debug(TAG, "onOptionsItemSelected");
                onBackPressed();
                return true;

            case R.id.action_done:
                this.saveJournal();
                return true;

            case R.id.action_image:
                if(this.getJournal() != null && this.getJournal().getAlbum() != null){
                    getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_LIST_PICTURES, this.getJournal().getAlbum());
                }else{
                    getLogic().displayPictureActivity(this, PictureActivity.OPEN_FRAGMENT_LIST_PICTURES, null);
                }

                return true;
        }
        return false;
    }


    public void addTag(Tag newTag) {
        //if(newTag != null){
        //    this.getEditJournal().getTagList().addTag(newTag);
       // }
    }

    public void removeTag(String name){
        //if(!getParser().isStringEmpty(name)){
        //    this.getJournal().getTagList().removeTag(name);
        //}
    }

    public void refreshRecyclerDialogTagAdapter(){
        if(this.mRecyclerDialogTagAdapter != null) {
            this.mRecyclerDialogTagAdapter.notifyDataSetChanged();
        }
    }

    public JournalReceiver getJournalReceiver(){
        if(this.mJournalReceiver == null){
            this.initJournalReceiver();
        }
        return this.mJournalReceiver;
    }

    private void initJournalReceiver(){
        mJournalReceiver = new JournalReceiver(new Handler());
        mJournalReceiver.setReceiver(this);
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

    private void initToolbar() {
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

    private void setContext(Context context) {
        this.mContext = context;
    }

    private void initUnusedTagMap() {
        this.recentTagMap = new TagMap();
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

                String databaseFormatDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                setTextJournalDate(convertToDisplayDate(databaseFormatDate));

            }
        };
    }

    private void initTextTag() {
        this.mTextTag = (TextView) findViewById(R.id.text_journal_tag);
        this.mTextTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagDialog(currentTagList, getRecentTagMap());
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

    private void initRecyclerDialogTagAdapter(TagList currentUsedTagList, TagMap unusedTagMap){
        this.mRecyclerDialogTagAdapter = new RecyclerTagListAdapter(currentUsedTagList, unusedTagMap);
    }

    private void initRecyclerProjectAdapter(ProjectList list){
        this.mRecyclerDialogProjectAdapter = new RecyclerProjectListAdapter(list, this.getJournal().getProject().getProjectID());
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
        this.mJournal = new Journal(j);
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

    private void setRecentTagMap(TagList list) {
        this.recentTagMap.addTagList(list);
    }

    private String getNewTitle() {
        return this.mEditTextTitle.getText().toString();
    }

    private String getNewJournalDate() {
        String displayDate = this.mTextJournalDate.getText().toString();
        String databaseDate = FormatDate.parseDate(displayDate, FormatDate.DISPLAY_DATE_TO_DATABASE_DATE, FormatDate.DATABASE_FORMAT);

        return databaseDate;
    }

    private String getNewTags() {
       return this.mTextTag.getText().toString();
    }

    private String getNewProject() {
        return this.mTextProject.getText().toString();
    }

    private String getNewContent() {
        return this.mEditTextContent.getText().toString();
    }

    private TagMap getRecentTagMap() {
        return this.recentTagMap;
    }


    private void updateRecentTagMap(TagList list) {
        for(Tag tag : list.getList()) {
            recentTagMap.setValue(tag.getName(), tag.getIsChecked());
        }
    }

    private void saveJournal() {
        if(hasChanges()){
            getCustomLogging().debug(TAG, "saveJournal there are changes made ");
            this.updateJournal();
        }else {
            getCustomLogging().debug(TAG, "saveJournal no changes made");
        }
    }

    private void updateJournal() {

        this.getJournal().setTitle(this.currentTitle);
        this.getJournal().setContent(this.currentContent);
        this.getJournal().setJournalDate(this.currentJournalDate);
        //this.getJournal().setProject(this.currentProject);
        this.getJournal().setTagList(this.currentTagList);

        String detail = getParser().updateJournalDetailToJson(this.getJournal());

        getCustomLogging().debug(TAG, "detail ==> " + detail);

        getLogic().updateUserJournal(this, CurrentLoginUser.getUser(), getJournal(), detail);

    }

    private boolean hasChanges(){

        getCustomLogging().debug(TAG, this.currentTitle + " <=> " + getNewTitle());
        getCustomLogging().debug(TAG, currentContent + " <=> " + getNewContent());
        getCustomLogging().debug(TAG, currentTags + " <=> " + getNewTags());
        getCustomLogging().debug(TAG, currentProject + " <=> " + getNewProject());
        getCustomLogging().debug(TAG, currentJournalDate + " <=> " + getNewJournalDate());


        if (this.getParser().compareBothString(this.currentTitle, this.getNewTitle())
                && this.getParser().compareBothString(this.currentContent, this.getNewContent())
                && this.getParser().compareBothString(this.currentTags, this.getNewTags())
                && this.getParser().compareBothString(this.currentProject, this.getNewProject())
                && this.getParser().compareBothString(this.currentJournalDate, this.getNewJournalDate())){

            return false; // no changes as all are the same
        }
        return true;
    }

    private void setProject(Project project) {
        this.getJournal().setProject(project);
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

   private void showTagDialog(TagList currentUsedTagList, final TagMap unusedTagMap){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();

       final View dialogView = inflater.inflate(R.layout.dialog_tag, null);


       this.initRecyclerDialogTagAdapter(currentUsedTagList, unusedTagMap);
       this.initDialogTagRecyclerView(dialogView);

       // Add new tag at the toolbar
       ImageView addNewTag = (ImageView) dialogView.findViewById(R.id.image_toolbar_add_new_tag);
       addNewTag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getLogic().showAddTagProjectDialog(EditSpecificJournalActivity.this, EnumDialogEditJournalType.ADD_TAG, "");
           }
       });


       builder.setView(dialogView)
               .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {

                       TagList list = ((RecyclerTagListAdapter) getRecyclerDialogTagAdapter()).getCompliedTagList();
                       updateRecentTagMap(list); // update tag map

                       // update all tags checked status that belongs to the current tag list from tag map
                       for (Tag t : currentTagList.getList()) {
                           boolean value = getRecentTagMap().getMap().get(t.getName());
                           t.setIsChecked(value);
                       }

                       setTextTag(currentTagList.getCheckedToString()); // update the tag text, show only all checked tags

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
                getLogic().showAddTagProjectDialog(EditSpecificJournalActivity.this, EnumDialogEditJournalType.ADD_PROJECT, "");
            }
        });

        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Project selectedProject = ((RecyclerProjectListAdapter) getRecyclerDialogProjectAdapter()).getSelectedProject();
                        setProject(selectedProject);
                        setTextProject(selectedProject != null ? selectedProject.getName() : "");
                    }
                });

        builder.create().show();
    }

    private String convertToDisplayDate(String oldDate) {
        return FormatDate.parseDate(oldDate, FormatDate.DATABASE_DATE_TO_DISPLAY_DATE, FormatDate.DISPLAY_FORMAT);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EnumJournalServiceType type = null;
        String jsonResult = "";

        switch (resultCode) {
            case JournalService.STATUS_RUNNING:
                break;
            case JournalService.STATUS_FINISHED:
                this.getCustomLogging().debug(TAG, "onReceiveResult -> JournalService.STATUS_FINISHED");
                type = (EnumJournalServiceType) resultData.getSerializable(Constant.INTENT_SERVICE_EXTRA_TYPE_TAG);

                switch(type){
                    case UPDATE_SPECIFIC_JOURNAL:

                        jsonResult = resultData.getString(Constant.INTENT_SERVICE_RESULT_JSON_STRING_TAG);
                        this.getCustomLogging().debug(TAG, "onReceiveResult UPDATE_SPECIFIC_JOURNAL -> jsonResult => " + jsonResult);

                        break;
                }
                break;
            case JournalService.STATUS_ERROR:
                break;
        }
    }

}
