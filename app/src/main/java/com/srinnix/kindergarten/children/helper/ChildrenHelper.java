package com.srinnix.kindergarten.children.helper;

import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/8/2017.
 */

public class ChildrenHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApi;

    public ChildrenHelper(CompositeDisposable compositeDisposable) {
        mApi = RetrofitClient.getApiService();
        mDisposable = compositeDisposable;
    }

    public void getInfoChildren(String token, String id, ChildrenListener listener) {
        if (listener == null) {
            return;
        }

        mApi.getInfoChildren(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onFail);
    }

    public void getListChildrenTeacher(String token, String idClass, ListChildrenListener listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(
                mApi.getListChildren(token, idClass)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listener::onSuccess, listener::onFail));

    }

    public interface ListChildrenListener {
        void onSuccess(ApiResponse<ArrayList<Child>> response);

        void onFail(Throwable throwable);
    }

    public interface ChildrenListener {
        void onSuccess(ApiResponse<Child> response);

        void onFail(Throwable throwable);
    }
}
