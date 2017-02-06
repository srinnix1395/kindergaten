package com.srinnix.kindergarten.camera.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srinnix.kindergarten.camera.adapter.CameraAdapter;
import com.srinnix.kindergarten.model.Camera;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraViewHolder extends RecyclerView.ViewHolder {
	
	private int position;
	
	public CameraViewHolder(View itemView, CameraAdapter.OnClickCameraItemListener listener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		itemView.setOnClickListener(view -> {
			if (listener != null) {
				listener.onClick(position);
			}
		});
	}
	
	public void bindData(Camera camera, int position) {
		this.position = position;
		
		
	}
}
