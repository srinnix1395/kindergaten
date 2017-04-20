package com.srinnix.kindergarten.chat.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.delegate.ChatListDelegate;
import com.srinnix.kindergarten.chat.fragment.ChatListFragment;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

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

    public void onClickItemChat(ChatListFragment fragment, Contact contact, String name, String urlImage) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, contact.getId());
        bundle.putString(AppConstant.KEY_NAME, name);
        bundle.putInt(AppConstant.KEY_STATUS, contact.getStatus());
        bundle.putString(AppConstant.KEY_IMAGE, urlImage);
        bundle.putInt(AppConstant.KEY_ACCOUNT_TYPE, contact instanceof ContactParent ?
                AppConstant.ACCOUNT_PARENTS : AppConstant.ACCOUNT_TEACHERS);

        ViewManager.getInstance().addFragment(new DetailChatFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);

        ((MainFragment) fragment.getParentFragment()).closeDrawer();
    }

    public void onDisconnect(ArrayList<Object> arrayList) {
        for (Object obj : arrayList) {
            if (obj instanceof Contact) {
                ((Contact) obj).setStatus(ChatConstant.STATUS_UNDEFINED);
            }
        }
    }

    public void onSetupContactStatus(MessageContactStatus message, ArrayList<Object> arrayList) {
        if (message.arrayList.size() == 0) {
            for (Object contact : arrayList) {
                if (contact instanceof Contact) {
                    ((Contact) contact).setStatus(ChatConstant.STATUS_OFFLINE);
                }
            }
        } else {
            for (Object contact : arrayList) {
                if (contact instanceof Contact) {
                    if (message.arrayList.contains(((Contact) contact).getId())) {
                        ((Contact) contact).setStatus(ChatConstant.STATUS_ONLINE);
                    } else {
                        ((Contact) contact).setStatus(ChatConstant.STATUS_OFFLINE);
                    }
                }
            }
        }
    }

    public void getContactFromDatabase() {
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_PARENTS) {
                getContactTeacher();
            } else {
                getContactParents();
            }
        }
    }

    private void getContactParents() {
        if (mChatListDelegate == null) {
            return;
        }
        mDisposable.add(Single.fromCallable(() -> {
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.add("Lớp mình");

            Realm realm = Realm.getDefaultInstance();
            RealmResults<ContactParentRealm> results = realm.where(ContactParentRealm.class).findAll();

            boolean isAddHeaderOther = false;

            MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);

            for (ContactParentRealm result : results) {
                ContactParent contactParent = new ContactParent();

                contactParent.setId(result.getId());
                contactParent.setName(result.getName());
                contactParent.setGender(result.getGender());
                contactParent.setChildren(result.getChildren());

                if (message != null) {
                    if (message.arrayList.size() == 0) {
                        contactParent.setStatus(ChatConstant.STATUS_OFFLINE);
                    } else {
                        if (message.arrayList.contains(contactParent.getId())) {
                            contactParent.setStatus(ChatConstant.STATUS_ONLINE);
                        } else {
                            contactParent.setStatus(ChatConstant.STATUS_OFFLINE);
                        }
                    }
                }

                if (!isAddHeaderOther && !result.isMyClass()) {
                    arrayList.add("Lớp khác");
                    isAddHeaderOther = true;
                }

                arrayList.add(contactParent);
            }
            realm.close();

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

            MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);

            for (ContactTeacherRealm result : results) {
                ContactTeacher contactTeacher = new ContactTeacher();

                contactTeacher.setId(result.getId());
                contactTeacher.setName(result.getName());
                contactTeacher.setGender(result.getGender());
                contactTeacher.setImage(result.getImage());
                contactTeacher.setClassName(result.getClassName());

                if (message != null) {
                    if (message.arrayList.size() == 0) {
                        contactTeacher.setStatus(ChatConstant.STATUS_OFFLINE);

                    } else {
                        if (message.arrayList.contains(contactTeacher.getId())) {
                            contactTeacher.setStatus(ChatConstant.STATUS_ONLINE);
                        } else {
                            contactTeacher.setStatus(ChatConstant.STATUS_OFFLINE);
                        }
                    }
                }

                arrayList.add(contactTeacher);
            }

            realm.close();

            return arrayList;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactTeachers -> mChatListDelegate.addContactTeacher(contactTeachers),
                        throwable -> ErrorUtil.handleException(mContext, throwable)));
    }

    public void onUserConnect(ArrayList<Object> listContact, MessageUserConnect message, ChatListAdapter mAdapter) {
        int i = 0;
        for (Object obj : listContact) {
            if (obj instanceof Contact && ((Contact) obj).getId().equals(message.id)) {
                ((Contact) obj).setStatus(message.isConnected ? ChatConstant.STATUS_ONLINE : ChatConstant.STATUS_OFFLINE);
                break;
            }
            i++;
        }
        if (i != listContact.size() && mChatListDelegate != null) {
            mChatListDelegate.updateStatus(i, message.isConnected ? ChatConstant.STATUS_ONLINE : ChatConstant.STATUS_OFFLINE);
        }
    }
}
