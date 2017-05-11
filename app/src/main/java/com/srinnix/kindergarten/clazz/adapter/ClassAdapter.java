package com.srinnix.kindergarten.clazz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.viewholder.HeaderViewHolder;
import com.srinnix.kindergarten.clazz.adapter.viewholder.ClassViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_CLASS = 2;

    private ArrayList<Object> arrayList;
    private OnClickClassItemListener mItemListener;

    public ClassAdapter(ArrayList<Object> arrayList, OnClickClassItemListener itemListener) {
        this.arrayList = arrayList;
        mItemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_chat, parent
                    , false);
            return new HeaderViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view, mItemListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            ((HeaderViewHolder) holder).bindData((String) arrayList.get(position));
        } else {
            ((ClassViewHolder) holder).bindData((com.srinnix.kindergarten.model.Class) arrayList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof String) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_CLASS;
    }

    public interface OnClickClassItemListener {
        void onClick(int position);
    }
}
