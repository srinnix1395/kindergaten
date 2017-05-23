package com.srinnix.kindergarten.login.helper;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.model.realm.ContactParentRealm;
import com.srinnix.kindergarten.model.realm.ContactTeacherRealm;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.service.UpdateFirebaseRegId;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by Administrator on 3/3/2017.
 */

public class LoginHelper extends BaseHelper {

    public LoginHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public Single<ApiResponse<LoginResponse>> login(String email, String password) {
        return mApiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void insertData(Realm realm, ArrayList<Child> children, ArrayList<Contact> arrayList) {
        realm.executeTransactionAsync(realm12 -> {
            if (children != null) {
                for (Child child : children) {
                    realm12.copyToRealm(child);
                }
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
            if (!realm.isClosed()) {
                realm.close();
            }
        });
    }

    public void updateRegId(Context mContext, User user, LoginDelegate loginDelegate) {
        String regID = FirebaseInstanceId.getInstance().getToken();
        UpdateFirebaseRegId.updateRegId(mContext, user.getToken(), user.getId(), regID, new UpdateFirebaseRegId.OnUpdateRegIdListener() {
            @Override
            public void onFinally() {
                if (loginDelegate != null) {
                    loginDelegate.loginSuccessfully();
                }
                EventBus.getDefault().post(new MessageLoginSuccessfully());
            }
        });
    }

    public Single<ApiResponse<Boolean>> resetPassword(String token, String idUser, String email, String newPassword,
                                                      String newPasswordEncrypted) {

        return mApiService.resetPassword(token, idUser, email, newPassword, newPasswordEncrypted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
