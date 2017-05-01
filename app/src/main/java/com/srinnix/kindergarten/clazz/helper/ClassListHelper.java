package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/20/2017.
 */

public class ClassListHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApi;

    public ClassListHelper(CompositeDisposable disposable) {
        mApi = RetrofitClient.getApiService();
        mDisposable = disposable;
    }

    public void getListClass(ResponseListener<ArrayList<Class>> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApi.getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
