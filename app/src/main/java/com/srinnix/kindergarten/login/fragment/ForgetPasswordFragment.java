package com.srinnix.kindergarten.login.fragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;

import butterknife.BindView;

/**
 * Created by Administrator on 3/7/2017.
 */

public class ForgetPasswordFragment extends BaseFragment {
    @BindView(R.id.edittext_email)
    EditText etEmail;

    @BindView(R.id.button_reset_password)
    Button btnResetPassword;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
