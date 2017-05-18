package com.srinnix.kindergarten.bulletinboard.activity;

import android.content.Intent;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.bulletinboard.fragment.DetailPostFragment;
import com.srinnix.kindergarten.main.activity.MainActivity;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        ViewManager.getInstance().addFragment(new DetailPostFragment(), getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_right_to_left, R.anim.translate_left_to_right);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
