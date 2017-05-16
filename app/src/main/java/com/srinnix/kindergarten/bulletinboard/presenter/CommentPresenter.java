package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;
import android.widget.EditText;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.CommentDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentPresenter extends BasePresenter {
    private CommentDelegate mCommentDelegate;
    private BulletinBoardHelper mHelper;
    private String idPost;
    private boolean isLoadFirst = true;

    public CommentPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mCommentDelegate = (CommentDelegate) mDelegate;

        mHelper = new BulletinBoardHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        idPost = bundle.getString(AppConstant.KEY_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        getComment(AppConstant.NOW);
    }

    public void getComment(String prevId) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mCommentDelegate.onLoadCommentFail(R.string.cant_connect, isLoadFirst);
            return;
        }

        mDisposable.add(mHelper.getComment(idPost, prevId)
                .subscribe(response -> {
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
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mCommentDelegate.onLoadCommentFail(R.string.error_common, isLoadFirst);
                }));
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
        String comment = etComment.getText().toString().trim();

        etComment.setText("");

        long now = System.currentTimeMillis();
        mCommentDelegate.insertComment(new Comment(String.valueOf(now),
                name, image, comment, now, accountType));

        sendComment(token, idUser, name, image, accountType, comment, now);
    }

    private void sendComment(String token, String idUser, String name, String image,
                             int accountType, String comment, long time) {

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mCommentDelegate.updateStateComment(time);
            return;
        }

        mDisposable.add(mHelper.sendComment(token, idPost, idUser, name, image, accountType, comment)
                .subscribe(response -> {
                    if (response == null) {
                        mCommentDelegate.updateStateComment(time);
                        ErrorUtil.handleException(mContext, new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        mCommentDelegate.updateStateComment(time);
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    mCommentDelegate.updateIdComment(time, response.getData());
                }, throwable -> {
                    mCommentDelegate.updateStateComment(time);
                    ErrorUtil.handleException(throwable);
                }));
    }

    public void onResendComment(Comment comment, int position) {
        String token = SharedPreUtils.getInstance(mContext).getToken();

        mCommentDelegate.updateStateComment(position, true);

        sendComment(token, comment.getId(), comment.getName(), comment.getImage(),
                comment.getAccountType(), comment.getComment(), comment.getCreatedAt());
    }

    public void onLongClickComment(Comment comment) {
        // TODO: 4/2/2017 long click
    }

    public String getIdPost() {
        return idPost;
    }
}
