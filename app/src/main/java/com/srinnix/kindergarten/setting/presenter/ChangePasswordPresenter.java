package com.srinnix.kindergarten.setting.presenter;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.setting.delegate.ChangePasswordDelegate;
import com.srinnix.kindergarten.setting.helper.SettingHelper;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;

/**
 * Created by anhtu on 5/12/2017.
 */

public class ChangePasswordPresenter extends BasePresenter {
    private boolean result;
    private SettingHelper mHelper;
    private ChangePasswordDelegate mChangePasswordDelegate;

    public ChangePasswordPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mChangePasswordDelegate = (ChangePasswordDelegate) mDelegate;

        mHelper = new SettingHelper(mDisposable);
    }

    public void onClickSave(String oldPassword, String newPassword1, String newPassword2, ImageView imvSuccess, ProgressBar pbLoading) {
        if (newPassword1.length() < 6) {
            AlertUtils.showToast(mContext, R.string.password_at_least_6_chars);
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            AlertUtils.showToast(mContext, R.string.password_not_match);
            return;
        }

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);
        String token = preUtils.getToken();
        String idUser = preUtils.getUserID();

        mDisposable.add(mHelper.changePassword(token, idUser, StringUtil.md5(oldPassword), StringUtil.md5(newPassword1))
                .doOnSubscribe(disposable -> mChangePasswordDelegate.onStartCallAPI())
                .doFinally(() -> mChangePasswordDelegate.onFinally(result))
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    result = response.getData();
                }, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                }));
    }
}
