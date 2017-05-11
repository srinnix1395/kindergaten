package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.DetailPostDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostPresenter extends BasePresenter {

    private String idPost;
    private BulletinBoardHelper mHelper;
    private DetailPostDelegate mDetailPostDelegate;

    public DetailPostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDetailPostDelegate = (DetailPostDelegate) mDelegate;

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
        getDetailPost();
    }

    public void getDetailPost() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mDetailPostDelegate.onFail(R.string.cant_connect);
            return;
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);

        String token = preUtils.getToken();
        String idUser = preUtils.getUserID();
        mHelper.getDetailPost(token, idPost, idUser, new ResponseListener<Post>() {
            @Override
            public void onSuccess(ApiResponse<Post> response) {
                if (response == null) {
                    mDetailPostDelegate.onFail(R.string.error_common);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mDetailPostDelegate.onSuccess(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(throwable);

                mDetailPostDelegate.onFail(R.string.error_common);
            }

            @Override
            public void onFinally() {

            }
        });
    }
}
