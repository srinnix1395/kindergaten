package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostHelper {
    private ApiService mApi;
    private CompositeDisposable mDisposable;

    public DetailPostHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
        mApi = RetrofitClient.getApiService();
    }

    public void getDetailPost(String token, String idPost, String idUser, ResponseListener<Post> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApi.getDetailPost(token, idPost, idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
