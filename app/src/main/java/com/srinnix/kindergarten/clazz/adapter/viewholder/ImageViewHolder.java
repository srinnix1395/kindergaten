package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 2/20/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_video)
    ImageView imvVideo;

    private Disposable disposable;
    private ImageAdapter.OnClickImageListener listener;
    private int position;

    public ImageViewHolder(View itemView, ImageAdapter.OnClickImageListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(Image image, int position) {
        this.position = position;
        ViewCompat.setTransitionName(imvImage, "image_" + position);

        if (image.isVideo()) {
            disposable = Single.fromCallable(() -> {
                try {
                    return UiUtils.retrieveVideoFrameFromVideo(image.getUrl());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        if (bitmap != null) {
                            imvImage.setImageBitmap(bitmap);
                        }
                    });
            imvVideo.setVisibility(View.VISIBLE);
        } else {
            Glide.with(itemView.getContext())
                    .load(image.getUrl())
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .into(imvImage);

            imvVideo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.imageview_image)
    void onClick() {
        if (listener != null) {
            listener.onClick(position, imvImage);
        }
    }

    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
