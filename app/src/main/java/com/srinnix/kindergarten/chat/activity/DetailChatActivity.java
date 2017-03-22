package com.srinnix.kindergarten.chat.activity;

import android.content.Intent;

import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 2/27/2017.
 */

public class DetailChatActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        Intent intent = getIntent();

        DetailChatFragment fragment = new DetailChatFragment();
        ViewManager.getInstance().addFragment(fragment, intent.getExtras());
    }

}
