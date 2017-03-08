package com.srinnix.kindergarten.login.activity;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.login.fragment.LoginFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by Administrator on 3/1/2017.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        LoginFragment fragment = new LoginFragment();

        ViewManager.getInstance().addFragment(fragment);
    }
}
