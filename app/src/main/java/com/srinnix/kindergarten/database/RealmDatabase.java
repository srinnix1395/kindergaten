package com.srinnix.kindergarten.database;

import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.exception.MessageNotFoundException;
import com.srinnix.kindergarten.database.model.ContactParentRealm;
import com.srinnix.kindergarten.database.model.ContactTeacherRealm;
import com.srinnix.kindergarten.login.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.request.remote.ApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
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

    public static void getPreviousMessage(Realm realm, String conversationID
            , ArrayList<Object> listMessage, ChatAdapter adapter, ApiService mApi, String token) {
        long timeFirstMessage;
        if (listMessage.size() == 1) {
            timeFirstMessage = System.currentTimeMillis();
        } else {
            timeFirstMessage = ((Message) listMessage.get(1)).getCreatedAt();
        }

        Single.fromCallable(() -> {
            RealmResults<Message> results = realm.where(Message.class)
                    .equalTo("conversationID", conversationID)
                    .lessThan("created_at", timeFirstMessage)
                    .findAllSorted("created_at", Sort.DESCENDING);

            ArrayList<Message> arrayList = new ArrayList<>();
            int size = results.size() > ChatConstant.ITEM_MESSAGE_PER_PAGE ? ChatConstant.ITEM_MESSAGE_PER_PAGE : results.size();
            for (int i = 0; i < size; i++) {
                arrayList.add(0, results.get(i));
            }

            if (arrayList.size() == 0) {
                throw new MessageNotFoundException();
            }

            return arrayList;
        }).doOnError(throwable -> mApi.getHistoryMessage(token, timeFirstMessage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    listMessage.addAll(1, messages);
                    adapter.notifyItemRangeInserted(1, messages.size());
                }, throwable -> {
                    ((LoadingItem) listMessage.get(0)).setLoadingState(LoadingItem.STATE_ERROR);
                    adapter.notifyItemChanged(0);
                });
    }
}
