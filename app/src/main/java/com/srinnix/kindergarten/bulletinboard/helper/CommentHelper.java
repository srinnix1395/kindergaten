package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentHelper {
    private ApiService mApi;
    private CompositeDisposable mDisposable;

    public CommentHelper(CompositeDisposable mDisposable) {
        mApi = RetrofitClient.getApiService();
        this.mDisposable = mDisposable;
    }

    public void getComment(String idPost, long timeLastComment, ResponseListener<ArrayList<Comment>> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(
                mApi.getComment(idPost, timeLastComment)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(listener::onFinally)
                        .subscribe(listener::onSuccess, listener::onFail)
        );
    }

    public void sendComment(String token, String idPost, String idUser, String name, String image,
                            int accountType, String comment, ResponseListener<Comment> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApi.insertComment(token, idPost, idUser, name, image, accountType, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
