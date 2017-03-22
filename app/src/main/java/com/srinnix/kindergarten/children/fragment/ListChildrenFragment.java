package com.srinnix.kindergarten.children.fragment;

import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;

import butterknife.BindView;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ListChildrenFragment extends BaseFragment {
    @BindView(R.id.recycler_view_children_list)
    RecyclerView rvChildren;

    private ChildrenAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_children_list;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
