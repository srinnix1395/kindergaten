package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostHelper {
    private ApiService mApi;
    private CompositeDisposable mDisposable;

    public PostHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public void post(String token, Post post, ResponseListener<Post> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApi.insertPost(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
