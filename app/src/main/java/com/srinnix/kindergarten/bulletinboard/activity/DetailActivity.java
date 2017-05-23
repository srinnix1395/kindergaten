package com.srinnix.kindergarten.bulletinboard.activity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.bulletinboard.fragment.DetailPostFragment;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        int keyFragment = getIntent().getIntExtra(AppConstant.KEY_FRAGMENT, AppConstant.FRAGMENT_DETAIL_POST);
        if (keyFragment == AppConstant.FRAGMENT_DETAIL_POST) {
            ViewManager.getInstance().addFragment(new DetailPostFragment(), getIntent().getExtras());
        } else {
            ViewManager.getInstance().addFragment(new DetailChatFragment(), getIntent().getExtras());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }
}
