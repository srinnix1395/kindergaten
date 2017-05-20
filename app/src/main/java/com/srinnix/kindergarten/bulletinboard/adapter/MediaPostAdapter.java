package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.MediaPostViewHolder;
import com.srinnix.kindergarten.custom.SquareItemLayout;
import com.srinnix.kindergarten.model.MediaLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/24/2017.
 */

public class MediaPostAdapter extends RecyclerView.Adapter<MediaPostViewHolder> {
    private ArrayList<MediaLocal> listMedia;
    private MediaPostViewHolder.OnClickRemoveListener listener;
    private int type;

    public MediaPostAdapter(ArrayList<MediaLocal> listMedia, MediaPostViewHolder.OnClickRemoveListener listener, int type) {
        this.listMedia = listMedia;
        this.listener = listener;
        this.type = type;
    }

    public MediaPostAdapter(ArrayList<MediaLocal> listMedia, MediaPostViewHolder.OnClickRemoveListener listener) {
        this.listMedia = listMedia;
        this.listener = listener;
        this.type = SquareItemLayout.TYPE_WIDTH;
    }

    @Override
    public MediaPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_post, parent, false);
        if (type == SquareItemLayout.TYPE_HEIGHT) {
            ((SquareItemLayout) view).setMeasureType(SquareItemLayout.TYPE_HEIGHT);
        }
        return new MediaPostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MediaPostViewHolder holder, int position) {
        holder.bindData(listMedia.get(position));
    }

    @Override
    public int getItemCount() {
        return listMedia.size();
    }
}
