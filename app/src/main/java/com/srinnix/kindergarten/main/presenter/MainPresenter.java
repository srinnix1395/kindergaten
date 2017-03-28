package com.srinnix.kindergarten.main.presenter;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.activity.DetailActivity;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.fragment.ChatListFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.delegate.MainDelegate;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.service.UpdateFirebaseRegId;
import com.srinnix.kindergarten.setting.activity.SettingActivity;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainPresenter extends BasePresenter {

    private int currentPosition = 0;

    private MainDelegate mMainDelegate;
    private boolean isFirstOpenMenuChat = true;
    private Disposable mDisposable;

    public MainPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mMainDelegate = (MainDelegate) mDelegate;
    }

    public void startActivityLogin() {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(AppConstant.SCREEN_ID, AppConstant.FRAGMENT_LOGIN);
        mContext.startActivity(intent);
    }

    public void startActivitySetting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        mContext.startActivity(intent);
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

    public void onClickMenuItemChat(Fragment mainFragment, DrawerLayout drawerLayout) {
        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            Toast.makeText(mContext, R.string.login_to_chat, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            if (isFirstOpenMenuChat) {
                ChatListFragment chatListFragment = new ChatListFragment();
                FragmentTransaction transaction = mainFragment.getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_menu, chatListFragment);
                transaction.commit();

                isFirstOpenMenuChat = false;
            }
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

            UpdateFirebaseRegId.updateRegId(mContext, mDisposable, token, id, regID);
        }
    }

    public void signOut() {
        // TODO: 3/20/2017 sign out
    }

    public void setupDrawerLayout(DrawerLayout mDrawer) {
        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void loginSuccessfully(Toolbar mToolbar) {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.main_menu_signed_in);

        KinderApplication.getInstance().getSocketUtil().connect(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}