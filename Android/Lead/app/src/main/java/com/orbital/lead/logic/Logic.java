package com.orbital.lead.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.orbital.lead.Parser.Parser;
import com.orbital.lead.controller.FragmentLogin;
import com.orbital.lead.logic.Asynchronous.AsyncLogin;
import com.orbital.lead.model.Message;

/**
 * Created by joseph on 14/6/2015.
 */
public class Logic {

    private static Logic mLogic = new Logic();
    //private ProgressDialog prog;
    //private Parser mParser;


    private Logic(){} // private constructor to prevent creating new instance

    public static Logic getInstance(){
        //mLogic.initParser();
        return mLogic;
    }

    /**
     * @param username User input a username in plaintext
     * @param password User input a password in plaintext
     * @return Message object - consists of code and message
     *
     * **/
    public void login(FragmentLogin frag, String username, String password){
        //frag.HttpAsyncLogin mAsyncLogin = new frag.HttpAsyncLogin(frag);
       // mAsyncLogin.execute(username, password);
    }

    /*
    private void initParser(){
        if(this.mParser == null) {
            this.mParser = Parser.getInstance();
        }
    }
    */

    /*
    private class HttpAsyncLogin extends AsyncLogin {

        private Context mContext;

        public HttpAsyncLogin(Context c){
            this.mContext = c;
        }

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(this.mContext);
            prog.setMessage("Logging in...");
            prog.show();
        }

        @Override
        protected void onPostExecute(final String result) {
            if (prog != null && prog.isShowing()) {
                prog.dismiss();
            }//end if

            Message msg = mParser.parseJsonToMessage(result);
            System.out.println(msg.getMessage());

        }

    }
    */


    public void showSnackBarRed(Activity activity){
        Snackbar mSnackBar = Snackbar.with(activity)
                .text("")
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                .actionLabel("")
                .actionColor(Color.WHITE)
                .color(Color.RED)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                    }
                });

        SnackbarManager.show(mSnackBar, activity);
    }

    public void showSnackBarNormal(Activity activity){
        Snackbar mSnackBar = Snackbar.with(activity)
                .text("")
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                .actionLabel("")
                .actionColor(Color.WHITE)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                    }
                });

        SnackbarManager.show(mSnackBar, activity);
    }



}
