package com.srinnix.kindergarten.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.srinnix.kindergarten.R;

/**
 * Created by DELL on 2/3/2017.
 */

public class ServiceUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean hasConnectivity = mConnectivityManager.getActiveNetworkInfo() != null
                && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                && mConnectivityManager.getActiveNetworkInfo().isConnected();

        if (!hasConnectivity) {
            Toast.makeText(context, R.string.noInteretConnection, Toast.LENGTH_SHORT).show();
        }

        return hasConnectivity;
    }
}
