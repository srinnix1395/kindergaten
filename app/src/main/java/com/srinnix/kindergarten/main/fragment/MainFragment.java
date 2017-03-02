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
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.fragment.ClassFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.adapter.MainAdapter;
import com.srinnix.kindergarten.main.presenter.MainPresenter;
import com.srinnix.kindergarten.schoolboard.fragment.SchoolBoardFragment;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class MainFragment extends BaseFragment {
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.tablayout_main)
    TabLayout tabLayout;

    @BindView(R.id.view_pager_main)
    ViewPager viewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private SchoolBoardFragment schoolBoardFragment;
    private MainAdapter adapter;
    private ArrayList<Fragment> arrayList;

    private MainPresenter mPresenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(AppConstant.TITLE_TAB[0]);
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            toolbar.inflateMenu(R.menu.main_menu_signed_in);
        } else {
            toolbar.inflateMenu(R.menu.main_menu_unsigned_in);
        }
        toolbar.setOnMenuItemClickListener(item -> {
            onMenuItemItemSelected(item);
            return false;
        });

        arrayList = new ArrayList<>();
        arrayList.add(SchoolBoardFragment.newInstance());
        arrayList.add(ClassFragment.newInstance());
        arrayList.add(SchoolBoardFragment.newInstance());
        arrayList.add(SchoolBoardFragment.newInstance());

        adapter = new MainAdapter(getChildFragmentManager(), arrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(AppConstant.ICON_TAB_SELECTED[0]);
        tabLayout.getTabAt(1).setIcon(AppConstant.ICON_TAB_UNSELECTED[1]);
        tabLayout.getTabAt(2).setIcon(AppConstant.ICON_TAB_UNSELECTED[2]);
        tabLayout.getTabAt(3).setIcon(AppConstant.ICON_TAB_UNSELECTED[3]);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.changeTabIcon(toolbar, tabLayout, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onMenuItemItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sign_in: {

                break;
            }
            case R.id.menu_item_sign_out: {

                break;
            }
            case R.id.menu_item_about: {

                break;
            }
            case R.id.menu_item_chat: {
                mPresenter.onClickMenuItemChat(drawerLayout);
                break;
            }
            case R.id.menu_item_setting: {

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

    public void onBackPressed() {
        mPresenter.onBackPressed(this, drawerLayout, viewPager);
    }
}
