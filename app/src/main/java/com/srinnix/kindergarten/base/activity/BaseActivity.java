package com.srinnix.kindergarten.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 3/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        overrideTransition();
        ViewManager.getInstance().setActivity(this);
        initChildView();
    }

    protected void overrideTransition() {

    }

    protected void initChildView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewManager.getInstance().setActivity(this);
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = ViewManager.getInstance().getCurrentFragment(this);
        if (fragment != null) {
            fragment.onBackPressed();
        }
    }
}
