package com.newiplquizgame.myipl.managers;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManagerFile {

    SharedPreferences moSharedPreferences;
    public static String FILE_NAME = "";
    public static String SESSION_GUID = "SessionGUID";
    public static String USER_PROFILE_PIC = "UserProfilePic";
    public static String USER_NAME = "UserName";
    public static String EMAIL = "Email";
    public static String ISLOGIN = "Login";
    public static String GROUP_ID = "GroupId";

    public SharedPreferenceManagerFile(Context mContext) {
        try {
            moSharedPreferences = mContext.getApplicationContext().getSharedPreferences(FILE_NAME, 0 | Context.MODE_MULTI_PROCESS);
        } catch (Exception e) {
            moSharedPreferences = mContext.getApplicationContext().getSharedPreferences(FILE_NAME, 0);
        }
    }

    public void setInSharedPreference(String fsKey, String fsValue) {
        if (moSharedPreferences != null && fsValue != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(fsKey, fsValue);
            loEditor.commit();
        }
    }

    public void setBooleanSharedPreference(String fsKey, Boolean fsValue) {
        if (moSharedPreferences != null && fsValue != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putBoolean(fsKey, true);
            loEditor.commit();
        }
    }
    public void setIntSharedPreference(String fsKey, Integer fsValue) {
        if (moSharedPreferences != null && fsValue != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putInt(fsKey, 0);
            loEditor.commit();
        }
    }
    public Integer getIntSharedPreference(String fsKey) {

        if (moSharedPreferences != null) {
            return moSharedPreferences.getInt(fsKey, 0);
        }
        return null;
    }

    public Boolean getBooleanSharedPreference(String fsKey) {

        if (moSharedPreferences != null) {
            return moSharedPreferences.getBoolean(fsKey, false);
        }
        return null;
    }

    public String getFromSharedPreference(String fsKey) {

        if (moSharedPreferences != null) {
            return moSharedPreferences.getString(fsKey, null);
        }
        return null;
    }

    public Boolean clearPreference() {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(USER_NAME, "");
            loEditor.putString(EMAIL, "");
            loEditor.putString(SESSION_GUID, "");
            loEditor.putString(USER_PROFILE_PIC, "");
            loEditor.putBoolean(ISLOGIN, false);
            loEditor.commit();
        }
        return true;
    }
}
