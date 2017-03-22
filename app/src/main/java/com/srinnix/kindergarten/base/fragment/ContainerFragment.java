package com.srinnix.kindergarten.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.fragment.ClassListFragment;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by anhtu on 3/17/2017.
 */

public class ContainerFragment extends BaseFragment {
    private static final String FRAGMENT_CLASS = "fragment_class";

    private int typeFragment;
    private int accountType;
    private boolean isFirst = true;

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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            switch (typeFragment) {
                case AppConstant.TYPE_CLASS_FRAGMENT: {
                    initClassFragment();
                    break;
                }
            }
        }
    }

    private void initClassFragment() {
        if (getChildFragmentManager().findFragmentByTag(FRAGMENT_CLASS) != null) {
            return;
        }

        if (accountType == AppConstant.ACCOUNT_TEACHERS) {
            String classId = SharedPreUtils.getInstance(mContext).getClassId();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_CLASS, classId);
            replaceFragment(DetailClassFragment.newInstance(bundle));
        } else {
            replaceFragment(new ClassListFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.layout_content, fragment, FRAGMENT_CLASS);
        transaction.commit();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
