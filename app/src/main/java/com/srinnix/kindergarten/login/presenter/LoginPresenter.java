package com.srinnix.kindergarten.login.presenter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.login.activity.LoginActivity;
import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.login.helper.LoginHelper;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import io.realm.Realm;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginPresenter extends BasePresenter {

    private LoginDelegate mLoginDelegate;
    private LoginHelper mLoginHelper;
    private Realm mRealm;

    public LoginPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mLoginDelegate = (LoginDelegate) mDelegate;
        mRealm = KinderApplication.getInstance().getRealm();
        mLoginHelper = new LoginHelper();
    }

    public void login(FragmentActivity activity, String email, String password, ProgressBar pbLoading,
                      Button btnLogin) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
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

        mLoginHelper.login(email, password, new LoginHelper.LoginListener() {
            @Override
            public void onResponseSuccess(ApiResponse<DataLogin> response) {
                if (response == null) {
                    DebugLog.e(ErrorConstant.RESPONSE_NULL);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(response.error);
                    return;
                }

                SharedPreUtils.getInstance(mContext).saveUserData(response.getData().getUser());
                mLoginHelper.insertContact(mRealm, response.getData().getContacts(), mLoginDelegate);
            }

            @Override
            public void onResponseFail(Throwable throwable) {
                AlertUtils.showToast(mContext, R.string.commonError);
                DebugLog.e(throwable.getMessage());
            }
        });
    }

    public void handleForgetPassword() {
        ((LoginActivity) mContext).addOrRemoveFragment(true);
    }

    public void handleDestroy(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SharedPreUtils.getInstance(mContext).setLastEmailFragmentLogin(email);
        }
    }
}