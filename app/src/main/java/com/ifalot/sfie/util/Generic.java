package com.ifalot.sfie.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Generic {

    public static Date getMidnight(){
        long now = new Date().getTime();
        TimeZone tz = new GregorianCalendar().getTimeZone();
        int offset = tz.getRawOffset() + tz.getDSTSavings();
        return new Date(now - now % DateUtils.DAY_IN_MILLIS - offset);
    }

    public static long date2Timestamp(int year, int month, int day){
        return new GregorianCalendar(year, month, day).getTimeInMillis();
    }

    public static void fastErrorDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setNeutralButton("CLOSE", null);
        builder.show();
    }

}
