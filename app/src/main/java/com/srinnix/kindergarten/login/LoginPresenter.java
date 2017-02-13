package com.srinnix.kindergarten.login;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.request.service.ApiService;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginPresenter extends BasePresenter {

    private ApiService mApi;

    public LoginPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void login(FragmentActivity activity, String email, String password, ProgressBar pbLoading,
                      Button btnLogin, Disposable disposable) {
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

        disposable = mApi.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                }, throwable -> {

                }, () -> {

                });
    }

    public void handleForgetPassword() {

    }

    public void handleDestroy(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SharedPreUtils.getInstance(mContext).setLastEmailFragmentLogin(email);
        }
    }
}
