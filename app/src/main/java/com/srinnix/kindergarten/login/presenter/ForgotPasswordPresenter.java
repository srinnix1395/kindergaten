package com.srinnix.kindergarten.login.presenter;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.login.delegate.ForgotPasswordDelegate;
import com.srinnix.kindergarten.login.helper.ForgotPasswordHelper;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 4/18/2017.
 */

public class ForgotPasswordPresenter extends BasePresenter {

    private ForgotPasswordHelper mHelper;
    private CompositeDisposable mDisposable;
    private ForgotPasswordDelegate mForgotpasswordDelegate;

    public ForgotPasswordPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mForgotpasswordDelegate = (ForgotPasswordDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new ForgotPasswordHelper(mDisposable);
    }

    public void onClickResetPassword() {
        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);
        String token = preUtils.getToken();
        String idUser = preUtils.getUserID();
        String email = preUtils.getEmail();
        String newPassword = StringUtil.randomNewPassword(10);

        mHelper.resetPassword(token, idUser, email, newPassword, StringUtil.md5(newPassword),
                new ResponseListener<Boolean>() {
                    @Override
                    public void onSuccess(ApiResponse<Boolean> response) {
                        if (response == null) {
                            onFail(new NullPointerException());
                            return;
                        }

                        if (response.result == ApiResponse.RESULT_NG) {
                            ErrorUtil.handleErrorApi(mContext, response.error);
                            return;
                        }

                        mForgotpasswordDelegate.onResetSuccess();
                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        ErrorUtil.handleException(mContext, throwable);
                    }

                    @Override
                    public void onFinally() {

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
