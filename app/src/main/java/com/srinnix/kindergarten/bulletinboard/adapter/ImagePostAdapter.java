package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.ImagePostViewHolder;
import com.srinnix.kindergarten.custom.SquareItemLayout;
import com.srinnix.kindergarten.model.ImageLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostViewHolder> {
    private ArrayList<ImageLocal> listImage;
    private ImagePostViewHolder.OnClickRemoveListener listener;
    private int type;

    public ImagePostAdapter(ArrayList<ImageLocal> listImage, ImagePostViewHolder.OnClickRemoveListener listener, int type) {
        this.listImage = listImage;
        this.listener = listener;
        this.type = type;
    }

    public ImagePostAdapter(ArrayList<ImageLocal> listImage, ImagePostViewHolder.OnClickRemoveListener listener) {
        this.listImage = listImage;
        this.listener = listener;
        this.type = SquareItemLayout.TYPE_WIDTH;
    }

    @Override
    public ImagePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_post, parent, false);
        if (type == SquareItemLayout.TYPE_HEIGHT) {
            ((SquareItemLayout) view).setMeasureType(SquareItemLayout.TYPE_HEIGHT);
        }
        return new ImagePostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ImagePostViewHolder holder, int position) {
        holder.bindData(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
}
