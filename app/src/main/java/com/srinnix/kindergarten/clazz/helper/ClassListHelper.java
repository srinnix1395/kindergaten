package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
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

    public void getListClass(ClassResponseListener listener) {
        mDisposable.add(mApi.getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (listener != null) {
                        listener.onLoadSuccess(response);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onLoadError(throwable);
                    }
                }));
    }

    public interface ClassResponseListener {
        void onLoadSuccess(ApiResponse<ArrayList<Class>> response);

        void onLoadError(Throwable throwable);
    }
}
