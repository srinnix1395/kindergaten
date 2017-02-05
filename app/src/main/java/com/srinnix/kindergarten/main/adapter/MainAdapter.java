package com.srinnix.kindergarten.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainAdapter extends FragmentPagerAdapter {
	private static final int PAGE_COUNT = 5;
	
	private ArrayList<Fragment> arrayList;
	
	public MainAdapter(FragmentManager fm, ArrayList<Fragment> arrayList) {
		super(fm);
		this.arrayList = arrayList;
	}
	
	@Override
	public Fragment getItem(int position) {
		return null;
	}
	
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
}
