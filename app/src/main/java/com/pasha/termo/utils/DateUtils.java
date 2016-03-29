package com.pasha.termo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String DATE_TIME_PATTERN_SEC = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN_SEC, Locale.getDefault());
        return sdf.format(date);
    }

    public static Date stringToDate(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN_SEC, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String timeToString(Date date, boolean is1x1) {
        SimpleDateFormat sdf = new SimpleDateFormat(is1x1 ? "HH\nmm" : "HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
