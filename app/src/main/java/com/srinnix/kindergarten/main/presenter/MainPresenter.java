package com.srinnix.kindergarten.main.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.fragment.ContainerFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.fragment.ChatListFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.service.UpdateFirebaseRegId;
import com.srinnix.kindergarten.setting.fragment.SettingFragment;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import io.reactivex.disposables.Disposable;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainPresenter extends BasePresenter {

    private boolean isFirstOpenMenuChat = true;
    private Disposable mDisposable;
    private int currentPosition = 0;

    public MainPresenter(BaseDelegate mDelegate) {
        super(mDelegate);

    }

    public void startActivitySetting() {
        ViewManager.getInstance().addFragment(new SettingFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void changeTabIcon(FragmentManager fragmentManager, int tabId) {
        Fragment fragmentShow = null;

        Fragment fragmentHide = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentHide != null) {
            transaction.hide(fragmentHide);
        }

        switch (tabId) {
            case R.id.menu_item_news: {
                currentPosition = AppConstant.FRAGMENT_BULLETIN_BOARD;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                break;
            }
            case R.id.menu_item_class: {
                currentPosition = AppConstant.FRAGMENT_CLASS;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = ContainerFragment.newInstance(AppConstant.TYPE_CLASS_FRAGMENT);
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                }
                break;
            }
            case R.id.menu_item_camera: {
                currentPosition = AppConstant.FRAGMENT_CAMERA;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = ContainerFragment.newInstance(AppConstant.TYPE_CAMERA_FRAGMENT);
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                }
                break;
            }
            case R.id.menu_item_children: {
                currentPosition = AppConstant.FRAGMENT_CHILDREN;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = ContainerFragment.newInstance(AppConstant.TYPE_CHILDREN_FRAGMENT);
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                }
                break;
            }
        }
        transaction.setCustomAnimations(R.anim.anim_show, 0).show(fragmentShow);
        transaction.commit();
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
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            }
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void onBackPressed(MainFragment mainFragment, DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
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
        SharedPreUtils.getInstance(mContext).clearUserData();
        // TODO: 3/20/2017 sign out
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