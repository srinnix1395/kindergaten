package com.srinnix.kindergarten.base.activity;

import com.srinnix.kindergarten.children.fragment.ChartFragment;
import com.srinnix.kindergarten.clazz.fragment.StudyTimetableFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by anhtu on 4/24/2017.
 */

public class HorizontalActivity extends BaseActivity {

    @Override
    protected void initChildView() {
        super.initChildView();
        switch (getIntent().getIntExtra(AppConstant.KEY_FRAGMENT, 0)) {
            case AppConstant.FRAGMENT_HEALTH_INDEX:{
                ViewManager.getInstance().addFragment(new ChartFragment(), getIntent().getExtras());
                break;
            }
            case AppConstant.FRAGMENT_STUDY_TIMETABLE:{
                ViewManager.getInstance().addFragment(new StudyTimetableFragment(), getIntent().getExtras());
                break;
            }
        }
    }
}
