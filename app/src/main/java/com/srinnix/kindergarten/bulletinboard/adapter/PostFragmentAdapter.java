package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/1/2017.
 */

public class PostFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> arrayList;

    public PostFragmentAdapter(FragmentManager fm, ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
