package com.srinnix.kindergarten.login.helper;

import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LoginResponse;
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
        if (mListener == null) {
            return;
        }
        mApi.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(mListener::onFinally)
                .subscribe(mListener::onResponseSuccess, mListener::onResponseFail);
    }

    public void insertData(Realm realm, ArrayList<Child> children, ArrayList<Contact> arrayList, LoginDelegate loginDelegate) {
        realm.executeTransactionAsync(realm12 -> {
            if (arrayList == null || arrayList.size() == 0) {
                if (loginDelegate != null) {
                    loginDelegate.loginSuccessfully();
                }
                return;
            }

            for (Child child : children) {
                realm12.copyToRealm(child);
            }

            if (arrayList.get(0) instanceof ContactTeacher) {
                for (Contact contact : arrayList) {
                    ContactTeacherRealm c = new ContactTeacherRealm((ContactTeacher) contact);
                    realm12.copyToRealmOrUpdate(c);
                }
            } else {
                for (Contact contact : arrayList) {
                    ContactParentRealm c = new ContactParentRealm(((ContactParent) contact));
                    realm12.copyToRealmOrUpdate(c);
                }
            }
        }, () -> {
            if (loginDelegate != null) {
                loginDelegate.loginSuccessfully();
            }
            EventBus.getDefault().post(new MessageLoginSuccessfully());
            if (!realm.isClosed()) {
                realm.close();
            }
        });
    }

    public interface LoginListener {

        void onResponseSuccess(ApiResponse<LoginResponse> dataLogin);

        void onResponseFail(Throwable throwable);

        void onFinally();
    }
}
