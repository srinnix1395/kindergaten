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



    public void replaceFragment(FragmentManager manager, Fragment fragment, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_content, fragment, tag);
        transaction.commit();
    }

    public void removeFragment(FragmentManager fragmentManager, boolean isVisible, int typeFragment) {
        if (!isVisible) {
            Fragment fragment = fragmentManager.findFragmentByTag(String.valueOf(typeFragment));
            if (fragment == null) {
                return;
            }

            if (typeFragment == AppConstant.TYPE_CLASS_FRAGMENT) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }
        }
    }
}
