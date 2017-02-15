package com.srinnix.kindergarten.clazz.fragment;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.presenter.ClassPresenter;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassFragment extends BaseFragment implements ClassDelegate {

    private ClassPresenter mPresenter;

    public static ClassFragment newInstance() {
        return new ClassFragment();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ClassPresenter(this);
        return mPresenter;
    }
}
