package com.srinnix.kindergarten.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

        View viewSnackbar = snackbar.getView();
        TextView tv = (TextView) viewSnackbar.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        snackbar.show();
    }

    public static void showToast(Context context, int resID) {
        Toast.makeText(context, context.getString(resID), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastSuccess(Context context, int resIcon, int resString) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);

        ImageView imvIcon = (ImageView) view.findViewById(R.id.imvIcon);
        imvIcon.setImageResource(resIcon);

        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText(resString);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);

        toast.show();
    }
}
