package com.srinnix.kindergarten.bulletinboard.fragment;

import android.support.v4.view.ViewPager;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PreviewImageAdapter;
import com.srinnix.kindergarten.constant.AppConstant;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by anhtu on 3/24/2017.
 */

public class DetailPostFragment extends BaseFragment {
    @BindView(R.id.viewpager_image)
    ViewPager mViewPager;

    private ArrayList<String> mListImage;
    private PreviewImageAdapter mAdapter;

    @Override
    protected void getData() {
        super.getData();
        mListImage = getArguments().getStringArrayList(AppConstant.KEY_IMAGE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_post;
    }

    @Override
    protected void initChildView() {
        mAdapter = new PreviewImageAdapter(getChildFragmentManager(), mListImage);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
