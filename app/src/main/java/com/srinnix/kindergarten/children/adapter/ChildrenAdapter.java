package com.srinnix.kindergarten.children.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.children.adapter.viewholder.ChildrenLinearViewHolder;
import com.srinnix.kindergarten.clazz.adapter.viewholder.ChildrenGridViewHolder;
import com.srinnix.kindergarten.model.Child;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ChildrenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_LINEAR = 0;
    public static final int TYPE_GRID = 1;

    private ArrayList<Child> arrayList;
    private int layoutType;
    private OnClickChildListener listener;

    public ChildrenAdapter(ArrayList<Child> arrayList, int layoutType, OnClickChildListener listener) {
        this.arrayList = arrayList;
        this.layoutType = layoutType;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutType == TYPE_LINEAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children_linear, parent, false);
            return new ChildrenLinearViewHolder(view, listener);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children_grid, parent, false);
        return new ChildrenGridViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (layoutType == TYPE_LINEAR) {
            ((ChildrenLinearViewHolder) holder).bindData(arrayList.get(position), position);
        } else {
            ((ChildrenGridViewHolder) holder).bindData(arrayList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnClickChildListener{
        void onClick(int position);
    }
}
