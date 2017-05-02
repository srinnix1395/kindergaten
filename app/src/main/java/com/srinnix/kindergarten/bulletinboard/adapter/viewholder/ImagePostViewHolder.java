package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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

    private OnClickRemoveListener listener;

    public ImagePostViewHolder(View itemView, OnClickRemoveListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(ImageLocal image) {
        Glide.with(itemView.getContext())
                .load(image.getPath())
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.image_error)
                .thumbnail(0.5f)
                .into(imvImage);
    }

    @OnClick(R.id.imageview_remove)
    void onClickRemove() {
        if (listener != null) {
            listener.onClickRemove(getAdapterPosition());
        }
    }

    public interface OnClickRemoveListener {
        void onClickRemove(int position);
    }
}
