package com.srinnix.kindergarten.base.helper;

import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 5/4/2017.
 */

public class BaseHelper {
    protected ApiService mApiService;
    protected CompositeDisposable mDisposable;

    public BaseHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
        mApiService = RetrofitClient.getApiService();
    }
}
