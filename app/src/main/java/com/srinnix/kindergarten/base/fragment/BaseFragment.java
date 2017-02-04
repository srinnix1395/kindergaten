package com.srinnix.kindergarten.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;

/**
 * Created by DELL on 2/3/2017.
 */

public abstract class BaseFragment extends Fragment implements BaseDelegate {
	protected Context mContext;
	protected View mView;
	protected BasePresenter mPresenter;
	
	public BaseFragment() {
		mPresenter = initPresenter();
		if (mPresenter == null) {
			mPresenter = new BasePresenter(this);
		}
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflateView(inflater, container);
	}
	
	private View inflateView(LayoutInflater inflater, ViewGroup container) {
		mView = inflater.inflate(getLayoutId(), container, false);
		return mView;
	}
	
	protected abstract int getLayoutId();
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		if (mPresenter != null) {
			mPresenter.setContext(mContext);
		}
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// get data transfer
		if (getArguments() != null) {
			getData();
			Bundle bundle = getArguments();
			mPresenter.getData(bundle);
		}

		initChildView();
	}
	
	protected abstract void initChildView();
	
	protected abstract BasePresenter initPresenter();
	
	protected void getData() {
		
	}
}
