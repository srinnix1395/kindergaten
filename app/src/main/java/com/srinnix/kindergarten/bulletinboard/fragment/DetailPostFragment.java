package com.srinnix.kindergarten.bulletinboard.fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PreviewImageAdapter;
import com.srinnix.kindergarten.constant.AppConstant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by anhtu on 3/24/2017.
 */

public class DetailPostFragment extends BaseFragment {
    @BindView(R.id.viewpager_image)
    ViewPager mViewPager;

    @BindView(R.id.cardview_left)
    CardView cardViewLeft;

    @BindView(R.id.cardview_right)
    CardView cardViewRight;

    private ArrayList<String> mListImage;
    private int currentPosition = 0;

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
        if (mListImage.size() == 1) {
            cardViewLeft.setVisibility(View.GONE);
            cardViewRight.setVisibility(View.GONE);
        }

        PreviewImageAdapter mAdapter = new PreviewImageAdapter(getChildFragmentManager(), mListImage);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (position == 0) {
                    cardViewLeft.setVisibility(View.GONE);
                    cardViewRight.setVisibility(View.VISIBLE);
                } else if (position == mListImage.size() - 1) {
                    cardViewRight.setVisibility(View.GONE);
                    cardViewLeft.setVisibility(View.VISIBLE);
                } else {
                    if (cardViewLeft.getVisibility() != View.VISIBLE) {
                        cardViewLeft.setVisibility(View.VISIBLE);
                    }
                    if (cardViewRight.getVisibility() != View.VISIBLE) {
                        cardViewRight.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnTouch({R.id.cardview_left, R.id.cardview_right})
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.setAlpha(0.15f);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                v.setAlpha(1f);
                break;
            }
        }
        return false;
    }

    @OnClick({R.id.cardview_left, R.id.cardview_right})
    public void onClickArrow(View v) {
        switch (v.getId()) {
            case R.id.cardview_left: {
                mViewPager.setCurrentItem(currentPosition - 1, true);
                break;
            }
            case R.id.cardview_right: {
                mViewPager.setCurrentItem(currentPosition + 1, true);
                break;
            }
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
