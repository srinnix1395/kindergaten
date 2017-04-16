package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.HashMap;

/**
 * Created by DELL on 2/3/2017.
 */

public class UiUtils {
    public static int dpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        metrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    public static int pixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
//        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        metrics = context.getResources().getDisplayMetrics();
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

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<>());

            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            ErrorUtil.handleException(e);
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
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

//        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
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

//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
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

//        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
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

//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);

        v.startAnimation(a);
    }
}
