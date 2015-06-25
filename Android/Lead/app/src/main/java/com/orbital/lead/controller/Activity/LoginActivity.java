package com.orbital.lead.controller.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orbital.lead.R;
import com.orbital.lead.controller.Fragment.FragmentLogin;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.InternetVariableClass;

public class LoginActivity extends AppCompatActivity
        implements FragmentLogin.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    // fragments
    private FragmentLogin mFragmentLogin;

    private boolean exit = false;
    private InternetVariableClass mInternetVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide action bar (remove)
        //this.hideActionBar();

        // fragment manager
        this.initFragmentManager();

        // Display login first
        this.displayFragmentLogin();


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

    /*CHECK NETWORK CONNECTION*/
    public boolean hasNetworkConnection(){
        this.mInternetVar = this.getNetworkConnection();
        if(mInternetVar.getHasData() || mInternetVar.getHasWifi()){
            return true;
        }
        return false;
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

    private InternetVariableClass getNetworkConnection(){
        InternetVariableClass internetvar = new InternetVariableClass(false,false);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for(NetworkInfo ni : netInfo){
            if(ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()){
                internetvar.setHasWifi(true);
            }//end if
            if(ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()){
                internetvar.setHasData(true);
            }//end if
        }//end for

        return internetvar;
    }//end hasNetworkConnection

    private void hideActionBar(){
        getSupportActionBar().hide();
    }


    @Override
    public void onFragmentLoginInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if(this.exit){
            this.finish();
        }else{
            Toast.makeText(this, "Press back again to exit program.",
                    Toast.LENGTH_SHORT).show();
            this.exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }//end run
            }, 1 * 1000);
        }
    }
}
