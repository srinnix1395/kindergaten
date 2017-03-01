package com.srinnix.kindergarten.schoolboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardPresenter extends BasePresenter {

    private ApiService mApiService;
    private String token;
    private SchoolBoardDelegate mDelegate;
    private CompositeDisposable mDisposable;

    public SchoolBoardPresenter(BaseDelegate delegate) {
        super(delegate);
        mDelegate = (SchoolBoardDelegate) delegate;
        token = SharedPreUtils.getInstance(mContext).getToken();
        mApiService = RetrofitClient.getApiService();
        mDisposable = new CompositeDisposable();
    }

    public void onLoadMore(ArrayList<Object> arrayList, PostAdapter postAdapter) {
        int size = arrayList.size();

        long timePrevPost;
        if (arrayList.size() == 1) {
            timePrevPost = System.currentTimeMillis();
        } else {
            timePrevPost = ((Post) arrayList.get(size - 2)).getCreatedAt();
        }

        if (ServiceUtils.isNetworkAvailable(mContext)) {
            ((LoadingItem) arrayList.get(arrayList.size() - 1)).setLoadingState(LoadingItem.STATE_ERROR);
            postAdapter.notifyItemChanged(arrayList.size() - 1);
            return;
        }

        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            String idUser = SharedPreUtils.getInstance(mContext).getUserID();
            getPostSignIn(idUser, timePrevPost);
        } else {
            getPostUnsignIn(timePrevPost);
        }
    }

    private void getPostUnsignIn(long timePrevPost) {
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
                        handleError(((Error) o));
                    } else {
                        handleResponsePost((ArrayList<Post>) o);
                    }
                }, this::handleExceptionPost));
    }

    private void getPostSignIn(String idUser, long timePrevPost) {
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
                                handleError(((Error) o));
                            } else {
                                handleResponsePost((ArrayList<Post>) o);
                            }
                        }
                        , this::handleExceptionPost));
    }

    private void handleResponsePost(ArrayList<Post> arrayList) {
        if (mDelegate != null) {
            mDelegate.updateSchoolBoard(arrayList);
        }
    }

    private void handleError(Error error) {
        //// TODO: 2/28/2017 handle error
    }

    private void handleExceptionPost(Throwable throwable) {
        DebugLog.i(throwable.getMessage());

        if (mDelegate != null) {
            mDelegate.setErrorItemLoading();
        }
    }


    public void onClickLike(ArrayList<Object> arrPost, PostAdapter postAdapter, String idPost, boolean isLike) {
        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            AlertUtils.showDialogToLogin(mContext, R.string.login_to_like);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        String idUser = SharedPreUtils.getInstance(mContext).getUserID();

        if (isLike) {
            likePost(arrPost, token, idUser, idPost);
        } else {
            unlikePost(arrPost, token, idUser, idPost);
        }

    }

    private void likePost(ArrayList<Object> arrPost, String token, String idUser, String idPost) {
        mDisposable.add(
                mApiService.likePost(token, idUser, idPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> handleResponseLike(arrPost, response)
                                , this::handleExceptionLike));
    }

    private void unlikePost(ArrayList<Object> arrPost, String token, String idUser, String idPost) {
        mDisposable.add(
                mApiService.unlikePost(token, idUser, idPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> handleResponseLike(arrPost, response),
                                this::handleExceptionLike)
        );
    }


    private void handleResponseLike(ArrayList<Object> arrPost, ApiResponse<LikeResponse> response) {
        if (response == null) {
            handleExceptionLike(new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_OK) {
            mDisposable.add(Observable.fromIterable(arrPost)
                    .filter(o -> o instanceof Post && ((Post) o).getId().equals(response.getData().getIdPost()))
                    .map(o -> {
                        int position = arrPost.indexOf(o);
                        ((Post) o).setUserLike(response.getData().isLike());
                        return position;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (mDelegate != null) {
                            mDelegate.handleLikePost(integer);
                        }
                    }));
        } else {
            handleError(response.error);
        }
    }

    private void handleExceptionLike(Throwable throwable) {
        DebugLog.i(throwable.getMessage());

        AlertUtils.showToast(mContext, R.string.commonError);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
