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
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 24 * 7 * HOUR_MILLIS;
    private static final long MONTH_MILLIS = DAY_MILLIS * 30;
    private static final long YEAR_MILLIS = 365 * DAY_MILLIS;

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

        if (contact.getGender().equals(AppConstant.MALE)) {
            return String.format("%s %s %s - %s %s %s", context.getString(R.string.Mr),
                    namesParent[size - 2], namesParent[size - 1], context.getString(R.string.baby),
                    namesChild[sizeChild - 2], namesChild[sizeChild - 1]);
        } else {
            return String.format("%s %s %s - %s %s %s", context.getString(R.string.Ms),
                    namesParent[size - 2].equalsIgnoreCase("thị") ? "" : namesParent[size - 2]
                    , namesParent[size - 1], context.getString(R.string.baby),
                    namesChild[sizeChild - 2], namesChild[sizeChild - 1]).trim();
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
            return Html.fromHtml("<b>" + comment.getName() + "</b> " + comment.getComment());
        } else {
            return Html.fromHtml("<b>" + comment.getName() + "</b> " + comment.getComment(), Html.FROM_HTML_MODE_COMPACT);
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

    public static int getDrawableState(int status) {
        switch (status) {
            case ChatConstant.STATUS_ONLINE: {
                return R.drawable.ic_state_online;
            }
            case ChatConstant.STATUS_OFFLINE: {
                return R.drawable.ic_state_offline;
            }
            default: {
                return 0;
            }
        }
    }
}
