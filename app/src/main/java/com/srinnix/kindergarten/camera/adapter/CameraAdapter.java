package com.srinnix.kindergarten.camera.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.camera.adapter.viewholder.CameraViewHolder;
import com.srinnix.kindergarten.model.Camera;

import java.util.ArrayList;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraAdapter extends RecyclerView.Adapter<CameraViewHolder> {
	private ArrayList<Camera> arrayList;
	private OnClickCameraItemListener listener;
	
	public CameraAdapter(ArrayList<Camera> arrayList, OnClickCameraItemListener listener) {
		this.arrayList = arrayList;
		this.listener = listener;
	}
	
	@Override
	public CameraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
		return new CameraViewHolder(view, listener);
	}
	
	@Override
	public void onBindViewHolder(CameraViewHolder holder, int position) {
		holder.bindData(arrayList.get(position), position);
	}
	
	@Override
	public int getItemCount() {
		return arrayList.size();
	}
	
	public interface OnClickCameraItemListener{
		void onClick(int position);
	}
}
