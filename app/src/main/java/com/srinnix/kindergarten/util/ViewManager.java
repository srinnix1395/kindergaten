package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;

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

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        setFragmentManager(mActivity.getSupportFragmentManager());
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    public void addFragment(BaseFragment fragment) {
        addFragment(fragment, null, true);
    }

    public void addFragment(BaseFragment fragment, Bundle bundle) {
        addFragment(fragment, bundle, true);
    }

    public void addFragment(BaseFragment fragment, Bundle bundle, boolean addStack) {
        if (null != mFragmentManager) {
            fragment.setArguments(bundle);
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.layout_content, fragment);
            if (addStack) {
                ft.addToBackStack(nameFragment);
            }
            ft.commit();
        }
    }

    public void backFragment() {
        if (null != mFragmentManager) {
            if (mFragmentManager.getFragments().size() > 0) {
                mFragmentManager.popBackStack();
            } else {
                mActivity.finish();
            }
        }
    }

}