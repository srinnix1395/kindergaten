package com.srinnix.kindergarten.main.presenter;

import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuInflater;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.SharedPreUtils;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainPresenter extends BasePresenter {
	private int currentPosition = 0;
	
	public MainPresenter(BaseDelegate mDelegate) {
		super(mDelegate);
	}
	
	public void changeTabIcon(TabLayout tabLayout, int position) {
		if (tabLayout == null || tabLayout.getTabAt(position) == null
				|| tabLayout.getTabAt(currentPosition) == null) {
			return;
		}
		
		tabLayout.getTabAt(position).setIcon(AppConstant.ICON_TAB_SELECTED[position]);
		tabLayout.getTabAt(currentPosition).setIcon(AppConstant.ICON_TAB_UNSELECTED[currentPosition]);
		currentPosition = position;
	}
	
	public void createOptionMenu(Menu menu, MenuInflater inflater) {
		if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
			inflater.inflate(R.menu.main_menu_signed_in, menu);
		} else {
			inflater.inflate(R.menu.main_menu_unsigned_in, menu);
		}
	}
}
