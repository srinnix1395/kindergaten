package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    private OnClickViewHolderListener listener;

    private int position;

    public ImagePostViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(String uri, int position) {
        this.position = position;

        Glide.with(itemView.getContext())
                .load(uri)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.image_error)
                .thumbnail(0.5f)
                .into(imvImage);
    }

    @OnClick(R.id.imageview_remove)
    void onClickRemove() {
        if (listener != null) {
            listener.onClickRemove(position);
        }
    }

    public interface OnClickViewHolderListener {
        void onClickRemove(int position);
    }
}
