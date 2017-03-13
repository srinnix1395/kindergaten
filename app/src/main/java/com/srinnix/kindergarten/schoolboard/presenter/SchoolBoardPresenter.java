package com.srinnix.kindergarten.schoolboard.presenter;

import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.schoolboard.helper.SchoolBoardHelper;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardPresenter extends BasePresenter {

    private SchoolBoardDelegate mDelegate;
    private SchoolBoardHelper mHelper;
    private CompositeDisposable mDisposable;

    public SchoolBoardPresenter(BaseDelegate delegate) {
        super(delegate);
        mDelegate = (SchoolBoardDelegate) delegate;
        mDisposable = new CompositeDisposable();
        mHelper = new SchoolBoardHelper(mDisposable);
    }

    public void onLoadMore(RecyclerView rvListPost, ArrayList<Object> arrayList, PostAdapter postAdapter) {
        int size = arrayList.size();

        long timePrevPost;
        if (arrayList.size() == 1) {
            timePrevPost = System.currentTimeMillis();
        } else {
            timePrevPost = ((Post) arrayList.get(size - 2)).getCreatedAt();
        }

        if (ServiceUtils.isNetworkAvailable(mContext)) {
            ((LoadingItem) arrayList.get(arrayList.size() - 1)).setLoadingState(LoadingItem.STATE_ERROR);

            //To fix warning: Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data.
            rvListPost.post(() -> postAdapter.notifyItemChanged(arrayList.size() - 1));
            return;
        }

        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            String idUser = SharedPreUtils.getInstance(mContext).getUserID();
            String token = SharedPreUtils.getInstance(mContext).getToken();
            getPostSignIn(token, idUser, timePrevPost);
        } else {
            getPostUnsignIn(timePrevPost);
        }
    }

    private void getPostSignIn(String token, String idUser, long timePrevPost) {
        mHelper.getPostSignIn(mContext, token, idUser, timePrevPost, new SchoolBoardHelper.PostListener() {
            @Override
            public void onSuccess(ArrayList<Post> arrayList) {
                if (mDelegate != null) {
                    mDelegate.updateSchoolBoard(arrayList);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                handleExceptionPost(throwable);
            }
        });
    }

    private void getPostUnsignIn(long timePrevPost) {
        mHelper.getPostUnsignIn(mContext, timePrevPost, new SchoolBoardHelper.PostListener() {
            @Override
            public void onSuccess(ArrayList<Post> arrayList) {
                if (mDelegate != null) {
                    mDelegate.updateSchoolBoard(arrayList);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                handleExceptionPost(throwable);
            }
        });
    }

    private void handleExceptionPost(Throwable throwable) {
        DebugLog.e(throwable.getMessage());

        if (mDelegate != null) {
            mDelegate.setErrorItemLoading();
        }
    }


    public void onClickLike(ArrayList<Object> arrPost, String idPost, boolean isLike) {
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
        mHelper.likePost(token, idUser, idPost, new SchoolBoardHelper.LikeListener() {
            @Override
            public void onSuccess(ApiResponse<LikeResponse> response) {
                handleResponseLike(arrPost, response);
            }

            @Override
            public void onFail(Throwable throwable) {
                handleExceptionLike(throwable);
            }
        });
    }

    private void unlikePost(ArrayList<Object> arrPost, String token, String idUser, String idPost) {
        mHelper.unlikePost(token, idUser, idPost, new SchoolBoardHelper.LikeListener() {
            @Override
            public void onSuccess(ApiResponse<LikeResponse> response) {
                handleResponseLike(arrPost, response);
            }

            @Override
            public void onFail(Throwable throwable) {
                handleExceptionLike(throwable);
            }
        });
    }


    private void handleResponseLike(ArrayList<Object> arrPost, ApiResponse<LikeResponse> response) {
        if (response == null) {
            handleExceptionLike(new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_OK) {
            Single.fromCallable(() -> {
                int i = 0;
                for (Object o : arrPost) {
                    if (o instanceof Post && ((Post) o).getId().equals(response.getData().getIdPost())) {
                        ((Post) o).setUserLike(response.getData().isLike());
                    }
                    i++;
                }
                return i;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (mDelegate != null) {
                            mDelegate.handleLikePost(integer);
                        }
                    });
        } else {
            ErrorUtil.handleErrorApi(mContext, response.error);
        }
    }

    private void handleExceptionLike(Throwable throwable) {
        DebugLog.e(throwable.getMessage());

        AlertUtils.showToast(mContext, R.string.commonError);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }

    public void refresh() {

    }
}
