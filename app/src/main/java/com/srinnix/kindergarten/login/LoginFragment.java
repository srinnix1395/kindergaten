package com.srinnix.kindergarten.login;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoginFragment extends BaseFragment {
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

    private LoginPresenter mPresenter;
    private Disposable mDisposable;

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

//        pbLoading.getIndeterminateDrawable().setColorFilter(
//                ContextCompat.getColor(mContext, R.color.colorWhiteFade),
//                PorterDuff.Mode.SRC_ATOP);
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
                btnLogin,
                mDisposable);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mPresenter.handleDestroy(etEmail.getText().toString());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}
