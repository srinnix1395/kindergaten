package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.LikeDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

/**
 * Created by anhtu on 4/4/2017.
 */

public class LikePresenter extends BasePresenter {
    private BulletinBoardHelper mHelper;
    private LikeDelegate mLikeDelegate;
    private String idPost;
    private boolean isLoadFirst = true;

    public LikePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mLikeDelegate = (LikeDelegate) mDelegate;

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
        getListLike(AppConstant.NOW);
    }

    public void getListLike(String prevId) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mLikeDelegate.onLoadFail(R.string.cant_connect, isLoadFirst);
            return;
        }

        mHelper.getListNumberLike(idPost, prevId)
                .subscribe(response -> {
                    if (response == null) {
                        ErrorUtil.handleException(new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    mLikeDelegate.onLoadSuccess(response.getData(), isLoadFirst);
                    if (isLoadFirst) {
                        isLoadFirst = false;
                    }
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mLikeDelegate.onLoadFail(R.string.error_common, isLoadFirst);
                });
    }
}
