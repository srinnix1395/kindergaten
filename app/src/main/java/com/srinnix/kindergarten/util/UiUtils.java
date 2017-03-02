package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.Calendar;

/**
 * Created by DELL on 2/3/2017.
 */

public class UiUtils {
    public static int dpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        metrics = context.getResources().getDisplayMetrics();
        return (int)((dp * metrics.density) + 0.5);
    }

    public static int pixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        metrics = context.getResources().getDisplayMetrics();
        return (int) ((px/metrics.density)+0.5);
    }

    // Prevent dialog dismiss when orientation changes
    public static void doKeepDialog(Dialog dialog) {
        if (dialog == null) {
            return;
        }

        if (dialog.getWindow() == null) {
            return;
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showProgressBar(ProgressBar progressBar) {
        if (progressBar == null) {
            return;
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar == null) {
            return;
        }

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setEnabled(false);
    }

    public static String convertDateTime(long dateTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateTime);
        return c.get(Calendar.DATE) +"/" + (c.get(Calendar.MONTH) + 1) + "/" +c.get(Calendar.YEAR)
                + " lúc " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }
}
