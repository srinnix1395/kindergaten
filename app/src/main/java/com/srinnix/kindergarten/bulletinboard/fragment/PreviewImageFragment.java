package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
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

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        postponeEnterTransition();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_image_class));
//        }
//    }

    @Override
    protected void getData() {
        super.getData();
        image = getArguments().getParcelable(AppConstant.KEY_MEDIA);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview_image;
    }

    @Override
    protected void initChildView() {
        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        if (image.isVideo()) {
            videoView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(image.getThumbnailUrl())
                    .dontAnimate()
                    .into(imvImage);

            initVideo();
        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                ViewCompat.setTransitionName(imvImage, transition);
//            }

            videoView.setVisibility(View.GONE);
            imvImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            Glide.with(mContext)
                    .load(image.getUrl())
                    .dontAnimate()
                    .error(R.drawable.dummy_image)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            startPostponedEnterTransition();
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            startPostponedEnterTransition();
//                            return false;
//                        }
//                    })
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
            UiUtils.hideView(imvImage);
            UiUtils.hideView(imvPlay);

            UiUtils.showProgressBar(pbLoading);

            Uri videoUri = Uri.parse(image.getUrl());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();
            videoView.setOnPreparedListener(mp -> {
                UiUtils.hideProgressBar(pbLoading);
                videoView.start();
            });
            videoView.setOnCompletionListener(mp -> {
                mp.stop();
                UiUtils.showView(imvImage);
                UiUtils.showView(imvPlay);
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
