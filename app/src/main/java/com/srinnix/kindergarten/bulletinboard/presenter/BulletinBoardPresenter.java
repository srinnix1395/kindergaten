package com.srinnix.kindergarten.bulletinboard.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.BulletinBoardDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.CommentFragment;
import com.srinnix.kindergarten.bulletinboard.fragment.DetailPostFragment;
import com.srinnix.kindergarten.bulletinboard.fragment.LikeDialogFragment;
import com.srinnix.kindergarten.bulletinboard.fragment.PostFragment;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageNumberComment;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.model.PostResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2/3/2017.
 */

public class BulletinBoardPresenter extends BasePresenter {

    private BulletinBoardDelegate mBoardDelegate;
    private BulletinBoardHelper mHelper;
    private CompositeDisposable mDisposable;
    private boolean isLoadFirst = true;

    public BulletinBoardPresenter(BaseDelegate delegate) {
        super(delegate);
        mBoardDelegate = (BulletinBoardDelegate) delegate;
        mDisposable = new CompositeDisposable();
        mHelper = new BulletinBoardHelper(mDisposable);
    }

    public void onLoadMore(RecyclerView rvListPost, ArrayList<Object> arrayList, PostAdapter postAdapter) {
        int size = arrayList.size();

        long timePrevPost;
        if (arrayList.size() == 1) {
            timePrevPost = System.currentTimeMillis();
        } else {
            timePrevPost = ((Post) arrayList.get(size - 2)).getCreatedAt();
        }

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            ((LoadingItem) arrayList.get(arrayList.size() - 1)).setLoadingState(LoadingItem.STATE_ERROR);

            //To fix warning: Scroll callbacks might be run during Health measure & layout pass where you cannot change the RecyclerView data.
            rvListPost.post(() -> postAdapter.notifyItemChanged(arrayList.size() - 1));
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        if (((LoadingItem) arrayList.get(arrayList.size() - 1)).getLoadingState() == LoadingItem.STATE_ERROR) {
            ((LoadingItem) arrayList.get(arrayList.size() - 1)).setLoadingState(LoadingItem.STATE_IDLE_AND_LOADING);

            //To fix warning: Scroll callbacks might be run during Health measure & layout pass where you cannot change the RecyclerView data.
            rvListPost.post(() -> postAdapter.notifyItemChanged(arrayList.size() - 1));
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
        mHelper.getPostSignIn(mContext, token, idUser, timePrevPost, new BulletinBoardHelper.PostListener() {
            @Override
            public void onSuccess(ArrayList<Post> arrayList) {
                if (mBoardDelegate != null) {
                    mBoardDelegate.updateSchoolBoard(arrayList, isLoadFirst);
                }
                if (isLoadFirst) {
                    isLoadFirst = false;
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                handleExceptionPost(throwable);
            }
        });
    }

    private void getPostUnsignIn(long timePrevPost) {
        mHelper.getPostUnsignIn(mContext, timePrevPost, new BulletinBoardHelper.PostListener() {
            @Override
            public void onSuccess(ArrayList<Post> arrayList) {
                if (mBoardDelegate != null) {
                    mBoardDelegate.updateSchoolBoard(arrayList, isLoadFirst);
                }
                if (isLoadFirst) {
                    isLoadFirst = false;
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

        if (mBoardDelegate != null) {
            mBoardDelegate.setErrorItemLoading();
        }
    }


    public void onClickLike(ArrayList<Object> arrPost, Post post) {
        SharedPreUtils sharedPreUtils = SharedPreUtils.getInstance(mContext);

        if (!sharedPreUtils.isUserSignedIn()) {
            AlertUtils.showToast(mContext, R.string.login_to_like);
            return;
        }

        String idPost = post.getId();
        boolean isLike = !post.isUserLike();

        String token = sharedPreUtils.getToken();
        String idUser = sharedPreUtils.getUserID();

        if (isLike) {
            String name = sharedPreUtils.getAccountName();
            int accountType = sharedPreUtils.getAccountType();
            String image = sharedPreUtils.getImage();
            likePost(arrPost, token, idUser, idPost, name, image, accountType);
        } else {
            unlikePost(arrPost, token, idUser, idPost);
        }

    }

    private void likePost(ArrayList<Object> arrPost, String token, String idUser, String idPost,
                          String name, String image, int accountType) {
        mHelper.likePost(token, idUser, idPost, name, image, accountType, new ResponseListener<LikeResponse>() {
            @Override
            public void onSuccess(ApiResponse<LikeResponse> response) {
                handleResponseLike(arrPost, response);
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, new NullPointerException());
            }

            @Override
            public void onFinally() {

            }
        });
    }

    private void unlikePost(ArrayList<Object> arrPost, String token, String idUser, String idPost) {
        mHelper.unlikePost(token, idUser, idPost, new ResponseListener<LikeResponse>() {
            @Override
            public void onSuccess(ApiResponse<LikeResponse> response) {
                handleResponseLike(arrPost, response);
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, new NullPointerException());
            }

            @Override
            public void onFinally() {

            }
        });
    }


    private void handleResponseLike(ArrayList<Object> arrPost, ApiResponse<LikeResponse> response) {
        if (response == null) {
            ErrorUtil.handleException(mContext, new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_OK) {
            Single.fromCallable(() -> {
                int i = 0;
                for (Object o : arrPost) {
                    if (o instanceof Post && ((Post) o).getId().equals(response.getData().getIdPost())) {
                        ((Post) o).setUserLike(response.getData().isLike());
                        if (response.getData().isLike()) {
                            ((Post) o).setNumberOfLikes(((Post) o).getNumberOfLikes() + 1);
                        } else {
                            ((Post) o).setNumberOfLikes(((Post) o).getNumberOfLikes() - 1);
                        }
                        break;
                    }
                    i++;
                }
                return i;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (mBoardDelegate != null) {
                            mBoardDelegate.handleLikePost(integer, response.getData().isLike(), ((Post) arrPost.get(integer)).getNumberOfLikes());

                        }
                    });
        } else {
            ErrorUtil.handleErrorApi(mContext, response.error);
        }
    }

    public void onClickNumberLike(FragmentManager fragmentManager, Post post) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, post.getId());
        bundle.putInt(AppConstant.KEY_LIKE, post.getNumberOfLikes());

        LikeDialogFragment dialog = new LikeDialogFragment();
        dialog.setArguments(bundle);

        dialog.show(fragmentManager, "dialog");
    }

    public void refresh(SwipeRefreshLayout refreshLayout, ArrayList<Object> arrPost) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);
        String token = null;
        String userId = null;
        boolean userSignedIn = preUtils.isUserSignedIn();
        if (userSignedIn) {
            token = preUtils.getToken();
            userId = preUtils.getUserID();
        }

        long timePrev = arrPost.get(0) instanceof LoadingItem ? System.currentTimeMillis() : ((Post) arrPost.get(0)).getCreatedAt();

        mHelper.getNewPost(userSignedIn, token, userId, timePrev, new ResponseListener<PostResponse>() {
            @Override
            public void onSuccess(ApiResponse<PostResponse> response) {
                if (response == null) {
                    ErrorUtil.handleException(mContext, new NullPointerException());
                    mBoardDelegate.onRefreshResult(false, null);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    mBoardDelegate.onRefreshResult(false, null);
                    return;
                }

                mBoardDelegate.onRefreshResult(true, response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mBoardDelegate.onRefreshResult(false, null);
            }

            @Override
            public void onFinally() {

            }
        });
    }

    public void getPostAfterLogin(SwipeRefreshLayout refreshLayout, ArrayList<Object> mListPost) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        String userId = SharedPreUtils.getInstance(mContext).getUserID();

        int size = mListPost.size();
        long minTime = mListPost.get(size - 1) instanceof LoadingItem ?
                ((Post) mListPost.get(size - 2)).getCreatedAt() :
                ((Post) mListPost.get(size - 1)).getCreatedAt();

        mHelper.getImportantPost(token, userId, minTime, new ResponseListener<PostResponse>() {
            @Override
            public void onSuccess(ApiResponse<PostResponse> response) {
                if (response == null) {
                    ErrorUtil.handleException(mContext, new NullPointerException());
                    mBoardDelegate.onRefreshResult(false, null);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    mBoardDelegate.onRefreshResult(false, null);
                    return;
                }

                mBoardDelegate.onGetImportantPost(true, response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mBoardDelegate.onRefreshResult(false, null);
            }

            @Override
            public void onFinally() {

            }
        });
    }

    public void onClickImages(Post post) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_IMAGE, post.getListImage());

        ViewManager.getInstance().addFragment(new DetailPostFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickComment(Post post) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, post.getId());
        bundle.putInt(AppConstant.KEY_COMMENT, post.getNumberOfComments());

        ViewManager.getInstance().addFragment(new CommentFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickShare(Post post) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void updateNumberComment(MessageNumberComment message, ArrayList<Object> arrPost) {
        for (int i = 0, size = arrPost.size(); i < size; i++) {
            if (arrPost.get(i) instanceof Post && ((Post) arrPost.get(i)).getId().equals(message.idPost)) {
                ((Post) arrPost.get(i)).setNumberOfComments(((Post) arrPost.get(i)).getNumberOfComments() + message.numberComment);
                mBoardDelegate.updateNumberComment(i, ((Post) arrPost.get(i)).getNumberOfComments());
                break;
            }
        }
    }

    public void logout(ArrayList<Object> arrPost) {
        if (arrPost.size() == 1) {
            return;
        }
        for (int i = arrPost.size() - 1; i >= 0; i--) {
            if (arrPost.get(i) instanceof Post && ((Post) arrPost.get(i)).getType() == AppConstant.POST_IMPORTANT) {
                arrPost.remove(i);
                mBoardDelegate.deletePost(i);
            }
        }

        for (Object o : arrPost) {
            if (o instanceof Post && ((Post) o).isUserLike()) {
                ((Post) o).setUserLike(false);
            }
        }
        mBoardDelegate.updateLogout();
    }

    public void onClickAddPost() {
        ViewManager.getInstance().addFragment(new PostFragment(), null,
                R.anim.translate_down_to_up, R.anim.translate_up_to_down);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
