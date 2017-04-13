package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    ImageViewTouch imvImage;

    @BindView(R.id.videoview_preview)
    VideoView videoView;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_play_video)
    ImageView imvPlay;

    private Image image;
    private String transition;

    private MediaController mediaController;

    public static PreviewImageFragment newInstance(Image image) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setImage(image);
        return fragment;
    }

    public static PreviewImageFragment newInstance(Image image, String transitionName) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setImage(image);
        fragment.setTransition(transitionName);
        return fragment;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_image_class));
        }
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
            imvImage.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            initVideo();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setTransitionName(imvImage, transition);
            }

            videoView.setVisibility(View.GONE);
            imvImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            Glide.with(mContext)
                    .load(image.getUrl())
                    .dontAnimate()
                    .error(R.drawable.dummy_image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imvImage);
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
