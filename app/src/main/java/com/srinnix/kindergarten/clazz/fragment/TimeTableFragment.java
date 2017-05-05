package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ActionTimetableAdapter;
import com.srinnix.kindergarten.clazz.delegate.TimeTableDelegate;
import com.srinnix.kindergarten.clazz.presenter.TimeTablePresenter;
import com.srinnix.kindergarten.model.ActionTimetable;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/25/2017.
 */

public class TimeTableFragment extends BaseFragment implements TimeTableDelegate {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textview_subject)
    TextView tvSubject;

    @BindView(R.id.textview_title_event)
    TextView tvTitleEvent;

    @BindView(R.id.textview_event)
    TextView tvEvent;

    @BindView(R.id.recyclerview_timetable)
    RecyclerView rvTimetable;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_subject)
    LinearLayout layoutSubject;

    private TimeTablePresenter mPresenter;

    private ArrayList<ActionTimetable> mListAction;
    private ActionTimetableAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timetable;
    }

    @Override
    protected void initData() {
        super.initData();
        mListAction = new ArrayList<>();
        mAdapter = new ActionTimetableAdapter(mListAction);
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(String.format(mContext.getString(R.string.timetable1), mPresenter.getTime()));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        rvTimetable.setLayoutManager(new LinearLayoutManager(mContext));
        rvTimetable.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new TimeTablePresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        mPresenter.getTimetable();
    }

    @Override
    public void onFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        if (layoutRetry.getVisibility() != View.VISIBLE) {
            layoutRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessTimetable(Timetable data) {
        UiUtils.hideProgressBar(pbLoading);

        layoutSubject.setVisibility(View.VISIBLE);
        tvSubject.setText(data.getSubject());

        if (data.getEvent() != null) {
            tvEvent.setText(data.getEvent());
        } else {
            tvTitleEvent.setVisibility(View.GONE);
        }

        rvTimetable.setVisibility(View.VISIBLE);
        if (!data.getActionTimeTable().isEmpty()) {
            mListAction.addAll(data.getActionTimeTable());
            mAdapter.notifyItemRangeInserted(0, data.getActionTimeTable().size());

            rvTimetable.scrollToPosition(0);
        }
    }
}
