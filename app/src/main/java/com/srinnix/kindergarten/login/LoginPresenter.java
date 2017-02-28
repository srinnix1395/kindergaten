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
import com.srinnix.kindergarten.database.RealmDatabase;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

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
                      Button btnLogin, CompositeDisposable disposable, Realm realm) {
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
                .subscribe(dataLoginApiResponse -> handleResponse(dataLoginApiResponse, realm),
                        this::handleException));

    }

    private void handleException(Throwable throwable) {
        AlertUtils.showToast(mContext, R.string.commonError);
        DebugLog.i(throwable.getMessage());
    }

    private void handleResponse(ApiResponse<DataLogin> loginResponse, Realm realm) {
        if (loginResponse == null) {
            DebugLog.i(ErrorConstant.RESPONSE_NULL);
            return;
        }

        if (loginResponse.result == ApiResponse.RESULT_NG) {
            handleError(loginResponse.error);
            return;
        }

        SharedPreUtils.getInstance(mContext).saveUserData(loginResponse.getData().getUser());
        RealmDatabase.insertContact(realm, loginResponse.getData().getContacts(), loginDelegate);
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
