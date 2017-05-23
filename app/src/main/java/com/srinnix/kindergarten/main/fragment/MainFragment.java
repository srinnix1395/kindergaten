package com.srinnix.kindergarten.main.fragment;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.BulletinBoardFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.login.helper.LogoutHelper;
import com.srinnix.kindergarten.main.delegate.MainDelegate;
import com.srinnix.kindergarten.main.presenter.MainPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.messageeventbus.MessageLogout;
import com.srinnix.kindergarten.util.AlertUtils;
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

        if (getChildFragmentManager().findFragmentByTag(String.valueOf(AppConstant.FRAGMENT_BULLETIN_BOARD)) == null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.frame_layout_main, new BulletinBoardFragment(), String.valueOf(AppConstant.FRAGMENT_BULLETIN_BOARD));
            transaction.commit();
        }

        mBottomBar.setOnTabSelectListener(tabId -> mPresenter.changeTabIcon(getChildFragmentManager(), tabId), false);
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int i) {
                mPresenter.onReselectTab(getChildFragmentManager(), i);
            }
        });
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void onMenuItemItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sign_in: {
                mPresenter.addFragmentLogin();
                break;
            }
            case R.id.menu_item_sign_out: {
                AlertUtils.showDialogConfirm(mContext, R.string.confirm_log_out, R.string.sign_out, (dialog, which) -> LogoutHelper.signOut(mContext));
                break;
            }
            case R.id.menu_item_account: {
                mPresenter.onClickAccount();
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeFragment();
    }

    public void removeFragment() {
        mPresenter.removeUnUsedFragment(getChildFragmentManager());
    }

    @Subscribe
    public void onEventLoginSuccessfully(MessageLoginSuccessfully message) {
        mPresenter.loginSuccessfully(mToolbar);
    }

    @Subscribe
    public void onEventLogout(MessageLogout message) {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.main_menu_unsigned_in);
    }

    public void closeDrawer() {
        if (mDrawer.isDrawerOpen(Gravity.END)) {
            mDrawer.closeDrawer(Gravity.END, false);
        }
    }

    public void onBackPressed() {
        mPresenter.onBackPressed(this, mDrawer, mBottomBar);
    }

}
