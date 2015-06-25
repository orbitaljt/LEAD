package com.orbital.lead.Parser;

import com.orbital.lead.logic.CustomLogging;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joseph on 24/6/2015.
 */
public class FormatTime {
    public static final int DATABASE_TIME_TO_DISPLAY_TIME = 0;
    public static final int DISPLAY_TIME_TO_DATABASE_TIME = 1;

    //public static final String DATABASE_FORMAT = "yyyy-MM-dd";
    //public static final String DISPLAY_FORMAT = "dd MMM yyy";

    private static final String TAG = "FormatTime";

    private static CustomLogging mLogging = CustomLogging.getInstance();

    public static String parseTime(String rawTime, int type){
        // database time format -> HH:mm:ss
        // convert to format -> HH:mm
        switch (type){
            case DATABASE_TIME_TO_DISPLAY_TIME:
                mLogging.debug(TAG, "Convert database date to display date");
                return toDisplayTime(rawTime);
            case DISPLAY_TIME_TO_DATABASE_TIME:
                return "";
            default:
                return "";
        }

    }

    private static String toDisplayTime(String rawTime){
        String[] times = rawTime.split(":"); // HH:mm:ss
        String displayTime = times[0] + ":" + times[1]; // HH:mm
        return displayTime;
    }

}
