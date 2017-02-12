package com.srinnix.kindergarten.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.srinnix.kindergarten.R;

/**
 * Created by DELL on 2/3/2017.
 */

public class AlertUtils {

    public static void showSnackBarNoInternet(View view) {
        final Snackbar snackbar = Snackbar.make(view, R.string.noInternetConnection, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.close, v -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        snackbar.show();
        //// TODO: 2/3/2017 snackbar
    }

    public static void showToast(Context context, int resID) {
        Toast.makeText(context, context.getString(resID), Toast.LENGTH_SHORT).show();
    }
}
