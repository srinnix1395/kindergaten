package com.srinnix.kindergarten.chat.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.srinnix.kindergarten.base.activity.DetailActivity;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.delegate.ChatListDelegate;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by DELL on 2/5/2017.
 */

public class ChatListPresenter extends BasePresenter {

    private ChatListDelegate mChatListDelegate;
    private CompositeDisposable mDisposable;

    public ChatListPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mChatListDelegate = (ChatListDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
    }

    public void onClickItemChat(Contact contact, String name, String urlImage) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(AppConstant.SCREEN_ID, AppConstant.FRAGMENT_DETAIL_CHAT);

        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, contact.getId());
        bundle.putString(AppConstant.KEY_NAME, name);
        bundle.putInt(AppConstant.KEY_STATUS, contact.getStatus());
        bundle.putString(AppConstant.KEY_IMAGE, urlImage);
        bundle.putInt(AppConstant.KEY_ACCOUNT_TYPE, contact instanceof ContactParent ?
                AppConstant.ACCOUNT_PARENTS : AppConstant.ACCOUNT_TEACHERS);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }

    public void onDisconnect(ArrayList<Contact> arrayList) {
        for (Contact contact : arrayList) {
            contact.setStatus(ChatConstant.STATUS_UNDEFINED);
        }
    }

    public void onSetupContactStatus(MessageContactStatus message, ArrayList<Contact> arrayList) {
        if (message.arrayList.size() == 0) {
            for (Contact contact : arrayList) {
                contact.setStatus(ChatConstant.STATUS_OFFLINE);
            }
        } else {
            for (Contact contact : arrayList) {
                if (message.arrayList.contains(contact.getId())) {
                    contact.setStatus(ChatConstant.STATUS_ONLINE);
                } else {
                    contact.setStatus(ChatConstant.STATUS_OFFLINE);
                }
            }
        }
    }

    public void getContactFromDatabase() {
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_PARENTS) {
                getContactTeacher();
            } else {
                getContactParent();
            }
        }
    }

    private void getContactParent() {
        if (mChatListDelegate == null) {
            return;
        }
        mDisposable.add(Single.fromCallable(() -> {
            ArrayList<ContactParent> arrayList = new ArrayList<>();

            Realm realm = Realm.getDefaultInstance();
            RealmResults<ContactParentRealm> results = realm.where(ContactParentRealm.class).findAll();
            for (ContactParentRealm result : results) {
                ContactParent contactParent = new ContactParent();

                contactParent.setId(result.getId());
                contactParent.setName(result.getName());
                contactParent.setGender(result.getGender());
                contactParent.setChildren(result.getChildren());

                arrayList.add(contactParent);
            }

            realm.close();

            MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);
            if (message != null) {
                if (message.arrayList.size() == 0) {
                    for (Contact contact : arrayList) {
                        contact.setStatus(ChatConstant.STATUS_OFFLINE);
                    }
                } else {
                    for (Contact contact : arrayList) {
                        if (message.arrayList.contains(contact.getId())) {
                            contact.setStatus(ChatConstant.STATUS_ONLINE);
                        } else {
                            contact.setStatus(ChatConstant.STATUS_OFFLINE);
                        }
                    }
                }
//                EventBus.getDefault().removeStickyEvent(MessageContactStatus.class);
            }

            return arrayList;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactParents -> mChatListDelegate.addContactParent(contactParents),
                        throwable -> ErrorUtil.handleException(mContext, throwable)));
    }

    private void getContactTeacher() {
        if (mChatListDelegate == null) {
            return;
        }
        mDisposable.add(Single.fromCallable(() -> {
            ArrayList<ContactTeacher> arrayList = new ArrayList<>();

            Realm realm = Realm.getDefaultInstance();
            RealmResults<ContactTeacherRealm> results = realm.where(ContactTeacherRealm.class).findAll();
            for (ContactTeacherRealm result : results) {
                ContactTeacher contactTeacher = new ContactTeacher();

                contactTeacher.setId(result.getId());
                contactTeacher.setName(result.getName());
                contactTeacher.setGender(result.getGender());
                contactTeacher.setImage(result.getImage());
                contactTeacher.setClassName(result.getClassName());

                arrayList.add(contactTeacher);
            }

            realm.close();

            MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);
            if (message != null) {
                if (message.arrayList.size() == 0) {
                    for (Contact contact : arrayList) {
                        contact.setStatus(ChatConstant.STATUS_OFFLINE);
                    }
                } else {
                    for (Contact contact : arrayList) {
                        if (message.arrayList.contains(contact.getId())) {
                            contact.setStatus(ChatConstant.STATUS_ONLINE);
                        } else {
                            contact.setStatus(ChatConstant.STATUS_OFFLINE);
                        }
                    }
                }
                EventBus.getDefault().removeStickyEvent(MessageContactStatus.class);
            }
            return arrayList;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactTeachers -> mChatListDelegate.addContactTeacher(contactTeachers),
                        throwable -> ErrorUtil.handleException(mContext, throwable)));
    }

    public void onUserConnect(ArrayList<Contact> listContact, MessageUserConnect message, ChatListAdapter mAdapter) {
        int i = 0;
        for (Contact contact : listContact) {
            if (contact.getId().equals(message.id)) {
                contact.setStatus(message.isConnected ? ChatConstant.STATUS_ONLINE : ChatConstant.STATUS_OFFLINE);
                break;
            }
            i++;
        }
        if (i != listContact.size() && mChatListDelegate != null) {
            mChatListDelegate.updateStatus(i, listContact.get(i).getStatus());
        }
    }
}
