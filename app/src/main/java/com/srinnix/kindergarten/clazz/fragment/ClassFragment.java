package com.srinnix.kindergarten.clazz.fragment;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.presenter.ClassPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassFragment extends BaseFragment implements ClassDelegate {
    @BindView(R.id.imageview_chat)
    ImageView imvChat;

    @BindView(R.id.cardview_member_class)
    CardView cardViewMemberClass;

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

    @OnClick(R.id.imageview_chat)
    void onClickChat() {
        mPresenter.onClickChat();
    }

    @OnClick({R.id.rel_teacher_1, R.id.rel_teacher_2, R.id.rel_teacher_3})
    void onClickTeachers(View view) {
        switch (view.getId()) {
            case R.id.rel_teacher_1: {
                mPresenter.onClickTeacher(1);
                break;
            }
            case R.id.rel_teacher_2: {
                mPresenter.onClickTeacher(2);
                break;
            }
            case R.id.rel_teacher_3: {
                mPresenter.onClickTeacher(3);
                break;
            }
        }
    }
}
