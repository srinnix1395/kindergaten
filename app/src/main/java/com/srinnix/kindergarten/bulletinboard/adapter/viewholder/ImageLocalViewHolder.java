package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.ImageLocal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImageLocalViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.imageview_alpha)
    ImageView imvAlpha;

    private int position;

    public ImageLocalViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
    }

    public void bindData(ImageLocal image, int position) {
        this.position = position;

        Glide.with(itemView.getContext())
                .load(image.getPath())
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.image_error)
                .into(imvImage);
        bindSelected(image.isSelected());
    }

    public void bindSelected(Boolean isSelected) {
        if (isSelected) {
            imvAlpha.setImageResource(R.drawable.background_item_image_picker);
        } else {
            imvAlpha.setImageDrawable(null);
        }
    }

    public interface OnClickViewHolderListener {
        void onClick(int position);
    }
}
