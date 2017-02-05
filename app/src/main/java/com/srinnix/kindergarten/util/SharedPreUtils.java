package com.srinnix.kindergarten.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.srinnix.kindergarten.constant.AppConstant;

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
}