package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.MediaLocalViewHolder;
import com.srinnix.kindergarten.model.MediaLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhtu on 4/24/2017.
 */

public class MediaPickerAdapter extends RecyclerView.Adapter<MediaLocalViewHolder> {
    private ArrayList<MediaLocal> arrayList;
    private OnClickViewHolderListener listener;

    public MediaPickerAdapter(ArrayList<MediaLocal> arrayList, OnClickViewHolderListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public MediaLocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_picker, parent, false);
        return new MediaLocalViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MediaLocalViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        int size = payloads.size();

        if (size == 0) {
            onBindViewHolder(holder, position);
            return;
        }

        if (payloads.get(size - 1) instanceof Boolean) {
            holder.bindSelected(((Boolean) payloads.get(size - 1)));
        }
    }

    @Override
    public void onBindViewHolder(MediaLocalViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
