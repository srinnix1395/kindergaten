package com.srinnix.kindergarten.clazz.activity;

import android.content.Intent;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.clazz.fragment.MemberClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 3/20/2017.
 */

public class ClassActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        Intent intent = getIntent();

        int screenId = intent.getIntExtra(AppConstant.SCREEN_ID, 0);
        switch (screenId) {
            case AppConstant.FRAGMENT_DETAIL_CLASS:{
                ViewManager.getInstance().addFragment(new DetailClassFragment(), intent.getExtras());
                break;
            }
            case AppConstant.FRAGMENT_MEMBER_CLASS:{
                ViewManager.getInstance().addFragment(new MemberClassFragment(), intent.getExtras());
                break;
            }
        }
    }
}
