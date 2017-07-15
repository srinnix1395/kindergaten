package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ClassAdapter;
import com.srinnix.kindergarten.clazz.delegate.ClassListDelegate;
import com.srinnix.kindergarten.clazz.presenter.ClassListPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassListFragment extends BaseFragment implements ClassListDelegate {
    @BindView(R.id.recycler_view_class_list)
    RecyclerView mRvListClass;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    private ArrayList<Object> mListClass;
    private ClassAdapter mAdapter;
    private ClassListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_list;
    }

    @Override
    protected void initChildView() {
        mRvListClass.setLayoutManager(new LinearLayoutManager(mContext));

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        if (!isFirst) {
            UiUtils.showView(mRvListClass);
            UiUtils.hideProgressBar(pbLoading);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mListClass = new ArrayList<>();
        mAdapter = new ClassAdapter(mListClass, position -> mPresenter.onClickClass((Class) mListClass.get(position)));
        mRvListClass.setAdapter(mAdapter);
    }

    @Override
    public void setData() {
        super.setData();
        mRvListClass.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ClassListPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        UiUtils.hideView(layoutRetry);
        UiUtils.showProgressBar(pbLoading);

        mPresenter.onClickRetry();
    }

    @Override
    public void onLoadSuccess(ArrayList<Class> arrayList) {
        UiUtils.hideProgressBar(pbLoading);

        layoutRetry.setVisibility(View.INVISIBLE);

        if (mRvListClass.getVisibility() != View.VISIBLE) {
            mRvListClass.setVisibility(View.VISIBLE);
        }

        mListClass.add(AppConstant.GROUP_TINY);
        mListClass.add(AppConstant.GROUP_SMALL);
        mListClass.add(AppConstant.GROUP_MEDIUM);
        mListClass.add(AppConstant.GROUP_BIG);

        int i = 1, j = 2, k = 3;

        for (Class aClass : arrayList) {
            if (aClass.getGroup().equals(AppConstant.GROUP_TINY)) {
                mListClass.add(i, aClass);
                i++;
                j++;
                k++;
            } else if (aClass.getGroup().equals(AppConstant.GROUP_SMALL)) {
                mListClass.add(j, aClass);
                j++;
                k++;
            } else if (aClass.getGroup().equals(AppConstant.GROUP_MEDIUM)) {
                mListClass.add(k, aClass);
                k++;
            } else {
                mListClass.add(aClass);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadError(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        UiUtils.showView(layoutRetry);
    }
}
