package com.srinnix.kindergarten.children.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.children.adapter.viewholder.ChildrenLinearViewHolder;
import com.srinnix.kindergarten.model.Child;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenLinearViewHolder> {
    private ArrayList<Child> arrayList;

    public ChildrenAdapter(ArrayList<Child> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ChildrenLinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children_linear, parent, false);
        return new ChildrenLinearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildrenLinearViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
