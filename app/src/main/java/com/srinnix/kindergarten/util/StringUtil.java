package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2/16/2017.
 */

public class StringUtil {

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

    private static String getNameContactTeacher(Context context, ContactTeacher contact) {
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
                    namesParent[size - 2], namesParent[size - 1], context.getString(R.string.baby),
                    namesChild[sizeChild - 2], namesChild[sizeChild - 1]);
        }

    }

    public static String getTime(long createdAt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm", Locale.getDefault());
        return dateFormat.format(new Date(createdAt));
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
}
