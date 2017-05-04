package com.srinnix.kindergarten.clazz.fragment;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.TimeTableDelegate;
import com.srinnix.kindergarten.clazz.presenter.TimeTablePresenter;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Timetable;

/**
 * Created by anhtu on 4/25/2017.
 */

public class TimeTableFragment extends BaseFragment implements TimeTableDelegate{
    private TimeTablePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timetable;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new TimeTablePresenter(this);
        return mPresenter;
    }

    @Override
    public void onFail(int resError) {

    }

    @Override
    public void onSuccessTimetable(Timetable data) {

    }

    @Override
    public void onSuccessStudyTimetable(StudyTimetable data) {

    }
}
