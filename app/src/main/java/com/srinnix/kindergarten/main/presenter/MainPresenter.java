package com.srinnix.kindergarten.main.presenter;

import android.os.Bundle;
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
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.fragment.CameraFragment;
import com.srinnix.kindergarten.chat.fragment.ChatListFragment;
import com.srinnix.kindergarten.children.fragment.ChildrenListFragment;
import com.srinnix.kindergarten.clazz.fragment.ClassListFragment;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.fragment.MainFragment;
import com.srinnix.kindergarten.service.UpdateFirebaseRegId;
import com.srinnix.kindergarten.setting.fragment.AccountFragment;
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
    private int currentPosition = 5;
    private int accountType;

    public MainPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        accountType = SharedPreUtils.getInstance(mContext).getAccountType();
    }

    public void startActivitySetting() {
        ViewManager.getInstance().addFragment(new SettingFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void changeTabIcon(FragmentManager fragmentManager, int tabId) {
        switch (tabId) {
            case R.id.menu_item_news: {
                if (currentPosition == AppConstant.FRAGMENT_BULLETIN_BOARD) {
                    return;
                }
                break;
            }
            case R.id.menu_item_class: {
                if (currentPosition == AppConstant.FRAGMENT_CLASS) {
                    return;
                }
                break;
            }
            case R.id.menu_item_camera: {
                if (currentPosition == AppConstant.FRAGMENT_CAMERA) {
                    return;
                }
                break;
            }
            case R.id.menu_item_children: {
                if (currentPosition == AppConstant.FRAGMENT_CHILDREN) {
                    return;
                }
                break;
            }
        }
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
                transaction.setCustomAnimations(R.anim.anim_show, 0);
                break;
            }
            case R.id.menu_item_class: {
                currentPosition = AppConstant.FRAGMENT_CLASS;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = initClassFragment();
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                } else {
                    transaction.setCustomAnimations(R.anim.anim_show, 0);
                }
                break;
            }
            case R.id.menu_item_camera: {
                currentPosition = AppConstant.FRAGMENT_CAMERA;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = new CameraFragment();
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                } else {
                    transaction.setCustomAnimations(R.anim.anim_show, 0);
                }
                break;
            }
            case R.id.menu_item_children: {
                currentPosition = AppConstant.FRAGMENT_CHILDREN;
                fragmentShow = fragmentManager.findFragmentByTag(String.valueOf(currentPosition));
                if (fragmentShow == null) {
                    fragmentShow = new ChildrenListFragment();
                    transaction.add(R.id.frame_layout_main, fragmentShow, String.valueOf(currentPosition));
                } else {
                    transaction.setCustomAnimations(R.anim.anim_show, 0);
                }
                break;
            }
        }

        if (fragmentShow == null) {
            return;
        }
        transaction.show(fragmentShow);
        transaction.commit();
    }

    private Fragment initClassFragment() {
        if (accountType == AppConstant.ACCOUNT_TEACHERS) {
            String classId = SharedPreUtils.getInstance(mContext).getClassId();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_CLASS, classId);
            return DetailClassFragment.newInstance(bundle);
        } else {
            return new ClassListFragment();
        }
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

        if (currentPosition == AppConstant.FRAGMENT_CHILDREN) {
            ChildrenListFragment fragmentChildren = (ChildrenListFragment) mainFragment
                    .getChildFragmentManager().findFragmentByTag(String.valueOf(currentPosition));
            fragmentChildren.onBackPressed();
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

    public void loginSuccessfully(Toolbar mToolbar) {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.main_menu_signed_in);

        KinderApplication.getInstance().getSocketUtil().connect(mContext);
    }

    public void removeUnUsedFragment(FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(String.valueOf(AppConstant.FRAGMENT_CLASS));
        if (fragment != null && currentPosition != AppConstant.FRAGMENT_CLASS ) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    public void onClickAccount() {
        ViewManager.getInstance().addFragment(new AccountFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}