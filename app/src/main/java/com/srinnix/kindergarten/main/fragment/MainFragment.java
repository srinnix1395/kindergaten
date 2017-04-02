package com.srinnix.kindergarten.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.main.delegate.MainDelegate;
import com.srinnix.kindergarten.main.presenter.MainPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class MainFragment extends BaseFragment implements MainDelegate {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @BindView(R.id.bottom_navigation)
    BottomBar mBottomBar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    private MainPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initChildView() {
        mPresenter.updateRegId();

        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Kids home");
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            mToolbar.inflateMenu(R.menu.main_menu_signed_in);
        } else {
            mToolbar.inflateMenu(R.menu.main_menu_unsigned_in);
        }
        mToolbar.setOnMenuItemClickListener(item -> {
            onMenuItemItemSelected(item);
            return false;
        });

        mBottomBar.setOnTabSelectListener(tabId -> mPresenter.changeTabIcon(getChildFragmentManager(), tabId));
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void onMenuItemItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sign_in: {
                mPresenter.addFragmentLogin();
                break;
            }
            case R.id.menu_item_sign_out: {
                mPresenter.signOut();
                break;
            }
            case R.id.menu_item_about: {

                break;
            }
            case R.id.menu_item_chat: {
                mPresenter.onClickMenuItemChat(this, mDrawer);
                break;
            }
            case R.id.menu_item_setting: {
                mPresenter.startActivitySetting();
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new MainPresenter(this);
        return mPresenter;
    }

    @Override
    public void inflateMenuToolbarLogin() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.main_menu_signed_in);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventLoginSuccessfully(MessageLoginSuccessfully message) {
        mPresenter.loginSuccessfully(mToolbar);
    }

    public void onBackPressed() {
        mPresenter.onBackPressed(this, mDrawer);
    }
}
