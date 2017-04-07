package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.LikeDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 4/4/2017.
 */

public class LikePresenter extends BasePresenter {
    private BulletinBoardHelper mHelper;
    private CompositeDisposable mDisposable;
    private LikeDelegate mLikeDelegate;
    private String idPost;
    private boolean isLoadFirst = true;

    public LikePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDisposable = new CompositeDisposable();
        mHelper = new BulletinBoardHelper(mDisposable);
        mLikeDelegate = (LikeDelegate) mDelegate;
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        idPost = bundle.getString(AppConstant.KEY_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListLike(System.currentTimeMillis());
    }

    public void getListLike(long timePrevLike) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mLikeDelegate.onLoadFail(R.string.noInternetConnection, isLoadFirst);
            return;
        }

        mHelper.getListNumberLike(idPost, timePrevLike, new ResponseListener<ArrayList<LikeModel>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<LikeModel>> response) {
                if (response == null) {
                    onFail(new NullPointerException());
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
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(throwable);
                mLikeDelegate.onLoadFail(R.string.error_common, isLoadFirst);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
