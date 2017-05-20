package com.srinnix.kindergarten.login.fragment;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.login.delegate.LoginDelegate;
import com.srinnix.kindergarten.login.presenter.LoginPresenter;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginFragment extends BaseFragment implements LoginDelegate {
    @BindView(R.id.edittext_email)
    EditText etEmail;

    @BindView(R.id.edittext_password)
    EditText etPassword;

    @BindView(R.id.button_login)
    Button btnLogin;

    @BindView(R.id.textview_forgetpassword)
    TextView tvForgetPassword;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_close)
    TextView tvClose;

    private LoginPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initChildView() {
        etEmail.setText(SharedPreUtils.getInstance(mContext).getLastEmailFragmentLogin());

        tvForgetPassword.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    tvForgetPassword.setTextColor(
                            ContextCompat.getColor(mContext, R.color.colorWhiteFadeFade)
                    );
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    tvForgetPassword.setTextColor(
                            ContextCompat.getColor(mContext, R.color.colorWhiteFade)
                    );
                    break;
                }
            }
            return false;
        });

        tvClose.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    tvClose.setTextColor(
                            ContextCompat.getColor(mContext, R.color.colorWhiteFadeFade)
                    );
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    tvClose.setTextColor(
                            ContextCompat.getColor(mContext, R.color.colorWhiteFade)
                    );
                    break;
                }
            }
            return false;
        });


        UiUtils.hideProgressBar(pbLoading);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new LoginPresenter(this);
        return mPresenter;
    }


    @OnClick(R.id.textview_forgetpassword)
    void onClickForgetPassword() {
        mPresenter.handleForgetPassword();
    }

    @OnClick(R.id.button_login)
    void onClickLogin() {
        mPresenter.login(getActivity(), etEmail.getText().toString(),
                etPassword.getText().toString(),
                pbLoading,
                btnLogin);
    }

    @OnClick(R.id.textview_close)
    void onClickClose() {
        onBackPressed();
    }

    @Override
    public void loginSuccessfully() {
        AlertUtils.showToastSuccess(mContext, R.drawable.ic_account_check, R.string.login_successfully);
        new Handler().postDelayed(this::onBackPressed, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.handleDestroy(etEmail.getText().toString());
    }
}
