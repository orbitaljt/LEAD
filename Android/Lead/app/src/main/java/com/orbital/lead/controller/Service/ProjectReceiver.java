package com.orbital.lead.controller.Service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;


/**
 * Created by joseph on 19/6/2015.
 */
public class ProjectReceiver extends ResultReceiver{

    private Receiver mReceiver;

    public ProjectReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
