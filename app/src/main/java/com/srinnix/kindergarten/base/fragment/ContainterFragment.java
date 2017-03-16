package com.srinnix.kindergarten.base.fragment;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.presenter.BasePresenter;

/**
 * Created by anhtu on 3/17/2017.
 */

public class ContainterFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blank;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
