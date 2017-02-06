package com.srinnix.kindergarten.camera.fragment;

import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.adapter.CameraAdapter;
import com.srinnix.kindergarten.model.Camera;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/6/2017.
 */

public class ListCameraFragment extends BaseFragment {
	@BindView(R.id.recycler_view_camera_list)
	RecyclerView recyclerView;
	
	private ArrayList<Camera> arrayList;
	private CameraAdapter adapter;
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_camera_list;
	}
	
	@Override
	protected void initChildView() {
		
	}
	
	@Override
	protected BasePresenter initPresenter() {
		return null;
	}
}
