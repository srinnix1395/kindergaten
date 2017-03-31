package com.srinnix.kindergarten.bulletinboard.fragment;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;

import butterknife.BindView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by anhtu on 3/28/2017.
 */

public class PreviewImageFragment extends BaseFragment {
    @BindView(R.id.touch_image_view)
    ImageViewTouch mImageView;

    private String url;

    public static PreviewImageFragment newInstance(String url) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setUrl(url);
        return fragment;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview_image;
    }

    @Override
    protected void initChildView() {
        mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Glide.with(mContext)
                .load(url)
                .error(R.drawable.dummy_image)
                .into(mImageView);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
