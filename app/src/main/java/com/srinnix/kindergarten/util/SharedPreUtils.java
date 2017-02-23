package com.srinnix.kindergarten.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ModelConstant;
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
	
	public boolean isUserSignedIn(){
		return sharedPreferences.getBoolean(AppConstant.IS_USER_SIGNED_IN, false);
	}
	
	public int getCurrentUserID() {
		return sharedPreferences.getInt(AppConstant.USER_ID, -1);
	}
	
	public int getCurrentAccountType() {
		return sharedPreferences.getInt(AppConstant.USER_TYPE, ModelConstant.GUESTS);
	}

	public void setLastEmailFragmentLogin(String email) {
		putString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, email);
	}

	public String getLastEmailFragmentLogin() {
		return sharedPreferences.getString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, "");
	}

    public void saveUserData(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstant.USER_ID, user.getId());
        editor.putString(AppConstant.EMAIL, user.getEmail());
        editor.putString(AppConstant.NAME, user.getName());
        editor.putInt(AppConstant.USER_TYPE, user.getAccountType());
        editor.putString(AppConstant.TOKEN, user.getToken());
        editor.apply();
    }

    public void clearUserData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(AppConstant.USER_ID);
        editor.remove(AppConstant.EMAIL);
        editor.remove(AppConstant.NAME);
        editor.remove(AppConstant.USER_TYPE);
        editor.remove(AppConstant.TOKEN);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(AppConstant.TOKEN, "");
    }
}
