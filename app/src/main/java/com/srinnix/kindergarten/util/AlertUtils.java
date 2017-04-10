package com.srinnix.kindergarten.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

    public static void showAlertDialog(Context context, int resTitle, int resMessage, int resNegative, OnClickListener listener){
        new AlertDialog.Builder(context)
                .setTitle(resTitle)
                .setMessage(resMessage)
                .setNegativeButton(resNegative, (dialog, which) -> {
                    if (listener != null) {
                        listener.onClick();
                    }
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    public static ProgressDialog showDialogSignOut(Context context, int resMessage) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setMessage(context.getString(resMessage));
        dialog.show();
        return dialog;
    }

    public interface OnClickListener {
        void onClick();
    }
}
