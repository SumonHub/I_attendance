package com.infinity.attendance.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.infinity.attendance.data.model.User;


public class SharedPrefsHelper {

    public static User getSuperUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String stringUser = prefs.getString("SUPER_USER", null);
        return new Gson().fromJson(stringUser, User.class);
    }

    public static void setSuperUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString("SUPER_USER", new Gson().toJson(user)).apply();
    }

    public static void setCheckInStatus(Context context, boolean status) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean("CHECK_IN", status).apply();
    }

    public static boolean getCheckInStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getBoolean("CHECK_IN", false);
    }
}
