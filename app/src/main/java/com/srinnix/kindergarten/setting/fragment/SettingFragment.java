package com.srinnix.kindergarten.setting.fragment;

import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.setting.delegate.SettingDelegate;
import com.srinnix.kindergarten.setting.presenter.SettingPresenter;

import butterknife.BindView;

/**
 * Created by anhtu on 3/6/2017.
 */

public class SettingFragment extends BaseFragment implements SettingDelegate{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.switch_noti)
    SwitchCompat mSwitchNoti;

    private SettingPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initChildView() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(mContext.getString(R.string.setting));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        mSwitchNoti.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.onCheckSwitch(isChecked);
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SettingPresenter(this);
        return mPresenter;
    }

    @Override
    public void updateSwitch(boolean flag) {
        mSwitchNoti.setChecked(flag);
    }
}
