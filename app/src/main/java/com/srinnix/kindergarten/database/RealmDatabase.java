package com.srinnix.kindergarten.database;

import com.srinnix.kindergarten.database.model.ContactParentRealm;
import com.srinnix.kindergarten.database.model.ContactTeacherRealm;
import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

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
                    ContactTeacherRealm c = realm12.createObject(ContactTeacherRealm.class);
                    c.bindData(((ContactTeacher) contact));
                }
            } else {
                for (Contact contact : arrayList) {
                    ContactParentRealm c = realm12.createObject(ContactParentRealm.class);
                    c.bindData(((ContactParent) contact));
                }
            }
        }, () -> {
            EventBus.getDefault().post(new MessageListContact(arrayList));
            loginDelegate.loginSuccessfully();
        });
    }


}
