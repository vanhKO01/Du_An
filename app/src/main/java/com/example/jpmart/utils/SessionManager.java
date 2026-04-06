package com.example.jpmart.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "jpmart_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_SAVED_USER = "saved_user";
    private static final String KEY_SAVED_PASS = "saved_pass";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createSession(int userId, String username, String fullname, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public void logout() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_FULLNAME);
        editor.remove(KEY_ROLE);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getInt(KEY_USER_ID, -1) != -1;
    }

    public int getUserId() { return prefs.getInt(KEY_USER_ID, -1); }
    public String getUsername() { return prefs.getString(KEY_USERNAME, ""); }
    public String getFullname() { return prefs.getString(KEY_FULLNAME, ""); }
    public String getRole() { return prefs.getString(KEY_ROLE, ""); }
    public boolean isManager() { return "manager".equals(getRole()); }

    public void saveCredentials(String username, String password) {
        editor.putBoolean(KEY_REMEMBER, true);
        editor.putString(KEY_SAVED_USER, username);
        editor.putString(KEY_SAVED_PASS, password);
        editor.apply();
    }

    public void clearCredentials() {
        editor.putBoolean(KEY_REMEMBER, false);
        editor.remove(KEY_SAVED_USER);
        editor.remove(KEY_SAVED_PASS);
        editor.apply();
    }

    public boolean isRememberPassword() { return prefs.getBoolean(KEY_REMEMBER, false); }
    public String getSavedUsername() { return prefs.getString(KEY_SAVED_USER, ""); }
    public String getSavedPassword() { return prefs.getString(KEY_SAVED_PASS, ""); }
}
