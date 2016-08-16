package com.ifalot.sfie.util;

import android.text.format.DateUtils;

import java.util.Calendar;
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

}
