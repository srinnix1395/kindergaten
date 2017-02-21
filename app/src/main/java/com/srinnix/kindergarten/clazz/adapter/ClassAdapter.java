package com.srinnix.kindergarten.clazz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.clazz.adapter.viewholder.ClassViewHolder;
import com.srinnix.kindergarten.model.Class;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder> {
    private ArrayList<Class> mArrayList;

    public ClassAdapter(ArrayList<Class> arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        holder.bindData(mArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
