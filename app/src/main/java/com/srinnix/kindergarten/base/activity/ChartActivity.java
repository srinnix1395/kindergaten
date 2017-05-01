package com.srinnix.kindergarten.base.activity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.children.fragment.ChartFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ChartActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        overridePendingTransition(R.anim.translate_right_to_left, R.anim.translate_left_to_right);
        ViewManager.getInstance().addFragment(new ChartFragment(), getIntent().getExtras());
    }


}
