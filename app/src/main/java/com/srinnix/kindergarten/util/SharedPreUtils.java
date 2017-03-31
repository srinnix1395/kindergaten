package com.srinnix.kindergarten.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.User;

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

    public void saveUserData(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstant.IS_USER_SIGNED_IN, true);
        editor.putString(AppConstant.USER_ID, user.getId());
        editor.putString(AppConstant.EMAIL, user.getEmail());
        editor.putString(AppConstant.NAME, user.getName());
        editor.putInt(AppConstant.USER_TYPE, user.getAccountType());
        if (user.getAccountType() == AppConstant.ACCOUNT_TEACHERS) {
            editor.putString(AppConstant._ID_CLASS, user.getIdClass());
        }
        editor.putString(AppConstant._ID_SCHOOL, user.getIdSchool());
        editor.putString(AppConstant.TOKEN, user.getToken());
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(AppConstant.TOKEN, "");
    }

    public void setHasDeviceToken(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstant.HAS_DEVICE_TOKEN, b);
        editor.apply();
    }

    public boolean getHasDeviceToken() {
        return sharedPreferences.getBoolean(AppConstant.HAS_DEVICE_TOKEN, false);
    }

    public String getImage() {
        //todo image
        return "";
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(AppConstant.IS_USER_SIGNED_IN);
        editor.remove(AppConstant.USER_ID);
        editor.remove(AppConstant.EMAIL);
        editor.remove(AppConstant.NAME);
        editor.remove(AppConstant.USER_TYPE);
        editor.remove(AppConstant.TOKEN);
        if (sharedPreferences.contains(AppConstant._ID_CLASS)) {
            editor.remove(AppConstant._ID_CLASS);
        }
        editor.remove(AppConstant.IMAGE);
        editor.remove(AppConstant._ID_SCHOOL);
        editor.apply();
    }
}
