package com.srinnix.kindergarten.bulletinboard.helper;

import android.content.Context;

import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.ErrorUtil;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 3/3/2017.
 */

public class BulletinBoardHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApiService;

    public BulletinBoardHelper(CompositeDisposable disposable) {
        mDisposable = disposable;
        mApiService = RetrofitClient.getApiService();
    }

    public void likePost(String token, String idUser, String idPost, LikeListener listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(
                mApiService.likePost(token, idUser, idPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listener::onSuccess, listener::onFail));
    }

    public void unlikePost(String token, String idUser, String idPost, LikeListener listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.unlikePost(token, idUser, idPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void getPostSignIn(Context context, String token, String idUser, long timePrevPost, PostListener listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getListPostMember(token, idUser, timePrevPost)
                .flatMap(response -> {
                    if (response == null) {
                        return Observable.error(new NullPointerException());
                    }

                    if (response.result == ApiResponse.RESULT_OK) {
                        ArrayList<String> listLikes = response.getData().getListLikes();
                        ArrayList<Post> listPost = response.getData().getListPost();
                        for (Post post : listPost) {
                            if (listLikes.contains(post.getId())) {
                                post.setUserLike(true);
                            }
                        }
                        return Observable.just(listPost);
                    }

                    return Observable.just(response.error);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                            if (o instanceof Error) {
                                ErrorUtil.handleErrorApi(context, (Error) o);
                            } else {
                                listener.onSuccess(((ArrayList<Post>) o));
                            }
                        }
                        , listener::onFail));
    }

    public void getPostUnsignIn(Context context, long timePrevPost, PostListener listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApiService.getListPostGuest(timePrevPost)
                .flatMap(response -> {
                    if (response == null) {
                        return Observable.error(new NullPointerException());
                    }

                    if (response.result == ApiResponse.RESULT_OK) {
                        return Observable.just(response.getData());
                    }

                    return Observable.just(response.error);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Error) {
                        ErrorUtil.handleErrorApi(context, (Error) o);
                    } else {
                        listener.onSuccess((ArrayList<Post>) o);
                    }
                }, listener::onFail));
    }

    public void getListNumberLike(String token, String id, NumberLikeListener listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApiService.getListNumberLike(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public interface NumberLikeListener {
        void onSuccess(ApiResponse<LikeModel> response);

        void onFail(Throwable throwable);
    }

    public interface LikeListener {
        void onSuccess(ApiResponse<LikeResponse> response);

        void onFail(Throwable throwable);

    }

    public interface PostListener {
        void onSuccess(ArrayList<Post> arrayList);

        void onFail(Throwable throwable);
    }
}
