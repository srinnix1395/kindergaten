package com.srinnix.kindergarten.schoolboard.helper;

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

public class SchoolBoardHelper {
    private CompositeDisposable mDisposable;
    private ApiService mApiService;

    public SchoolBoardHelper(CompositeDisposable disposable) {
        mDisposable = disposable;
        mApiService = RetrofitClient.getApiService();
    }

    public void likePost(String token, String idUser, String idPost, LikeListener listener) {
        mDisposable.add(
                mApiService.likePost(token, idUser, idPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                                    if (listener != null) {
                                        listener.onSuccess(response);
                                    }
                                }
                                , throwable -> {
                                    if (listener != null) {
                                        listener.onFail(throwable);
                                    }
                                }));
    }

    public void unlikePost(String token, String idUser, String idPost, LikeListener listener) {
        mDisposable.add(mApiService.unlikePost(token, idUser, idPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (listener != null) {
                                listener.onSuccess(response);
                            }
                        }
                        , throwable -> {
                            if (listener != null) {
                                listener.onFail(throwable);
                            }
                        }));
    }

    public void getPostSignIn(String token, String idUser, long timePrevPost, PostListener listener) {
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
                                ErrorUtil.handleErrorApi((Error) o);
                            } else if (listener != null) {
                                listener.onSuccess(((ArrayList<Post>) o));
                            }
                        }
                        , throwable -> {
                            if (listener != null) {
                                listener.onFail(throwable);
                            }
                        }));
    }

    public void getPostUnsignIn(long timePrevPost, PostListener listener) {
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
                        ErrorUtil.handleErrorApi((Error) o);
                    } else if (listener != null) {
                        listener.onSuccess((ArrayList<Post>) o);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onFail(throwable);
                    }
                }));
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
