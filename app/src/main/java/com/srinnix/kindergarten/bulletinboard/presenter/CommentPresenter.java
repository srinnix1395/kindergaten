package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;
import android.widget.EditText;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.CommentDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.CommentHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentPresenter extends BasePresenter {
    private CommentDelegate mCommentDelegate;
    private CommentHelper mHelper;
    private CompositeDisposable mDisposable;
    private String idPost;
    private boolean isLoadFirst = true;

    public CommentPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mCommentDelegate = (CommentDelegate) mDelegate;

        mDisposable = new CompositeDisposable();
        mHelper = new CommentHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        idPost = bundle.getString(AppConstant.KEY_ID);
    }

    @Override
    public void onStart(boolean isFirst) {
        super.onStart(isFirst);
        if (isFirst) {
            getComment(idPost, System.currentTimeMillis());
        }
    }

    public void getComment(long time) {
        getComment(idPost, time);
    }

    public void getComment(String idPost, long time) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mCommentDelegate.onLoadCommentFail(R.string.noInternetConnection, isLoadFirst);
            return;
        }

        mHelper.getComment(idPost, time, new CommentHelper.CommentListener() {
            @Override
            public void onLoadSuccess(ApiResponse<ArrayList<Comment>> response) {
                if (response == null) {
                    ErrorUtil.handleException(mContext, new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mCommentDelegate.onLoadCommentSuccess(response.getData(), isLoadFirst);
                if (isLoadFirst) {
                    isLoadFirst = false;
                }
            }

            @Override
            public void onLoadFail(Throwable throwable) {
                mCommentDelegate.onLoadCommentFail(R.string.error_common, isLoadFirst);
                if (!isLoadFirst) {
                    ErrorUtil.handleException(mContext, throwable);
                } else {
                    DebugLog.e(throwable.getMessage());
                }
            }
        });
    }

    public void onClickSend(EditText etComment) {
        SharedPreUtils sharedPreUtils = SharedPreUtils.getInstance(mContext);
        if (!sharedPreUtils.isUserSignedIn()) {
            AlertUtils.showToast(mContext, R.string.login_to_comment);
            return;
        }

        String token = sharedPreUtils.getToken();
        String idUser = sharedPreUtils.getUserID();
        String name = sharedPreUtils.getAccountName();
        int accountType = sharedPreUtils.getAccountType();
        String image = sharedPreUtils.getImage();
        String comment = etComment.getText().toString();

        etComment.setText("");

        long now = System.currentTimeMillis();
        mCommentDelegate.insertComment(new Comment(String.valueOf(now),
                name, image, comment, now, accountType));

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mCommentDelegate.updateStateComment(now);
            return;
        }

        mHelper.sendComment(token, idPost, idUser, name, image, accountType, comment, new CommentHelper.InsertCommentListener() {
            @Override
            public void onInsertSuccess(ApiResponse<Comment> response) {
                if (response == null) {
                    mCommentDelegate.updateStateComment(now);
                    ErrorUtil.handleException(mContext, new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    mCommentDelegate.updateStateComment(now);
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mCommentDelegate.updateIdComment(now, response.getData());
            }

            @Override
            public void onLoadFail(Throwable throwable) {
                mCommentDelegate.updateStateComment(now);
                ErrorUtil.handleException(mContext, throwable);
            }
        });
    }

    public void onResendComment(Comment comment) {
        // TODO: 4/2/2017 resend
    }

    public void onLongClickComment(Comment comment) {
        // TODO: 4/2/2017 long click
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }



}
