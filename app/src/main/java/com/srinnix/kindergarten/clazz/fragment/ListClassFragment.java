package com.srinnix.kindergarten.clazz.fragment;

import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ClassAdapter;
import com.srinnix.kindergarten.clazz.presenter.ClassListPresenter;

import butterknife.BindView;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ListClassFragment extends BaseFragment {
    @BindView(R.id.recycler_view_class_list)
    RecyclerView rvList;

    private ClassAdapter mAdapter;
    private ClassListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_list;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
