package com.srinnix.kindergarten.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by anhtu on 3/4/2017.
 */
public class KindergartenFcmInstanceIDService extends FirebaseInstanceIdService {
    private Disposable mDisposable;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        DebugLog.i("onTokenRefresh: " + token);

        SharedPreUtils.getInstance(this).setHasDeviceToken(false);

        boolean isUserLoggedIn = SharedPreUtils.getInstance(this).isUserSignedIn();
        if (isUserLoggedIn && ServiceUtils.isNetworkAvailable(this)) {
            String id = SharedPreUtils.getInstance(this).getUserID();
            UpdateFirebaseRegId.updateRegId(this, mDisposable, token, id, token);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}