package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.ImageDetailPostViewHolder;
import com.srinnix.kindergarten.model.Image;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/3/2017.
 */

public class ImageDetailPostAdapter extends RecyclerView.Adapter<ImageDetailPostViewHolder> {
    private ArrayList<Image> arrayList;
    private OnClickViewHolderListener listener;

    public ImageDetailPostAdapter(ArrayList<Image> arrayList, OnClickViewHolderListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public ImageDetailPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_detail_post, parent, false);
        return new ImageDetailPostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ImageDetailPostViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
