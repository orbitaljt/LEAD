package com.orbital.lead.logic;

import android.app.ProgressDialog;
import android.content.Context;

import com.orbital.lead.logic.Asynchronous.AsyncLogin;
import com.orbital.lead.model.Message;

/**
 * Created by joseph on 14/6/2015.
 */
public class Logic {

    private static Logic mLogic = new Logic();

    private ProgressDialog prog;

    private Logic(){} // private constructor to prevent creating new instance

    public static Logic getInstance(){
        return mLogic;
    }

    /**
     * @param username User input a username in plaintext
     * @param password User input a password in plaintext
     * @return Message object - consists of code and message
     *
     * **/
    public Message login(Context context, String username, String password){
        Message returnMsg = null;

        HttpAsyncLogin mAsyncLogin = new HttpAsyncLogin(context);
        mAsyncLogin.execute(username, password);

        return returnMsg;
    }

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

            System.out.println("onPostExecute");
            System.out.println(result);

        }

    }




}
