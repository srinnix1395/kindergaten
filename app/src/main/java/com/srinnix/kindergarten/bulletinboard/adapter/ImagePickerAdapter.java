package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.OnClickViewHolderListener;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.ImageLocalViewHolder;
import com.srinnix.kindergarten.model.ImageLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePickerAdapter extends RecyclerView.Adapter<ImageLocalViewHolder> {
    private ArrayList<ImageLocal> arrayList;
    private OnClickViewHolderListener listener;

    public ImagePickerAdapter(ArrayList<ImageLocal> arrayList, OnClickViewHolderListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public ImageLocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_picker, parent, false);
        return new ImageLocalViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ImageLocalViewHolder holder, int position, List<Object> payloads) {
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
    public void onBindViewHolder(ImageLocalViewHolder holder, int position) {
        holder.bindData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
