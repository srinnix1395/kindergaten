package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 5/2/2017.
 */

public class ImageDetailPostViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_play_video)
    ImageView imvPlay;

    private Disposable disposable;

    public ImageDetailPostViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(getAdapterPosition());
            }
        });
    }

    public void bindData(Image image) {
        if (image.isVideo()) {
            imvPlay.setVisibility(View.VISIBLE);
            if (!ServiceUtils.isNetworkAvailable(itemView.getContext())) {
                imvImage.setImageResource(R.drawable.dummy_image);
                return;
            }
            disposable = Single.fromCallable(() -> {
                try {
                    return UiUtils.retrieveVideoFrameFromVideo(image.getUrl());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.dummy_image);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> imvImage.setImageBitmap(bitmap));
        } else {
            Glide.with(itemView.getContext())
                    .load(image.getUrl())
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .into(imvImage);

            imvPlay.setVisibility(View.GONE);
        }
    }

    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
