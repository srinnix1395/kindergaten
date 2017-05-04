package com.srinnix.kindergarten.login.presenter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.login.fragment.ForgotPasswordFragment;
import com.srinnix.kindergarten.login.helper.LoginHelper;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;
import com.srinnix.kindergarten.util.ViewManager;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginPresenter extends BasePresenter {

    private LoginDelegate mLoginDelegate;
    private LoginHelper mLoginHelper;
    private CompositeDisposable mDisposable;
    private Realm mRealm;

    public LoginPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mLoginDelegate = (LoginDelegate) mDelegate;
        mRealm = Realm.getDefaultInstance();

        mDisposable = new CompositeDisposable();
        mLoginHelper = new LoginHelper(mDisposable);
    }

    public void login(FragmentActivity activity, String email, String password, ProgressBar pbLoading,
                      Button btnLogin) {

        UiUtils.hideKeyboard(activity);
        UiUtils.showProgressBar(pbLoading);
        btnLogin.setEnabled(false);

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            AlertUtils.showToast(mContext, R.string.emptyEmailOrPassword);
            return;
        }

        mLoginHelper.login(email, StringUtil.md5(password), new ResponseListener<LoginResponse>() {
            @Override
            public void onSuccess(ApiResponse<LoginResponse> response) {
                if (response == null) {
                    DebugLog.e(ErrorConstant.RESPONSE_NULL);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                SharedPreUtils.getInstance(mContext).saveUserData(response.getData().getUser(), response.getData().getChildren());
                mLoginHelper.insertData(mRealm, response.getData().getChildren(), response.getData().getContacts(), mLoginDelegate);
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
            }

            @Override
            public void onFinally() {
                btnLogin.setEnabled(true);
                UiUtils.hideProgressBar(pbLoading);
            }
        });
    }

    public void handleForgetPassword() {
        ViewManager.getInstance().addFragment(new ForgotPasswordFragment());
    }

    public void handleDestroy(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SharedPreUtils.getInstance(mContext).setLastEmailFragmentLogin(email);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mRealm.isClosed()) {
            mRealm.close();
        }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
