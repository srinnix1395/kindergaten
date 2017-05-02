package com.srinnix.kindergarten.setting.helper;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 4/20/2017.
 */

public class SettingHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApi;

    public SettingHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
        mApi = RetrofitClient.getApiService();
    }

    public void getAccountInfo(String token, String idUser, ResponseListener<User> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(
                mApi.getAccountInfo(token, idUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listener::onSuccess, listener::onFail)
        );
    }
}
