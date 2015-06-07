package com.orbital.lead.controller;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orbital.lead.R;
import com.orbital.lead.model.Constant;

import java.lang.reflect.Array;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity
        implements FragmentLogin.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    // fragments
    private FragmentLogin mFragmentLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // fragment manager
        this.initFragmentManager();

        // Display login first
        this.displayFragmentLogin();

        //Other initialization



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
        }

        return super.onOptionsItemSelected(item);
    }


    public void initFragmentManager(){

        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    //mNavigationDrawerToggle.setDrawerIndicatorEnabled(false);
                } else {
                    //mNavigationDrawerToggle.setDrawerIndicatorEnabled(true);
                    //mNavigationDrawerToggle.setToolbarNavigationClickListener(mNavigationDrawerFragment.getOriginalToolbarNavigationClickListener());
                }
            }
        });
    }






    private void displayFragmentLogin(){
        String fragName = Constant.FRAGMENT_LOGIN_NAME;
        this.mFragmentLogin = FragmentLogin.newInstance("", "");
        this.replaceFragment(this.mFragmentLogin, fragName);
    }

    private void replaceFragment(Fragment newFrag, String name){
        if(newFrag != null){
            this.fragmentTransaction = fragmentManager.beginTransaction();
            this.fragmentTransaction
                    .replace(R.id.login_activity_container, newFrag, name)
                    .commitAllowingStateLoss();
        }
    }


    @Override
    public void onFragmentLoginInteraction(Uri uri) {

    }
}
