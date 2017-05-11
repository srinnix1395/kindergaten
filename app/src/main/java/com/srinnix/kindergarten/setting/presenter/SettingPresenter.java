package com.srinnix.kindergarten.setting.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.setting.delegate.SettingDelegate;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by anhtu on 3/6/2017.
 */

public class SettingPresenter extends BasePresenter{

    private boolean isReceiveNoti;
    private SettingDelegate mSettingDelegate;

    public SettingPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mSettingDelegate = (SettingDelegate) mDelegate;
    }

    @Override
    public void onStart() {
        super.onStart();

        isReceiveNoti = SharedPreUtils.getInstance(mContext).getFlagReceiveNotification();
        mSettingDelegate.updateSwitch(isReceiveNoti);
    }

    public void onCheckSwitch(boolean isChecked) {
        isReceiveNoti = isChecked;

        SharedPreUtils.getInstance(mContext).setFlagReceiveNotification(isChecked);
    }
}
