package com.srinnix.kindergarten.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by DELL on 2/3/2017.
 */

public class ServiceUtils {
	public static boolean isNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			return (mConnectivityManager.getActiveNetworkInfo() != null
					&& mConnectivityManager.getActiveNetworkInfo().isAvailable()
					&& mConnectivityManager.getActiveNetworkInfo().isConnected());
		}
		return false;
	}
}
