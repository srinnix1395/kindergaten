package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.ImagePostViewHolder;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePostAdapter extends RecyclerView.Adapter<ImagePostViewHolder> {
    private ArrayList<String> listImage;
    private ImagePostViewHolder.OnClickViewHolderListener listener;

    public ImagePostAdapter(ArrayList<String> listImage, ImagePostViewHolder.OnClickViewHolderListener listener) {
        this.listImage = listImage;
        this.listener = listener;
    }

    @Override
    public ImagePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_post, parent, false);
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
