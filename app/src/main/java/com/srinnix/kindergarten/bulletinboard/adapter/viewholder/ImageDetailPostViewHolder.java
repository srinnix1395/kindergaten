package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.model.Image;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 5/2/2017.
 */

public class ImageDetailPostViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_play_video)
    ImageView imvPlay;

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

            imvPlay.setVisibility(View.GONE);
        }
    }
}
