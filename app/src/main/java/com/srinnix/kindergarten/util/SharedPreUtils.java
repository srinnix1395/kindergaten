package com.srinnix.kindergarten.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;

import java.util.ArrayList;

/**
 * Created by DELL on 2/3/2017.
 */

public class SharedPreUtils {
    private static SharedPreUtils mInstance = null;
    private SharedPreferences sharedPreferences;

    private SharedPreUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstant.KINDERGARTEN_SHARED_PREFERENCES, Context.MODE_APPEND);
    }

    public static SharedPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharedPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void putInt(String key, int vaue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, vaue);
        editor.apply();
    }

    public int getInt(String key, int defVal) {
        return sharedPreferences.getInt(key, defVal);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defVal) {
        return sharedPreferences.getString(key, defVal);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean isUserSignedIn() {
        return sharedPreferences.getBoolean(AppConstant.IS_USER_SIGNED_IN, false);
    }

    public String getUserID() {
        return sharedPreferences.getString(AppConstant.USER_ID, "-1");
    }

    public int getAccountType() {
        return sharedPreferences.getInt(AppConstant.USER_TYPE, AppConstant.ACCOUNT_GUESTS);
    }

    public String getAccountName() {
        return sharedPreferences.getString(AppConstant.NAME, "");
    }

    public void setLastEmailFragmentLogin(String email) {
        putString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, email);
    }

    public String getLastEmailFragmentLogin() {
        return sharedPreferences.getString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, "");
    }

    public String getClassId() {
        return sharedPreferences.getString(AppConstant._ID_CLASS, null);
    }

    public void saveUserData(User user, ArrayList<Child> children) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstant.IS_USER_SIGNED_IN, true);
        editor.putString(AppConstant.USER_ID, user.getId());
        editor.putString(AppConstant.EMAIL, user.getEmail());
        editor.putString(AppConstant.NAME, user.getName());
        editor.putString(AppConstant.GENDER, user.getGender());
        editor.putInt(AppConstant.USER_TYPE, user.getAccountType());
        if (user.getAccountType() == AppConstant.ACCOUNT_TEACHERS) {
            editor.putString(AppConstant._ID_CLASS, user.getIdClass());
            editor.putString(AppConstant.CLASS_NAME, user.getClassName());
            editor.putString(AppConstant.IMAGE, user.getImage());
        } else {
            editor.putString(AppConstant.IMAGE, !children.isEmpty() ? children.get(0).getImage() : "");
        }
        editor.putBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, true);
        editor.putString(AppConstant.TOKEN, user.getToken());
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(AppConstant.TOKEN, "");
    }

    public void setServerHasDeviceToken(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstant.SERVER_HAS_DEVICE_TOKEN, b);
        editor.apply();
    }

    public boolean getServerHasDeviceToken() {
        return sharedPreferences.getBoolean(AppConstant.SERVER_HAS_DEVICE_TOKEN, false);
    }

    public String getImage() {
        return sharedPreferences.getString(AppConstant.IMAGE, "");
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(AppConstant.EMAIL, "");
    }

    public boolean getFlagReceiveNotification() {
        return sharedPreferences.getBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, false);
    }

    public void setFlagReceiveNotification(boolean flag) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, flag);
        editor.apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(AppConstant.LANGUAGE, "Tiếng Việt");
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstant.LANGUAGE, language);
        editor.apply();
    }

    public String getGender() {
        return sharedPreferences.getString(AppConstant.GENDER, AppConstant.FEMALE);
    }

    public String getClassName() {
        return sharedPreferences.getString(AppConstant.CLASS_NAME, "");
    }
}
