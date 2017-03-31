package com.srinnix.kindergarten.base.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.constant.AppConstant;

/**
 * Created by anhtu on 3/23/2017.
 */

public class ContainerPresenter extends BasePresenter {

    public ContainerPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void replaceFragment(FragmentManager manager, Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_content, fragment);
        transaction.commit();
    }

    public void removeUnUsedFragment(FragmentManager fragmentManager, Fragment fragment, boolean isVisible, int typeFragment) {
        if (!isVisible) {
            if (typeFragment == AppConstant.TYPE_CLASS_FRAGMENT) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
}
