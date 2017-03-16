package com.srinnix.kindergarten.chat.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.activity.DetailChatActivity;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.delegate.ChatListDelegate;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageUserDisconnect;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.util.AlertUtils;
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

    public void onClickItemChat(Contact contact) {
        Intent intent = new Intent(mContext, DetailChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.KEY_INFO, contact);
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
        mDisposable.add(Single.fromCallable(() -> {
            ArrayList<ContactParent> arrayList = new ArrayList<>();

            Realm realm = KinderApplication.getInstance().getRealm();
            RealmResults<ContactParentRealm> results = realm.where(ContactParentRealm.class).findAll();
            for (ContactParentRealm result : results) {
                ContactParent contactParent = new ContactParent();

                contactParent.setId(result.getId());
                contactParent.setName(result.getName());
                contactParent.setGender(result.getGender());
                contactParent.setChildren(result.getChildren());

                arrayList.add(contactParent);
            }

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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactParents -> {
                    if (mChatListDelegate != null) {
                        mChatListDelegate.addContactParent(contactParents);
                    }
                }, throwable -> AlertUtils.showToast(mContext, R.string.commonError)));
    }

    private void getContactTeacher() {
        mDisposable.add(Single.fromCallable(() -> {
            ArrayList<ContactTeacher> arrayList = new ArrayList<>();

            Realm realm = KinderApplication.getInstance().getRealm();
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactTeachers -> {
                    if (mChatListDelegate != null) {
                        mChatListDelegate.addContactTeacher(contactTeachers);
                    }
                }));
    }

    public void onUserDisconnect(ArrayList<Contact> listContact, MessageUserDisconnect message, ChatListAdapter mAdapter) {
        int i = 0;
        for (Contact contact : listContact) {
            if (contact.getId().endsWith(message.id)) {
                contact.setStatus(ChatConstant.STATUS_OFFLINE);
                break;
            }
            i++;
        }
        if (i != listContact.size() && mChatListDelegate != null) {
            mChatListDelegate.updateStatus(i);
        }
    }
}
