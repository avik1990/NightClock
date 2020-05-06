package com.app.nightclock.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static void setFontTypePrefs(String value, Context context) {
        Editor editor = context.getSharedPreferences("smsreader", 0).edit();
        editor.putString("FontType", value);
        editor.apply();
    }

    public static String getFontTypePrefs(Context context) {
        return context.getSharedPreferences("smsreader", 0).getString("FontType", "");
    }

    public static void setFontSizePrefs(String value, Context context) {
        Editor editor = context.getSharedPreferences("smsreader", 0).edit();
        editor.putString("FontSize", value);
        editor.apply();
    }

    public static String getFontSizePrefs(Context context) {
        return context.getSharedPreferences("smsreader", 0).getString("FontSize", "");
    }

    public static void setFontColor(String value, Context context) {
        Editor editor = context.getSharedPreferences("smsreader", 0).edit();
        editor.putString("fcolor", value);
        editor.apply();
    }

    public static String getFontColor(Context context) {
        return context.getSharedPreferences("smsreader", 0).getString("fcolor", "");
    }


    public static boolean getBtnClicked(Context context) {
        return context.getSharedPreferences("smsreader", 0).getBoolean("isAutoStartEnabled", false);
    }

    public static void setbuttonClicked(Context context, boolean value) {
        Editor editor = context.getSharedPreferences("smsreader", 0).edit();
        editor.putBoolean("isAutoStartEnabled", value);
        editor.commit();
    }


    public static boolean getChecked(Context context) {
        return context.getSharedPreferences("smsreader", 0).getBoolean("isChecked", true);
    }

    public static void setChecked(Context context, boolean value) {
        Editor editor = context.getSharedPreferences("smsreader", 0).edit();
        editor.putBoolean("isChecked", value);
        editor.commit();
    }

    public static String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    public static String getAMPM() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.AM_PM) == Calendar.AM) {
            // AM
            return "AM";
            //System.out.println(""+now.get(Calendar.HOUR)+":AM");
        } else {
            // PM
            return "PM";
            // System.out.println(""+now.get(Calendar.HOUR)+":PM");
        }
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    public static String PREF_KEY_APP_AUTO_START;

    public static void writeBoolean(Context context, String prefKeyAppAutoStart, boolean b) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_KEY_APP_AUTO_START, 0).edit();
        editor.putBoolean(PREF_KEY_APP_AUTO_START, b);
        editor.commit();
    }
}
