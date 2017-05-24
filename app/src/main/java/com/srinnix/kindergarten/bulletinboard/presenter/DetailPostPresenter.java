package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.bulletinboard.delegate.DetailPostDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.DetailImageFragment;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageLikePost;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostPresenter extends BulletinBoardPresenter {

    private String idPost;
    private Post post;
    private int sourceScreen;

    private DetailPostDelegate mDetailPostDelegate;

    public DetailPostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);

        mHelper = new BulletinBoardHelper(mDisposable);
    }

    @Override
    protected void initDelegate(BaseDelegate delegate) {
        mDetailPostDelegate = (DetailPostDelegate) mDelegate;
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        sourceScreen = bundle.getInt(AppConstant.KEY_FRAGMENT);
        if (sourceScreen == AppConstant.NOTIFICATION) {
            idPost = bundle.getString(AppConstant.KEY_ID);
        } else {
            post = bundle.getParcelable(AppConstant.KEY_POST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sourceScreen == AppConstant.NOTIFICATION) {
            getDetailPost();
        } else {
            mDetailPostDelegate.onSuccess(post);
        }
    }

    public void getDetailPost() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mDetailPostDelegate.onFail(R.string.cant_connect);
            return;
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);

        String token = preUtils.getToken();
        String idUser = preUtils.getUserID();

        mDisposable.add(mHelper.getDetailPost(token, idPost, idUser)
                .subscribe(response -> {
                    if (response == null) {
                        mDetailPostDelegate.onFail(R.string.error_common);
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    post = response.getData().getPost();
                    post.setUserLike(response.getData().isUserLike());
                    mDetailPostDelegate.onSuccess(post);
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mDetailPostDelegate.onFail(R.string.error_common);
                }));
    }

    public void onClickImage(ArrayList<Image> listImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEDIA, listImage);

        ViewManager.getInstance().addFragment(new DetailImageFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickNumberLike(FragmentManager fragmentManager) {
        super.onClickNumberLike(fragmentManager, post);
    }

    public void onClickComment() {
        super.onClickComment(post);
    }

    public void onClickShare() {
        super.onClickShare(post);
    }

    public void onClickLike() {
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
            likePost(token, idUser, idPost, name, image, accountType);
        } else {
            unlikePost(token, idUser, idPost);
        }
    }

    private void unlikePost(String token, String idUser, String idPost) {
        mDisposable.add(mHelper.unlikePost(token, idUser, idPost)
                .subscribe(this::handleLike, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                }));
    }

    private void likePost(String token, String idUser, String idPost, String name, String image, int accountType) {
        mDisposable.add(mHelper.likePost(token, idUser, idPost, name, image, accountType)
                .subscribe(this::handleLike, throwable -> {
                    ErrorUtil.handleException(mContext, new NullPointerException());
                }));
    }

    private void handleLike(ApiResponse<LikeResponse> response) {
        if (response == null) {
            ErrorUtil.handleException(mContext, new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_NG) {
            ErrorUtil.handleErrorApi(mContext, response.error);
            return;
        }

        post.setUserLike(response.getData().isLike());
        if (response.getData().isLike()) {
            post.setNumberOfLikes(post.getNumberOfLikes() + 1);
        } else {
            post.setNumberOfLikes(post.getNumberOfLikes() - 1);
        }
        mDetailPostDelegate.likeSuccess(post, response.getData());
        if (EventBus.getDefault().hasSubscriberForEvent(MessageLikePost.class)) {
            EventBus.getDefault().post(new MessageLikePost(response.getData(), post.getNumberOfLikes()));
        }
    }

    public void updateNumberComment(int numberComment) {
        post.setNumberOfComments(numberComment);

    }

    public int getSrcScreen() {
        return sourceScreen;
    }


}
