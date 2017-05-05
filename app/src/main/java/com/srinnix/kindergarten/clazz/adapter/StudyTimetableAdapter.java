package com.srinnix.kindergarten.clazz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.viewholder.HeaderTimetableViewHolder;
import com.srinnix.kindergarten.clazz.adapter.viewholder.StudyTimetableViewHolder;
import com.srinnix.kindergarten.model.HeaderTimetable;
import com.srinnix.kindergarten.model.StudySchedule;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/5/2017.
 */

public class StudyTimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_STUDY = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private ArrayList<Object> arrayList;

    public StudyTimetableAdapter(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_STUDY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_timetable, parent, false);
                return new StudyTimetableViewHolder(view);
            }
            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_timetable, parent, false);
                return new HeaderTimetableViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_STUDY: {
                ((StudyTimetableViewHolder) holder).bindData((StudySchedule) arrayList.get(position));
                break;
            }
            case VIEW_TYPE_HEADER: {
                ((HeaderTimetableViewHolder) holder).bindData((HeaderTimetable) arrayList.get(position), position);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof StudySchedule) {
            return VIEW_TYPE_STUDY;
        }

        return VIEW_TYPE_HEADER;
    }
}
