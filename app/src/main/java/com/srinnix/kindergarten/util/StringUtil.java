package com.srinnix.kindergarten.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2/16/2017.
 */

public class StringUtil {
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final long WEEK_MILLIS = 24 * 7 * HOUR_MILLIS;
    public static final long MONTH_MILLIS = DAY_MILLIS * 30;
    public static final long YEAR_MILLIS = 365 * DAY_MILLIS;

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNameContact(Context context, Contact contact) {
        if (contact instanceof ContactParent) {
            return getNameContactParent(context, (ContactParent) contact);
        } else {
            return getNameContactTeacher(context, (ContactTeacher) contact);
        }
    }

    public static String getNameContactTeacher(Context context, ContactTeacher contact) {
        String[] names = contact.getName().split(" ");
        int size = names.length;

        if (contact.getGender().equals(AppConstant.MALE)) {
            return String.format("%s %s %s - %s", context.getString(R.string.MrTeacher),
                    names[size - 2], names[size - 1], contact.getClassName());
        } else {
            return String.format("%s %s %s - %s", context.getString(R.string.MsTeacher),
                    names[size - 2], names[size - 1], contact.getClassName());
        }
    }

    private static String getNameContactParent(Context context, ContactParent contact) {
        String[] namesParent = contact.getName().split(" ");
        int size = namesParent.length;

        Child children = contact.getChildren().get(0);
        String[] namesChild = children.getName().split(" ");
        int sizeChild = namesChild.length;

        String middleName;
        if (namesParent[size - 2].equalsIgnoreCase("thị") ||
                namesParent[size - 2].equalsIgnoreCase("văn")) {
            middleName = "";
        } else {
            middleName = " " + namesParent[size - 2];
        }

        String nameChild;
        if (namesChild[sizeChild - 2].equalsIgnoreCase("thị") ||
                namesChild[sizeChild - 2].equalsIgnoreCase("văn")) {
            nameChild = namesChild[sizeChild - 1];
        } else {
            nameChild = namesChild[sizeChild - 2] + " " + namesChild[sizeChild - 1];
        }

        if (contact.getGender().equals(AppConstant.MALE)) {
            return String.format("%s%s %s - %s %s", context.getString(R.string.Mr), middleName, namesParent[size - 1], context.getString(R.string.baby), nameChild);
        } else {
            return String.format("%s%s %s - %s %s", context.getString(R.string.Ms), middleName, namesParent[size - 1], context.getString(R.string.baby), nameChild);
        }

    }

    public static String getTimeAgo(Context context, long time) {
        long now = System.currentTimeMillis();

        long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.just_now);
        }
        if (diff < 60 * MINUTE_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / MINUTE_MILLIS, context.getString(R.string.minutes_ago));
        }
        if (diff < 24 * HOUR_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / HOUR_MILLIS, context.getString(R.string.hour_ago));
        }
        if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday);
        }
        if (diff < WEEK_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / DAY_MILLIS, context.getString(R.string.days_ago));
        }

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(time);
        if (calendar.get(Calendar.YEAR) - currentYear == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM - kk:mm", Locale.getDefault());
            return dateFormat.format(new Date(time)).replace("-", "lúc");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy - kk:mm", Locale.getDefault());
            return dateFormat.format(new Date(time)).replace("-", "lúc");
        }
    }

    public static String getTimeAgoComment(Context context, long time) {
        long now = System.currentTimeMillis();

        long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.just_now);
        }
        if (diff < 60 * MINUTE_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / MINUTE_MILLIS, context.getString(R.string.minute));
        }
        if (diff < 24 * HOUR_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / HOUR_MILLIS, context.getString(R.string.hour));
        }
        if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday);
        }
        if (diff < WEEK_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / DAY_MILLIS, context.getString(R.string.day));
        }

        if (diff < MONTH_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / WEEK_MILLIS, context.getString(R.string.week));
        }

        if (diff < YEAR_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / MONTH_MILLIS, context.getString(R.string.week));
        }

        return String.format(Locale.getDefault(), "%d %s", diff / YEAR_MILLIS, context.getString(R.string.week));
    }

    public static String getStatus(Context context, int status) {
        switch (status) {
            case ChatConstant.STATUS_ONLINE: {
                return context.getString(R.string.online);
            }
            case ChatConstant.STATUS_OFFLINE: {
                return context.getString(R.string.offline);
            }
            default: {
                return "";
            }
        }
    }

    public static String getNameChildren(String name) {
        String[] names = name.split(" ");
        int size = names.length;

        return String.format("%s %s",
                names[size - 2].equalsIgnoreCase("thị") ? "" : names[size - 2],
                names[size - 1]).trim();
    }

    public static Spanned getComment(Comment comment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml("<b><font face=\"sans-serif-light\">" + comment.getName() + "</font></b>  " + comment.getComment());
        } else {
            return Html.fromHtml("<b><font face=\"sans-serif-light\">" + comment.getName() + "</font></b>  " + comment.getComment(), Html.FROM_HTML_MODE_COMPACT);
        }
    }

    public static String randomNewPassword(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getTimeHealthIndex(String measureTime) {
        return measureTime.substring(measureTime.indexOf("/") + 1);
    }

    public static int getAccountType(int accountType) {
        if (accountType == AppConstant.ACCOUNT_PARENTS) {
            return R.string.parents;
        } else {
            return R.string.teacher;
        }
    }

    public static boolean isScheduleTimeValid(int[] schedule) {
        long now = System.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.set(schedule[0],
                schedule[1]-1,
                schedule[2],
                schedule[3],
                schedule[4]);

        return (c.getTimeInMillis() - now) >= (MINUTE_MILLIS * 10);
    }
}
