package com.srinnix.kindergarten.clazz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.clazz.adapter.viewholder.ImageViewHolder;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.LoadingItem;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/20/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_IMAGE = 2;

    private ArrayList<Object> arrayList;
    private OnClickImageListener listener;

    public ImageAdapter(ArrayList<Object> arrayList, OnClickImageListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_small, parent, false);
            viewHolder = new LoadingViewHolder(view, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_class, parent, false);
            viewHolder = new ImageViewHolder(view, listener);
        }
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).onDestroy();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).bindData((LoadingItem) arrayList.get(position));
        } else {
            ((ImageViewHolder) holder).bindData(((Image) arrayList.get(position)), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrayList.size() - 1) {
            return VIEW_TYPE_LOADING;
        }

        return VIEW_TYPE_IMAGE;
    }

    public interface OnClickImageListener {
        void onClick(int position);
    }
}
