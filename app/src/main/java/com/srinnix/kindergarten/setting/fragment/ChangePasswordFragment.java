package com.srinnix.kindergarten.setting.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.setting.delegate.ChangePasswordDelegate;
import com.srinnix.kindergarten.setting.presenter.ChangePasswordPresenter;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by anhtu on 5/12/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordDelegate{
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

    private ChangePasswordPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected void initChildView() {
        toolbar.setTitle(R.string.change_password);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        imvSuccess.setEnabled(false);
        imvSuccess.setAlpha(0.3f);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ChangePasswordPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.imageview_success)
    public void onClickSave() {
        UiUtils.hideKeyboard(getActivity());
        mPresenter.onClickSave(etOldPassword.getText().toString().trim(),
                etNewPassword1.getText().toString().trim(),
                etNewPassword2.getText().toString().trim(),
                imvSuccess, pbLoading);
    }

    @OnFocusChange(R.id.edittext_new_password1)
    public void onFocusChanged(View v, boolean hasFocus) {
        if (!hasFocus) {
            String newPassword = etNewPassword1.getText().toString().trim();
            etNewPassword1.setText(newPassword);
            if (newPassword.length() < 6) {
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

    @Override
    public void onStartCallAPI() {
        UiUtils.hideView(imvSuccess);
        UiUtils.showProgressBar(pbLoading);
    }

    @Override
    public void onFinally(boolean result) {
        UiUtils.hideProgressBar(pbLoading);
        UiUtils.showView(imvSuccess);

        if (result) {
            AlertUtils.showToastSuccess(mContext, R.drawable.ic_account_check, R.string.change_password_successfully);
            new Handler().postDelayed(this::onBackPressed, 2000);
        }
    }
}
