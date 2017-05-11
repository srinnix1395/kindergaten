package com.srinnix.kindergarten.setting.presenter;

import android.support.v4.app.FragmentManager;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.setting.delegate.AccountDelegate;
import com.srinnix.kindergarten.setting.fragment.ChangePasswordDialogFragment;
import com.srinnix.kindergarten.setting.helper.SettingHelper;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by anhtu on 5/11/2017.
 */

public class AccountPresenter extends BasePresenter {
    private SettingHelper mHelper;
    private AccountDelegate mAccountDelegate;

    public AccountPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mAccountDelegate = (AccountDelegate) mDelegate;

        mHelper = new SettingHelper(mDisposable);
    }

    @Override
    public void onStart() {
        super.onStart();
        getInfo();
    }

    public void getInfo() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mAccountDelegate.onFail(R.string.cant_connect);
            return;
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);

        String token = preUtils.getToken();
        String userID = preUtils.getUserID();
        int accountType = preUtils.getAccountType();

        mHelper.getAccountInfo(token, userID, accountType)
                .subscribe(pair -> {
                    ApiResponse<User> response = pair.first;

                    if (response == null) {
                        ErrorUtil.handleException(new NullPointerException());
                        mAccountDelegate.onFail(R.string.error_common);
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        mAccountDelegate.onFail(R.string.error_common);
                        return;
                    }

                    mAccountDelegate.onSuccess(response.getData(), pair.second);
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mAccountDelegate.onFail(R.string.error_common);
                });
    }

    public void onClickChangePassword(FragmentManager fragmentManager) {
        ChangePasswordDialogFragment dialogFragment = new ChangePasswordDialogFragment();

        dialogFragment.show(fragmentManager, "dialog");
    }
}
