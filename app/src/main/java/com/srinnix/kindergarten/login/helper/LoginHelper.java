package com.srinnix.kindergarten.login.helper;

import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;
import com.srinnix.kindergarten.request.remote.ApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by Administrator on 3/3/2017.
 */

public class LoginHelper {
    private ApiService mApi;

    public LoginHelper() {
        mApi = RetrofitClient.getApiService();
    }

    public void login(String email, String password, LoginListener mListener) {
        mApi.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mListener != null) {
                        mListener.onFinally();
                    }
                })
                .subscribe(response -> {
                            if (mListener != null) {
                                mListener.onResponseSuccess(response);
                            }
                        },
                        throwable -> {
                            if (mListener != null) {
                                mListener.onResponseFail(throwable);
                            }
                        });
    }

    public void insertContact(Realm realm, ArrayList<Contact> arrayList, LoginDelegate loginDelegate) {
        if (arrayList.size() <= 0) {
            loginDelegate.loginFail();
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

    public interface LoginListener {

        void onResponseSuccess(ApiResponse<DataLogin> dataLogin);

        void onResponseFail(Throwable throwable);

        void onFinally();
    }
}
