package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * Created by DELL on 2/3/2017.
 */

public class UiUtils {
    public static int dpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    public static int pixelsToDp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) ((px / metrics.density) + 0.5);
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

        if (progressBar.getVisibility() == View.VISIBLE) {
            return;
        }

        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar == null) {
            return;
        }

        if (progressBar.getVisibility() != View.VISIBLE) {
            return;
        }

        progressBar.setVisibility(View.GONE);
        progressBar.setEnabled(false);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(250);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(250);

        v.startAnimation(a);
    }

    public static void expand(View v, View viewTimeline) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        final int heightViewTimeLine = viewTimeline.getHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();

                viewTimeline.getLayoutParams().height = heightViewTimeLine + (int) (targetHeight * interpolatedTime);
                viewTimeline.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

//        Health.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);
        v.startAnimation(a);
    }

    public static void collapse(View v, View viewTimeline) {
        final int initialHeight = v.getMeasuredHeight();
        final int initialHeightViewTimeLine = viewTimeline.getHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int heightAnimated = (int) (initialHeight * interpolatedTime);

                viewTimeline.getLayoutParams().height = initialHeightViewTimeLine - heightAnimated;
                viewTimeline.requestLayout();

                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    return;
                }

                v.getLayoutParams().height = initialHeight - heightAnimated;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

//        Health.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);

        v.startAnimation(a);
    }

    public static void showDatePickerDialog(Context mContext, String source, DatePickerDialog.OnDateSetListener listener) {
        if (TextUtils.isEmpty(source)) {
            showDatePickerDialog(mContext, listener);
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(source));

            showDatePickerDialog(mContext, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), listener);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void showDatePickerDialog(Context mContext, int year, int month, int date, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(mContext, listener, year, month, date);
        dialog.show();
    }

    public static void showDatePickerDialog(Context mContext, DatePickerDialog.OnDateSetListener listener) {
        Calendar now = Calendar.getInstance();
        showDatePickerDialog(mContext,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                listener);
    }

    public static void showTimePickerDialog(Context mContext, String time, TimePickerDialog.OnTimeSetListener listener) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(time));

            TimePickerDialog dialog = new TimePickerDialog(mContext, listener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
            dialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static int randomBackgroundActionTimeTable() {
        Random random = new Random();

        switch (random.nextInt(8)) {
            case 2:
            case 6: {
                return R.drawable.background_state_weight;
            }
            case 3:
            case 7: {
                return R.drawable.background_action_green;
            }
            default: {
                return R.drawable.background_state_height;
            }
        }
    }

    public static void hideView(View view) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    public static void showView(View view) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
