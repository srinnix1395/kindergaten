package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.model.Image;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/20/2017.
 */

public class ImageClassViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_video)
    ImageView imvVideo;

    private ImageAdapter.OnClickImageListener listener;

    public ImageClassViewHolder(View itemView, ImageAdapter.OnClickImageListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(Image image) {
        ViewCompat.setTransitionName(imvImage, "image_" + getAdapterPosition());

        if (image.isVideo()) {
            imvVideo.setVisibility(View.VISIBLE);

            Glide.with(itemView.getContext())
                    .load(image.getThumbnailUrl())
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .into(imvImage);
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
            listener.onClick(getAdapterPosition(), imvImage);
        }
    }
}
