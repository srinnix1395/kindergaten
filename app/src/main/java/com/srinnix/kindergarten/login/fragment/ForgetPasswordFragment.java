package com.srinnix.kindergarten.login.fragment;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.textview_close)
    TextView tvClose;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forget_password;
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
        return null;
    }

    @OnClick(R.id.textview_close)
    void onClickClose(){
        onBackPressed();
    }
}
