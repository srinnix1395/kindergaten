package com.srinnix.kindergarten.main.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.main.adapter.MainAdapter;
import com.srinnix.kindergarten.main.presenter.MainPresenter;
import com.srinnix.kindergarten.schoolboard.fragment.SchoolBoardFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class MainFragment extends BaseFragment {
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
		arrayList = new ArrayList<>();

		adapter = new MainAdapter(getChildFragmentManager(), arrayList);
		viewPager.setAdapter(adapter);

		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_SELECTED[0]));
		tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_UNSELECTED[1]));
		tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_UNSELECTED[2]));
		tabLayout.addTab(tabLayout.newTab().setIcon(AppConstant.ICON_TAB_UNSELECTED[3]));

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				mPresenter.changeTabIcon(tabLayout, position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	@Override
	protected BasePresenter initPresenter() {
		mPresenter = new MainPresenter(this);
		return mPresenter;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		mPresenter.createOptionMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_sign_in: {

				break;
			}
			case R.id.menu_item_sign_out:{

				break;
			}
			case R.id.menu_item_about: {

				break;
			}
			case R.id.menu_item_chat: {

				break;
			}
			case R.id.menu_item_setting: {

				break;
			}
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

	}
}
