package com.srinnix.kindergarten.main.presenter;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.service.UpdateFirebaseRegId;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainPresenter extends BasePresenter {
    private int currentPosition = 0;

    public MainPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void changeTabIcon(Toolbar toolbar, TabLayout tabLayout, int position) {
        if (tabLayout == null || tabLayout.getTabAt(position) == null
                || tabLayout.getTabAt(currentPosition) == null) {
            return;
        }

        toolbar.setTitle(AppConstant.TITLE_TAB[position]);

        tabLayout.getTabAt(position).setIcon(AppConstant.ICON_TAB_SELECTED[position]);
        tabLayout.getTabAt(currentPosition).setIcon(AppConstant.ICON_TAB_UNSELECTED[currentPosition]);
        currentPosition = position;
    }

    public void onClickMenuItemChat(DrawerLayout drawerLayout) {
        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void onBackPressed(MainFragment mainFragment, DrawerLayout drawerLayout, ViewPager viewPager) {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }

        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0, true);
            return;
        }

        mainFragment.getActivity().finish();
    }

    public void updateRegId() {
        boolean isUserLoggedIn = SharedPreUtils.getInstance(mContext).isUserSignedIn();
        boolean hasDeviceToken = SharedPreUtils.getInstance(mContext).getHasDeviceToken();

        if (!hasDeviceToken && isUserLoggedIn && ServiceUtils.isNetworkAvailable(mContext)) {
            String token = SharedPreUtils.getInstance(mContext).getToken();
            String id = SharedPreUtils.getInstance(mContext).getUserID();
            String regID = FirebaseInstanceId.getInstance().getToken();

            UpdateFirebaseRegId.updateRegId(mContext, token, id, regID);
        }
    }
}