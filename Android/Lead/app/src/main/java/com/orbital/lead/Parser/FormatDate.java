package com.orbital.lead.Parser;

import com.orbital.lead.logic.CustomLogging;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joseph on 24/6/2015.
 */
public class FormatDate {
    public static final int DATABASE_DATE_TO_DISPLAY_DATE = 0;
    public static final int DISPLAY_DATE_TO_DATABASE_DATE = 1;

    public static final String DATABASE_FORMAT = "yyyy-MM-dd";
    public static final String DISPLAY_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_FULL_FORMAT = "dd MMMM yyyy cccc";

    private static final String TAG = "FormatDate";

    private static CustomLogging mLogging = CustomLogging.getInstance();

    public static String parseDate(String rawDate, int formatType, String format){
        // database date format -> yyyy-mm-dd
        // convert to format -> dd MMM yyy
        switch (formatType){
            case DATABASE_DATE_TO_DISPLAY_DATE:
                mLogging.debug(TAG, "Convert database date to display date");
                return toDisplayDate(rawDate, format);

            case DISPLAY_DATE_TO_DATABASE_DATE:
                return "";
            default:
                return "";
        }
    }

    private static String toDisplayDate(String rawDate, String format){
        DateFormat inputFormat = new SimpleDateFormat(DATABASE_FORMAT);
        DateFormat outputFormat = new SimpleDateFormat(format);
        Date parsed = new Date();
        try
        {
            parsed = inputFormat.parse(rawDate);
            String outputText = outputFormat.format(parsed);
            return outputText;
        }
        catch (ParseException e)
        {
            mLogging.debug(TAG, "ParseException => " + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }


}
