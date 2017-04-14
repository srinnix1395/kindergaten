package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.clazz.presenter.MemberClassPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by anhtu on 3/21/2017.
 */

public class MemberClassFragment extends BaseFragment {
    @BindView(R.id.recycler_view_member)
    RecyclerView rvMember;

    @BindView(R.id.toolbar_list_member)
    Toolbar toolbar;

    private ChildrenAdapter mAdapter;
    private ArrayList<Child> mListChild;
    private MemberClassPresenter mPresenter;
    private String className;

    @Override
    protected void getData() {
        super.getData();
        Bundle args = getArguments();
        className = args.getString(AppConstant.KEY_CLASS);
        mListChild = args.getParcelableArrayList(AppConstant.KEY_MEMBER);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_class;
    }

    @Override
    protected void initChildView() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle(className);

        toolbar.setTitleTextColor(Color.WHITE);

        rvMember.setLayoutManager(new LinearLayoutManager(mContext));

        if (mListChild == null) {
            mListChild = new ArrayList<>();
        }
        mAdapter = new ChildrenAdapter(mListChild, ChildrenAdapter.TYPE_LINEAR, position -> {
            mPresenter.onClickChildren(mListChild.get(position).getId());
        });
        rvMember.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new MemberClassPresenter(this);
        return mPresenter;
    }
}
