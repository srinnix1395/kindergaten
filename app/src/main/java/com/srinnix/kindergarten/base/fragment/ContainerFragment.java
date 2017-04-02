package com.srinnix.kindergarten.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.base.presenter.ContainerPresenter;
import com.srinnix.kindergarten.camera.fragment.CameraFragment;
import com.srinnix.kindergarten.children.fragment.ChildrenFragment;
import com.srinnix.kindergarten.clazz.fragment.ClassListFragment;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.List;

/**
 * Created by anhtu on 3/17/2017.
 */

public class ContainerFragment extends BaseFragment {

    private ContainerPresenter mPresenter;

    private int typeFragment;
    private int accountType;

    public static ContainerFragment newInstance(int type) {
        ContainerFragment fragment = new ContainerFragment();
        fragment.setTypeFragment(type);
        return fragment;
    }

    public void setTypeFragment(int typeFragment) {
        this.typeFragment = typeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blank;
    }

    @Override
    protected void initChildView() {
        accountType = SharedPreUtils.getInstance(mContext).getAccountType();

        switch (typeFragment) {
            case AppConstant.TYPE_CLASS_FRAGMENT: {
                initClassFragment();
                break;
            }
            case AppConstant.TYPE_CAMERA_FRAGMENT: {
                initCameraFragment();
                break;
            }
            case AppConstant.TYPE_CHILDREN_FRAGMENT:{
                initChildrenFragment();
                break;
            }
        }
    }

    private void initChildrenFragment() {
        mPresenter.replaceFragment(getChildFragmentManager(), new ChildrenFragment());
    }

    private void initCameraFragment() {
        mPresenter.replaceFragment(getChildFragmentManager(), new CameraFragment());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onHiddenChanged(hidden);
        }
    }

    public void initClassFragment() {
        if (accountType == AppConstant.ACCOUNT_TEACHERS) {
            String classId = SharedPreUtils.getInstance(mContext).getClassId();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_CLASS, classId);
            mPresenter.replaceFragment(getChildFragmentManager(), DetailClassFragment.newInstance(bundle));
        } else {
            mPresenter.replaceFragment(getChildFragmentManager(), new ClassListFragment());
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ContainerPresenter(this);
        return mPresenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        removeFragment();
    }

    public void removeFragment() {
        mPresenter.removeUnUsedFragment(getParentFragment().getChildFragmentManager(),
                this,isVisible(), typeFragment);
    }
}
