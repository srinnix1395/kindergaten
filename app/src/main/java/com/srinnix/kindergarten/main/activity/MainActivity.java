package com.srinnix.kindergarten.main.activity;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.camera.fragment.CameraFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainActivity extends BaseActivity{

    @Override
    protected void initChildView() {
        super.initChildView();

//        MainFragment mainFragment = new MainFragment();
        ViewManager.getInstance().addFragment(new CameraFragment());
    }
}
