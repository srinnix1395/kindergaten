package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
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

    public void getComment(String token, String idPost, long timeLastComment, CommentListener commentListener) {
        if (commentListener == null) {
            return;
        }

        mDisposable.add(
                mApi.getComment(token, idPost, timeLastComment)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(commentListener::onLoadSuccess, commentListener::onLoadFail)
        );
    }

    public void sendComment(String token, String idPost, String idUser, String name, String image,
                            int accountType, String comment, InsertCommentListener listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApi.insertComment(token, idPost, idUser, name, image, accountType, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onInsertSuccess, listener::onLoadFail));
    }

    public interface CommentListener {
        void onLoadSuccess(ApiResponse<ArrayList<Comment>> response);

        void onLoadFail(Throwable throwable);
    }

    public interface InsertCommentListener {
        void onInsertSuccess(ApiResponse<Comment> response);

        void onLoadFail(Throwable throwable);
    }
}
