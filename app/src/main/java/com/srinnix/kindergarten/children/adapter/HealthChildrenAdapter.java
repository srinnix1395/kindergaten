package com.srinnix.kindergarten.children.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.children.adapter.viewholder.HealthChildrenViewHolder;
import com.srinnix.kindergarten.model.HealthTotalChildren;
import com.srinnix.kindergarten.model.LoadingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhtu on 3/8/2017.
 */

public class HealthChildrenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_HEALTH = 2;

    private ArrayList<Object> arrayList;
    private HealthChildrenViewHolder.OnClickViewHolderListener listener;

    public HealthChildrenAdapter(ArrayList<Object> arrayList, HealthChildrenViewHolder.OnClickViewHolderListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEALTH) {
            View view = inflater.inflate(R.layout.item_timeline_children, parent, false);
            return new HealthChildrenViewHolder(view, listener);
        }

        View view = inflater.inflate(R.layout.item_loading, parent, false);
        return new LoadingViewHolder(view, null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        int size = payloads.size();
        if (size == 0) {
            onBindViewHolder(holder, position);
            return;
        }

        if (payloads.get(size - 1) instanceof Boolean) {
            ((HealthChildrenViewHolder) holder).bindViewDivider(((Boolean) payloads.get(size - 1)));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).bindData((LoadingItem) arrayList.get(position));
        } else {
            ((HealthChildrenViewHolder) holder).bindData((HealthTotalChildren) arrayList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof LoadingItem) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_HEALTH;
    }

}
