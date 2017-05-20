package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class MediaPostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.textview_gif)
    TextView tvGif;

    @BindView(R.id.textview_video)
    TextView tvVideo;

    private  OnClickRemoveListener listener;

    public MediaPostViewHolder(View itemView, OnClickRemoveListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
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

    }

    @OnClick(R.id.imageview_remove)
    public void onClickRemove(){
        if (listener != null) {
            listener.onClickRemove(getAdapterPosition());
        }
    }

    public interface OnClickRemoveListener {
        void onClickRemove(int position);
    }
}
