package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
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

    public void getClassInfo(String classId, boolean isTeacher,
                             ClassInfoListener listener) {
        mDisposable.add(
                mApi.getClassInfo(classId, isTeacher)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (listener != null) {
                                listener.onResponseSuccess(response);
                            }
                        }, throwable -> {
                            if (listener != null) {
                                listener.onResponseFail(throwable);
                            }
                        })
        );
    }

    public interface ClassInfoListener{
        void onResponseSuccess(ApiResponse<ClassResponse> response);

        void onResponseFail(Throwable throwable);
    }
}
