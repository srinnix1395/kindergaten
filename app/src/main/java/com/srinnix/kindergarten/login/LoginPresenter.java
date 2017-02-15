package com.srinnix.kindergarten.login;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.request.model.BaseResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginPresenter extends BasePresenter {

    private LoginDelegate loginDelegate;
    private ApiService mApi;

    public LoginPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        loginDelegate = (LoginDelegate) mDelegate;
    }

    public void login(FragmentActivity activity, String email, String password, ProgressBar pbLoading,
                      Button btnLogin, CompositeDisposable disposable) {
        if (ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInteretConnection);
            return;
        }

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            AlertUtils.showToast(mContext, R.string.emptyEmailOrPassword);
            return;
        }

        UiUtils.hideKeyboard(activity);
        UiUtils.showProgressBar(pbLoading);
        btnLogin.setEnabled(false);

        disposable.add(mApi.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleException));

    }

    private void handleException(Throwable throwable) {
        AlertUtils.showToast(mContext, R.string.commonError);
        DebugLog.e(throwable.getMessage());
    }

    private void handleResponse(LoginResponse loginResponse) {
        if (loginResponse == null) {
            DebugLog.e(ErrorConstant.RESPONSE_NULL);
            return;
        }

        if (loginResponse.result == BaseResponse.RESULT_OK) {
            SharedPreUtils.getInstance(mContext).saveUserData(loginResponse.getUser());
            loginDelegate.loginSuccessfully();
        } else {
            handleError(loginResponse.error);
        }
    }

    private void handleError(Error error) {
        switch (error.code) {
            //// TODO: 2/13/2017 error code
        }
    }

    public void handleForgetPassword() {

    }

    public void handleDestroy(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SharedPreUtils.getInstance(mContext).setLastEmailFragmentLogin(email);
        }
    }
}
