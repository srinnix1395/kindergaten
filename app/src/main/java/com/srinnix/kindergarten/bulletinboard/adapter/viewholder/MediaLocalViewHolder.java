package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 4/24/2017.
 */

public class MediaLocalViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_alpha)
    ImageView imvAlpha;

    @BindView(R.id.textview_gif)
    TextView tvGif;

    @BindView(R.id.textview_video)
    TextView tvVideo;

    public MediaLocalViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(getAdapterPosition());
            }
        });
    }

    public void bindData(MediaLocal media) {
        if (media.isVideo()) {
            Glide.with(itemView.getContext())
                    .load(media.getUrlThumbnail())
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_error)
                    .into(imvImage);

            tvVideo.setText(StringUtil.getDurationVideo(media.getDuration()));

            UiUtils.showView(tvVideo);
            UiUtils.hideView(tvGif);
        } else {
            UiUtils.hideView(tvVideo);

            if (media.isGIF()) {
                Glide.with(itemView.getContext())
                        .load(media.getPath())
                        .asBitmap()
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.image_error)
                        .into(imvImage);
                UiUtils.showView(tvGif);
            } else {
                Glide.with(itemView.getContext())
                        .load(media.getPath())
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.image_error)
                        .into(imvImage);
                UiUtils.hideView(tvGif);
            }
        }

        bindSelected(media.isSelected());
    }

    public void bindSelected(Boolean isSelected) {
        if (isSelected) {
            imvAlpha.setImageResource(R.drawable.background_item_image_picker);
        } else {
            imvAlpha.setImageDrawable(null);
        }
    }
}
