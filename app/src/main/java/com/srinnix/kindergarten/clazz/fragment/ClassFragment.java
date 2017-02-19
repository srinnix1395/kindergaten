package com.srinnix.kindergarten.clazz.fragment;

import android.support.v7.widget.CardView;
import android.widget.RelativeLayout;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.presenter.ClassPresenter;

import butterknife.BindView;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassFragment extends BaseFragment implements ClassDelegate {
    @BindView(R.id.cardview_class)
    CardView cardViewClass;

    @BindView(R.id.rel_teacher_1)
    RelativeLayout relTeacher1;

    @BindView(R.id.rel_teacher_2)
    RelativeLayout relTeacher2;

    @BindView(R.id.rel_teacher_3)
    RelativeLayout relTeacher3;

    private ClassPresenter mPresenter;
    private ImageAdapter adapter;


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
