package com.srinnix.kindergarten.main.activity;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.login.fragment.LoginFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainActivity extends BaseActivity{

    @Override
    protected void initChildView() {
        super.initChildView();

        ViewManager.getInstance().addFragment(new LoginFragment());
    }
}
