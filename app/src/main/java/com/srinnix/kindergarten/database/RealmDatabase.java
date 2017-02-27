package com.srinnix.kindergarten.database;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.database.mode.ContactParentRealm;
import com.srinnix.kindergarten.database.mode.ContactTeacherRealm;
import com.srinnix.kindergarten.login.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

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

    public static ArrayList<Message> getPreviousMessage(Realm realm, String conversationID, long timePrevMessage) {
        List<Message> messages = realm.where(Message.class)
                .equalTo("conversationID", conversationID)
                .lessThan("created_at", timePrevMessage)
                .findAll()
                .sort("created_at", Sort.DESCENDING)
                .subList(0, ChatConstant.ITEM_MESSAGE_PER_PAGE);
        return (ArrayList<Message>) messages;
    }
}
