package com.srinnix.kindergarten.chat.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by DELL on 2/5/2017.
 */

public class ChatListPresenter extends BasePresenter {

    public ChatListPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void onClickItemChat(Object o) {

    }

    public void onDisconnect(ArrayList<Contact> arrayList, ChatListAdapter adapter) {
        Observable.fromIterable(arrayList)
                .doOnNext(contact -> contact.setStatus(ChatConstant.UNDEFINED))
                .doOnComplete(adapter::notifyDataSetChanged);
    }

    public void onSetupContactList(MessageListContact message, ArrayList<Contact> arrayList, ChatListAdapter adapter) {
        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }
        arrayList.addAll(message.arrayList);
        adapter.notifyDataSetChanged();
    }

    public void onSetupContactStatus(MessageContactStatus message, ArrayList<Contact> arrayList, ChatListAdapter adapter) {
        Observable.fromIterable(arrayList)
                .doOnNext(contact -> {
                    if (message.arrayList.contains(contact.getId())) {
                        contact.setStatus(ChatConstant.ONLINE);
                    } else {
                        contact.setStatus(ChatConstant.OFFLINE);
                    }
                }).doOnComplete(adapter::notifyDataSetChanged);
//        for (Contact contact : arrayList) {
//            if (message.arrayList.contains(contact.getId())) {
//                contact.setStatus(ChatConstant.ONLINE);
//            } else {
//                contact.setStatus(ChatConstant.OFFLINE);
//            }
//        }
//        adapter.notifyDataSetChanged();
    }

    public void getContactFromDatabase(Realm realm, ArrayList<Contact> contacts, ChatListAdapter adapter) {
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            Single.fromCallable(new Callable<ArrayList<Contact>>() {
                @Override
                public ArrayList<Contact> call() throws Exception {
                    realm.where(Contact.class).findAll()
                    return null;
                }
            })
                    .map((Function<RealmResults, ArrayList<Contact>>) ArrayList::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(arrayList -> {
                        if (contacts.size() > 0) {
                            contacts.clear();
                        }
                        contacts.addAll(arrayList);
                        adapter.notifyDataSetChanged();
                    });
        }
    }
}
