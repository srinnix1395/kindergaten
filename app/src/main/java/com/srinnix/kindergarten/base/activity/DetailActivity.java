package com.srinnix.kindergarten.base.activity;

import android.content.Intent;

import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.clazz.fragment.MemberClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.login.fragment.LoginFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 3/24/2017.
 */

public class DetailActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        Intent intent = getIntent();

        int screenID = intent.getIntExtra(AppConstant.SCREEN_ID, 0);

        BaseFragment fragment = null;
        switch (screenID) {
            case AppConstant.FRAGMENT_DETAIL_CHAT: {
                fragment = new DetailChatFragment();
                break;
            }
            case AppConstant.FRAGMENT_DETAIL_CLASS: {
                fragment = new DetailClassFragment();
                break;
            }
            case AppConstant.FRAGMENT_MEMBER_CLASS: {
                fragment = new MemberClassFragment();
                break;
            }
            case AppConstant.FRAGMENT_LOGIN:{
                fragment = new LoginFragment();
                break;
            }
            default: {
                break;
            }
        }
        if (fragment != null) {
            ViewManager.getInstance().addFragment(fragment, intent.getExtras());
        }
    }
}
