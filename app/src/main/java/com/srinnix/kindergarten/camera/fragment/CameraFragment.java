package com.srinnix.kindergarten.camera.fragment;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.presenter.CameraPresenter;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraFragment extends BaseFragment {
	
	private CameraPresenter mPresenter;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_camera;
	}
	
	@Override
	protected void initChildView() {
		
	}
	
	@Override
	protected BasePresenter initPresenter() {
		mPresenter = new CameraPresenter(this);
		return mPresenter;
	}
}
