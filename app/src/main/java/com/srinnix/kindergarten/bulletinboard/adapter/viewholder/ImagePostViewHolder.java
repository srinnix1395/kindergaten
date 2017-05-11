package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.ImageLocal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.textview_gif)
    TextView tvGif;

    private  OnClickRemoveListener listener;

    public ImagePostViewHolder(View itemView, OnClickRemoveListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void bindData(ImageLocal image) {
        if (image.isGIF()) {
            Glide.with(itemView.getContext())
                    .load(image.getPath())
                    .asBitmap()
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_error)
                    .thumbnail(0.5f)
                    .into(imvImage);
            tvGif.setVisibility(View.VISIBLE);
        } else {
            Glide.with(itemView.getContext())
                    .load(image.getPath())
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_error)
                    .thumbnail(0.5f)
                    .into(imvImage);
            tvGif.setVisibility(View.GONE);
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
