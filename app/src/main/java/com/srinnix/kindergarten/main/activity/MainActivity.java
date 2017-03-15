package com.srinnix.kindergarten.main.activity;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainActivity extends BaseActivity{

    @Override
    protected void initChildView() {
        super.initChildView();

        ViewManager.getInstance().addFragment(new MainFragment());
    }
}
