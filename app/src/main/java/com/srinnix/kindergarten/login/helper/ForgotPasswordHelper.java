package com.srinnix.kindergarten.login.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 4/18/2017.
 */

public class ForgotPasswordHelper {
    private ApiService mApiService;
    private CompositeDisposable mDisposable;

    public ForgotPasswordHelper(CompositeDisposable mDisposable) {
        mApiService = RetrofitClient.getApiService();
        this.mDisposable = mDisposable;
    }

    public void resetPassword(String token, String idUser, String email, String newPassword,
                              String newPasswordEncrypted, ResponseListener<Boolean> listener) {

        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.resetPassword(token, idUser, email, newPassword, newPasswordEncrypted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
