package com.srinnix.kindergarten.main.fragment;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.fragment.ContainerFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.BulletinBoardFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.adapter.MainAdapter;
import com.srinnix.kindergarten.main.delegate.MainDelegate;
import com.srinnix.kindergarten.main.presenter.MainPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class MainFragment extends BaseFragment implements MainDelegate {
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @BindView(R.id.tablayout_main)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager_main)
    ViewPager mViewPager;

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
        mToolbar.setTitle(AppConstant.TITLE_TAB[0]);
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            mToolbar.inflateMenu(R.menu.main_menu_signed_in);
        } else {
            mToolbar.inflateMenu(R.menu.main_menu_unsigned_in);
        }
        mToolbar.setOnMenuItemClickListener(item -> {
            onMenuItemItemSelected(item);
            return false;
        });

        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(BulletinBoardFragment.newInstance());
        arrayList.add(new ContainerFragment());
        arrayList.add(new ContainerFragment());
        arrayList.add(new ContainerFragment());

        MainAdapter adapter = new MainAdapter(getChildFragmentManager(), arrayList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(AppConstant.ICON_TAB_SELECTED[0]);
        mTabLayout.getTabAt(1).setIcon(AppConstant.ICON_TAB_UNSELECTED[1]);
        mTabLayout.getTabAt(2).setIcon(AppConstant.ICON_TAB_UNSELECTED[2]);
        mTabLayout.getTabAt(3).setIcon(AppConstant.ICON_TAB_UNSELECTED[3]);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.changeTabIcon(mToolbar, mTabLayout, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPresenter.setupDrawerLayout(mDrawer);
    }

    private void onMenuItemItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sign_in: {
                mPresenter.startActivityLogin();
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

    @Subscribe(sticky = true)
    public void onEventLoginSuccessfully(MessageLoginSuccessfully message) {
        EventBus.getDefault().removeStickyEvent(MessageLoginSuccessfully.class);
        mPresenter.loginSuccessfully(mToolbar);
    }

    public void onBackPressed() {
        mPresenter.onBackPressed(this, mDrawer, mViewPager);
    }
}
