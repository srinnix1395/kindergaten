package com.srinnix.kindergarten.login.helper;

import android.app.ProgressDialog;
import android.content.Context;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.messageeventbus.MessageLogout;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

/**
 * Created by anhtu on 4/10/2017.
 */

public class LogoutHelper {

    public static void signOut(Context context) {
        ProgressDialog dialog = AlertUtils.showDialogSignOut(context, R.string.signing_out);

        if (EventBus.getDefault().hasSubscriberForEvent(MessageLogout.class)) {
            EventBus.getDefault().post(new MessageLogout());
        }
        SharedPreUtils.getInstance(context).clearUserData();
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
