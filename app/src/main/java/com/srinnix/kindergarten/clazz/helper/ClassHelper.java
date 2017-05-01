package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.ImageResponse;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/16/2017.
 */

public class ClassHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApi;

    public ClassHelper(CompositeDisposable mDisposable) {
        mApi = RetrofitClient.getApiService();
        this.mDisposable = mDisposable;
    }

    public void getClassInfo(String classId, boolean isTeacher, ResponseListener<ClassResponse> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(
                mApi.getClassInfo(classId, isTeacher)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(listener::onFinally)
                        .subscribe(listener::onSuccess, listener::onFail)
        );
    }

    public void getClassImage(String classId, long time, ResponseListener<ImageResponse> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApi.getImageClass(classId, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
