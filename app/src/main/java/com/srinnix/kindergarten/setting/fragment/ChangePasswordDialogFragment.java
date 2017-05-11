package com.srinnix.kindergarten.setting.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 5/12/2017.
 */

public class ChangePasswordDialogFragment extends BottomSheetDialogFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_success)
    ImageView imvSuccess;

    @BindView(R.id.edittext_old_password)
    EditText etOldPassword;

    @BindView(R.id.edittext_new_password1)
    EditText etNewPassword1;

    @BindView(R.id.edittext_new_password2)
    EditText etNewPassword2;

    private Disposable mDisposable;
    private ApiService mApiService;
    private boolean result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.change_password);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPress());

        imvSuccess.setEnabled(false);
        imvSuccess.setAlpha(0.3f);
    }

    @OnClick(R.id.imageview_success)
    public void onClickSave() {
        if (etNewPassword1.getText().toString().trim().length() < 6) {
            AlertUtils.showToast(getContext(), R.string.password_at_least_6_chars);
            return;
        }

        if (!etNewPassword1.getText().toString().equals(etNewPassword2.getText().toString())) {
            AlertUtils.showToast(getContext(), R.string.password_not_match);
            return;
        }

        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            AlertUtils.showToast(getContext(), R.string.noInternetConnection);
            return;
        }

        UiUtils.hideView(imvSuccess);
        UiUtils.showProgressBar(pbLoading);

        if (mApiService == null) {
            mApiService = RetrofitClient.getApiService();
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(getContext());
        String token = preUtils.getToken();
        String idUser = preUtils.getUserID();

        mDisposable = mApiService.changePassword(token, idUser, etOldPassword.getText().toString().trim(),
                etNewPassword1.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    UiUtils.hideProgressBar(pbLoading);
                    UiUtils.showView(imvSuccess);

                    if (result) {
                        AlertUtils.showToastSuccess(getContext(), R.drawable.ic_account_check, R.string.change_password_successfully);
                        new Handler().postDelayed(this::dismiss, 2000);
                    }
                })
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(getContext(), response.error);
                        return;
                    }

                    result = response.getData();
                }, throwable -> {
                    ErrorUtil.handleException(getContext(), throwable);
                });
    }

    @OnFocusChange(R.id.edittext_new_password1)
    public void onFocusChanged(View v, boolean hasFocus) {
        if (!hasFocus) {
            String newPassword = etNewPassword1.getText().toString().trim();
            if (newPassword.length() < 6) {
                etNewPassword1.setText(newPassword);
                AlertUtils.showToast(getContext(), R.string.password_at_least_6_chars);
            }
        }
    }

    @OnTextChanged({R.id.edittext_old_password, R.id.edittext_new_password2, R.id.edittext_new_password1})
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkValid();
    }

    private void checkValid() {
        if (etOldPassword.getText().toString().isEmpty() ||
                etNewPassword1.getText().toString().isEmpty() ||
                etNewPassword2.getText().toString().isEmpty()) {
            if (imvSuccess.isEnabled()) {
                imvSuccess.setEnabled(false);
                imvSuccess.setAlpha(0.3f);
            }
            return;
        }

        if (!imvSuccess.isEnabled()) {
            imvSuccess.setEnabled(true);
            imvSuccess.setAlpha(1f);
        }
    }

    private void onBackPress() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
