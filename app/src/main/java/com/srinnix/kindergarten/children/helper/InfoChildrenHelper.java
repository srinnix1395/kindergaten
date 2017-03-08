package com.srinnix.kindergarten.children.helper;

import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/8/2017.
 */

public class InfoChildrenHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApi;

    public InfoChildrenHelper(CompositeDisposable compositeDisposable) {
        mApi = RetrofitClient.getApiService();
        mDisposable = compositeDisposable;
    }

    public void getInfoChildren(String token, String id) {
        mApi.getInfoChildren(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<Child>>() {
                    @Override
                    public void accept(ApiResponse<Child> childApiResponse) throws Exception {

                    }
                });
    }
}
