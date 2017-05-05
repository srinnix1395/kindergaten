package com.srinnix.kindergarten.clazz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.viewholder.ActionTimetableViewHolder;
import com.srinnix.kindergarten.model.ActionTimetable;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/4/2017.
 */

public class ActionTimetableAdapter extends RecyclerView.Adapter<ActionTimetableViewHolder> {
    private ArrayList<ActionTimetable> arrayList;

    public ActionTimetableAdapter(ArrayList<ActionTimetable> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ActionTimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action_timetable, parent, false);
        return new ActionTimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActionTimetableViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
