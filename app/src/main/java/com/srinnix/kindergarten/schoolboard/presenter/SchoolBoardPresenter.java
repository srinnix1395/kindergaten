package com.srinnix.kindergarten.schoolboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.Error;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardPresenter extends BasePresenter {

    private ApiService apiService;
    private String token;
    private SchoolBoardDelegate mDelegate;

    public SchoolBoardPresenter(BaseDelegate delegate) {
        super(delegate);
        mDelegate = (SchoolBoardDelegate) delegate;
        token = SharedPreUtils.getInstance(mContext).getToken();
        apiService = RetrofitClient.getApiService();
    }

    public void onLoadMore(CompositeDisposable mDisposable, ArrayList<Object> arrayList, PostAdapter postAdapter) {
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

        mDisposable.clear();
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            String idUser = SharedPreUtils.getInstance(mContext).getUserID();
            mDisposable.add(
                    getPostSignIn(arrayList, postAdapter, idUser, timePrevPost)
            );
        } else {
            mDisposable.add(
                    getPostUnsignIn(arrayList, postAdapter, timePrevPost)
            );
        }
    }

    private Disposable getPostUnsignIn(ArrayList<Object> arrayList, PostAdapter postAdapter, long timePrevPost) {
        return apiService.getListPostGuest(timePrevPost)
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
                        handleSuccessResponse((ArrayList<Post>) o);
                    }
                }, throwable -> handleException(throwable, arrayList, postAdapter));
    }

    private Disposable getPostSignIn(ArrayList<Object> arrayList, PostAdapter postAdapter, String idUser, long timePrevPost) {
        return apiService.getListPostMember(token, idUser, timePrevPost)
                .flatMap(response -> {
                    if (response == null) {
                        return Observable.error(new NullPointerException());
                    }

                    if (response.result == ApiResponse.RESULT_OK) {
                        ArrayList<String> listLikes = response.getData().getListLikes();
                        ArrayList<Post> listPost = response.getData().getListPost();
                        for (Post post : listPost) {
                            for (String id : listLikes) {
                                if (post.getId().equals(id)) {
                                    post.setUserLike(true);
                                    break;
                                }
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
                                handleSuccessResponse((ArrayList<Post>) o);
                            }
                        }
                        , throwable -> handleException(throwable, arrayList, postAdapter));
    }

    private void handleSuccessResponse(ArrayList<Post> arrayList) {
        if (mDelegate != null) {
            mDelegate.updateSchoolBoard(arrayList);
        }
    }

    private void handleError(Error error) {
        //// TODO: 2/28/2017 handle error
    }

    private void handleException(Throwable throwable, ArrayList<Object> arrayList, PostAdapter postAdapter) {
        DebugLog.i(throwable.getMessage());

        int size = arrayList.size();
        ((LoadingItem) arrayList.get(size - 1)).setLoadingState(LoadingItem.STATE_ERROR);
        postAdapter.notifyItemChanged(size - 1);

        AlertUtils.showToast(mContext, R.string.errorPost);
    }


}
