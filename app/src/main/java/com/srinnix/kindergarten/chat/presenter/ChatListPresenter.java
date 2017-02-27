package com.srinnix.kindergarten.chat.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.activity.DetailChatActivity;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.database.mode.ContactParentRealm;
import com.srinnix.kindergarten.database.mode.ContactTeacherRealm;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.parceler.Parcels;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public void onClickItemChat(Contact contact) {
        Intent intent = new Intent(mContext, DetailChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.KEY_INFO, Parcels.wrap(contact));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
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
    }

    public void getContactFromDatabase(Realm realm, ArrayList<Contact> contacts, ChatListAdapter adapter) {
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {

            if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.PARENTS) {
                getContactTeacher(realm, contacts, adapter);
            } else {
                getContactParent(realm, contacts, adapter);
            }
        }
    }

    private void getContactParent(Realm realm, ArrayList<Contact> contacts, ChatListAdapter adapter) {
        Single.fromCallable(() -> {
            ArrayList<ContactParent> arrayList = new ArrayList<>();

            RealmResults<ContactParentRealm> results = realm.where(ContactParentRealm.class).findAll();
            for (ContactParentRealm result : results) {
                ContactParent contactParent = new ContactParent();

                contactParent.setId(result.getId());
                contactParent.setName(result.getName());

                arrayList.add(contactParent);
            }
            return arrayList;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactParents -> {
                    if (contacts.size() > 0) {
                        contacts.clear();
                    }
                    contacts.addAll(contactParents);
                    adapter.notifyDataSetChanged();
                });

    }

    private void getContactTeacher(Realm realm, ArrayList<Contact> contacts, ChatListAdapter adapter) {
        Single.fromCallable(() -> {
            ArrayList<ContactTeacher> arrayList = new ArrayList<>();

            RealmResults<ContactTeacherRealm> results = realm.where(ContactTeacherRealm.class).findAll();
            for (ContactTeacherRealm result : results) {
                ContactTeacher contactTeacher = new ContactTeacher();

                contactTeacher.setId(result.getId());
                contactTeacher.setName(result.getName());
                contactTeacher.setImage(result.getImage());
                contactTeacher.setClassName(result.getClassName());

                arrayList.add(contactTeacher);
            }
            return arrayList;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactTeachers -> {
                    if (contacts.size() > 0) {
                        contacts.clear();
                    }
                    contacts.addAll(contactTeachers);
                    adapter.notifyDataSetChanged();
                });
    }
}
