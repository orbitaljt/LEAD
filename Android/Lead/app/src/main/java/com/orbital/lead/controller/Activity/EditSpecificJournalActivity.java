package com.orbital.lead.controller.Activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.CurrentLoginUser;

import org.w3c.dom.Text;

public class EditSpecificJournalActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private View mToolbarView;
    private TextView mTextJournalDate;
    private TextView mTextHashTag;
    private TextView mTextProject;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;



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



}
