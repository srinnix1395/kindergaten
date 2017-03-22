package com.srinnix.kindergarten.children.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.srinnix.kindergarten.children.adapter.viewholder.InfoChildrenViewHolder;
import com.srinnix.kindergarten.model.TimeLineChildren;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/8/2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<InfoChildrenViewHolder> {
    private ArrayList<TimeLineChildren> arrayList;

    public TimelineAdapter(ArrayList<TimeLineChildren> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public InfoChildrenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(InfoChildrenViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
