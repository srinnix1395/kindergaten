package com.srinnix.kindergarten.login.fragment;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.login.delegate.ForgotPasswordDelegate;
import com.srinnix.kindergarten.login.presenter.ForgotPasswordPresenter;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 3/7/2017.
 */

public class ForgotPasswordFragment extends BaseFragment implements ForgotPasswordDelegate{
    @BindView(R.id.edittext_email)
    EditText etEmail;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_close)
    TextView tvClose;

    private ForgotPasswordPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    protected void initChildView() {
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
        mPresenter = new ForgotPasswordPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.textview_close)
    void onClickClose(){
        onBackPressed();
    }

    @OnClick(R.id.button_reset_password)
    void onClickResetPassword(){
        mPresenter.onClickResetPassword();
    }

    @Override
    public void onResetSuccess() {
        AlertUtils.showToast(mContext, R.string.password_reset);
        onBackPressed();
    }
}
