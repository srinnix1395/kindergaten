package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.srinnix.kindergarten.bulletinboard.fragment.PreviewImageFragment;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/28/2017.
 */

public class PreviewImageAdapter extends FragmentStatePagerAdapter{
    private ArrayList<String> listImage;

    public PreviewImageAdapter(FragmentManager fm, ArrayList<String> listImage) {
        super(fm);
        this.listImage = listImage;
    }

    @Override
    public Fragment getItem(int position) {
        return PreviewImageFragment.newInstance(listImage.get(position));
    }

    @Override
    public int getCount() {
        return listImage.size();
    }
}
