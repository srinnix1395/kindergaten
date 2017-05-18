package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.activity.BaseActivity;
import com.srinnix.kindergarten.base.fragment.BaseFragment;

import java.util.List;

/**
 * Created by anhtu on 3/7/2017.
 */

public class ViewManager {

    private AppCompatActivity mActivity;
    private FragmentManager mFragmentManager;

    private static ViewManager mInstance;

    public synchronized static ViewManager getInstance() {
        if (mInstance == null) {
            mInstance = new ViewManager();
        }
        return mInstance;
    }

    public void setActivity(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        setFragmentManager(mActivity.getSupportFragmentManager());
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    private void setFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, null);
    }

    public void addFragment(Fragment fragment, Bundle bundle, int animAppear, int animDisappear) {
        if (null != mFragmentManager) {
            fragment.setArguments(bundle);
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.setCustomAnimations(animAppear, animDisappear, animAppear, animDisappear);
            ft.add(R.id.layout_content, fragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
        }
    }

    public void addFragment(Fragment fragment, Bundle bundle) {
        if (null != mFragmentManager) {
            fragment.setArguments(bundle);
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.layout_content, fragment, nameFragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
        }
    }

    public BaseFragment getCurrentFragment(BaseActivity activity) {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment != null && fragment.isVisible())
                    if (fragment instanceof BaseFragment) {
                        return (BaseFragment) fragment;
                    }
            }
        }
        return null;
    }


}