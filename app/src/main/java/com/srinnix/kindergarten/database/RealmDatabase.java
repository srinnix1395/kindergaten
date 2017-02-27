package com.srinnix.kindergarten.database;

import com.srinnix.kindergarten.login.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by anhtu on 2/24/2017.
 */

public class RealmDatabase {
    public static void insertContact(Realm realm, ArrayList<Contact> arrayList
            , LoginDelegate loginDelegate) {
        if (arrayList.size() <= 0) {
            loginDelegate.loginSuccessfully();
            return;
        }
        realm.executeTransactionAsync(realm12 -> {
            if (arrayList.get(0) instanceof ContactTeacher) {
                for (Contact contact : arrayList) {
                    ContactTeacher c = realm12.copyToRealm((ContactTeacher) contact);
                }
            } else {
                for (Contact contact : arrayList) {
                    ContactParent c = realm12.copyToRealm((ContactParent) contact);
                }
            }
        }, () -> {
            EventBus.getDefault().post(new MessageListContact(arrayList));
            loginDelegate.loginSuccessfully();
        });
    }

    public static ArrayList<Message> getPreviousMessage(Realm realm, int page) {


    }
}
