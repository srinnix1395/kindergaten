package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by anhtu on 3/28/2017.
 */

public class PreviewImageFragment extends BaseFragment {
    @BindView(R.id.touch_image_view)
    ImageViewTouch imageViewImage;

    @BindView(R.id.videoview_preview)
    VideoView videoView;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_play_video)
    ImageView imvPlay;

    private Image image;

    private MediaController mediaController;

    public static PreviewImageFragment newInstance(Image image) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setImage(image);
        return fragment;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    protected void getData() {
        super.getData();
        image = getArguments().getParcelable(AppConstant.KEY_IMAGE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview_image;
    }

    @Override
    protected void initChildView() {
        if (image.isVideo()) {
            imageViewImage.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            initVideo();
        } else {
            videoView.setVisibility(View.GONE);
            imageViewImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            Glide.with(mContext)
                    .load(image.getUrl())
                    .error(R.drawable.dummy_image)
                    .into(imageViewImage);
        }

    }

    private void initVideo() {
        mediaController = new MediaController(mContext);
        mediaController.setAnchorView(videoView);

        imvPlay.setVisibility(View.VISIBLE);
        imvPlay.setOnClickListener(v -> playVideo());
    }

    private void playVideo() {
        try {
            UiUtils.showProgressBar(pbLoading);
            getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

            Uri video = Uri.parse(image.getUrl());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(mp -> {
                UiUtils.hideProgressBar(pbLoading);
                videoView.start();
            });

        } catch (Exception e) {
            UiUtils.hideProgressBar(pbLoading);
            AlertUtils.showToast(mContext, R.string.cant_play);
        }

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
