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
    public static final int FACEBOOK_DATE_TO_DATABASE_DATE = 2;

    public static final String FACEBOOK_FORMAT = "MM/dd/yyyy";
    public static final String DATABASE_FORMAT = "yyyy-MM-dd";
    public static final String DISPLAY_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_FULL_FORMAT = "dd MMMM yyyy cccc";

    private static final String TAG = "FormatDate";

    private static final CustomLogging mLogging = CustomLogging.getInstance();
    private static final Parser mParser = Parser.getInstance();

    public static String parseDate(String rawDate, int formatType, String format){
        // database date format -> yyyy-mm-dd
        // convert to format -> dd MMM yyy
        switch (formatType){
            case DATABASE_DATE_TO_DISPLAY_DATE:
                mLogging.debug(TAG, "Convert database date to display date");
                return toDisplayDate(rawDate, format);

            case DISPLAY_DATE_TO_DATABASE_DATE:
                mLogging.debug(TAG, "Convert display date to database date");
                return toDatabaseDate(rawDate, format);

            case FACEBOOK_DATE_TO_DATABASE_DATE:
                mLogging.debug(TAG, "Convert facebook date to database date");
                return facebookToDatabaseDate(rawDate);
            default:
                return "";
        }
    }

    public static int getYear(String date, String format){
        String[] strs = null;

        switch (format){
            case DATABASE_FORMAT: // yyyy-MM-dd
                strs = date.split("-");
                return mParser.convertStringToInteger(strs[0]);

            case FACEBOOK_FORMAT: // MM/dd/yyyy
                strs = date.split("/");
                return mParser.convertStringToInteger(strs[2]);

            default:
                return 0;
        }
    }

    public static int getMonth(String date, String format){
        String[] strs = null;
        // month in java calendar is in index, starting from 0
        switch (format){
            case DATABASE_FORMAT: // yyyy-MM-dd
                strs = date.split("-");
                return mParser.convertStringToInteger(strs[1]) - 1;

            case FACEBOOK_FORMAT: // MM/dd/yyyy
                strs = date.split("/");
                return mParser.convertStringToInteger(strs[0]) - 1;

            default:
                return 0;
        }
    }

    public static int getDay(String date, String format){
        String[] strs = null;
        // month in java calendar is in index, starting from 0
        switch (format){
            case DATABASE_FORMAT: // yyyy-MM-dd
                strs = date.split("-");
                return mParser.convertStringToInteger(strs[2]);

            case FACEBOOK_FORMAT: // MM/dd/yyyy
                strs = date.split("/");
                return mParser.convertStringToInteger(strs[1]);

            default:
                return 0;
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

    private static String toDatabaseDate(String rawDate, String format){
        DateFormat inputFormat = new SimpleDateFormat(DISPLAY_FORMAT);
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

    private static String facebookToDatabaseDate(String rawDate){
        DateFormat inputFormat = new SimpleDateFormat(FACEBOOK_FORMAT);
        DateFormat outputFormat = new SimpleDateFormat(DATABASE_FORMAT);
        Date parsed = new Date();
        try
        {
            parsed = inputFormat.parse(rawDate);
            String outputText = outputFormat.format(parsed);
            mLogging.debug(TAG, "facebookToDatabaseDate outputText => " + outputText);
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
