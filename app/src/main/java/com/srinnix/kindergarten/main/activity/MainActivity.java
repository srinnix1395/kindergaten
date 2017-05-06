package com.srinnix.kindergarten.main.activity;

import android.support.annotation.NonNull;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!(ViewManager.getInstance().getActivity() instanceof MainActivity)) {
            ViewManager.getInstance().setActivity(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseFragment currentFragment = ViewManager.getInstance().getCurrentFragment(this);
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
