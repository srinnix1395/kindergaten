package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.StudyTimetableAdapter;
import com.srinnix.kindergarten.clazz.delegate.StudyTimetableDelegate;
import com.srinnix.kindergarten.clazz.presenter.StudyTimetablePresenter;
import com.srinnix.kindergarten.custom.GridDividerDecoration;
import com.srinnix.kindergarten.model.HeaderTimetable;
import com.srinnix.kindergarten.model.StudySchedule;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/5/2017.
 */

public class StudyTimetableFragment extends BaseFragment implements StudyTimetableDelegate {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_timetable)
    RecyclerView rvTimetable;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.textview_no_content)
    TextView tvNoContent;

    private ArrayList<Object> mListTimeTable;
    private StudyTimetableAdapter mAdapter;
    private StudyTimetablePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_time_table;
    }

    @Override
    protected void initData() {
        super.initData();
        mListTimeTable = new ArrayList<>();
        mAdapter = new StudyTimetableAdapter(mListTimeTable);
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(String.format(mContext.getString(R.string.timetable2), mPresenter.getTime())
                + " " + String.format(mContext.getString(R.string.group), mPresenter.getGroup()));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        if (isFirst) {
            pbLoading.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP);
        } else {
            UiUtils.hideProgressBar(pbLoading);
            if (mPresenter.isHasError()) {
                UiUtils.showView(layoutRetry);
            } else {
                if (mListTimeTable.isEmpty()) {
                    UiUtils.showView(tvNoContent);
                } else {
                    UiUtils.showView(rvTimetable);
                }
            }
        }

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 9, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 5 == 0) {
                    return 1;
                }
                return 2;
            }
        });

        rvTimetable.addItemDecoration(new GridDividerDecoration(mContext, 1));
        rvTimetable.setLayoutManager(layoutManager);
        rvTimetable.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new StudyTimetablePresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        layoutRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);

        mPresenter.getStudyTimetable();
    }

    @Override
    public void onSuccessStudyTimetable(ArrayList<StudyTimetable> data) {
        UiUtils.hideProgressBar(pbLoading);

        if (data == null) {
            DebugLog.e("data is null");
            onFail(R.string.error_common);
            return;
        }

        if (data.isEmpty()) {
            UiUtils.showView(tvNoContent);
            UiUtils.hideView(rvTimetable);
            return;
        }

        mListTimeTable.add(null);

        for (int i = 0, size = data.size(); i < size; i++) {
            mListTimeTable.add(new HeaderTimetable("Tuần " + (i + 1), data.get(i).getTime(), false));
        }

        mListTimeTable.add(new HeaderTimetable(getString(R.string.subject_event), null, true));
        for (int i = 0, size = data.size(); i < size; i++) {
            mListTimeTable.add(new HeaderTimetable(data.get(i).getSubject(), null, true));
        }

        boolean isColor = false;

        StudySchedule studySchedule;
        for (int i = 0; i < 5; i++) {
            mListTimeTable.add(new HeaderTimetable("Thứ " + (i + 2), null, isColor));

            for (int j = 0, size = data.size(); j < size; j++) {
                studySchedule = data.get(j).getSchedule().get(i);
                studySchedule.setColor(isColor);
                mListTimeTable.add(studySchedule);
            }
            isColor = !isColor;
        }

        mAdapter.notifyDataSetChanged();

        rvTimetable.scrollToPosition(0);

        rvTimetable.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        if (layoutRetry.getVisibility() != View.VISIBLE) {
            layoutRetry.setVisibility(View.VISIBLE);
        }
    }
}
