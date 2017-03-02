package com.srinnix.kindergarten.base.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by DELL on 2/3/2017.
 */

public abstract class BaseFragment extends Fragment implements BaseDelegate {
	protected Context mContext;
	protected View mView;
	protected BasePresenter mBasePresenter;
    private boolean isFirst;

    public BaseFragment() {
		mBasePresenter = initPresenter();
		if (mBasePresenter == null) {
			mBasePresenter = new BasePresenter(this);
		}
	}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isFirst = true;
    }

    @Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflateView(inflater, container);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflateView(inflater, (ViewGroup) getView());
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
		if (mBasePresenter != null) {
			mBasePresenter.setContext(mContext);
		}
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//bind
		ButterKnife.bind(this, view);
		
		// get data transfer
		if (getArguments() != null) {
			getData();
			Bundle bundle = getArguments();
			mBasePresenter.getData(bundle);
		}

		initChildView();

        if (isFirst) {
            mBasePresenter.onStart();
            isFirst = false;
        } else {
            mBasePresenter.onResume();
        }
	}
	
	protected abstract void initChildView();
	
	protected abstract BasePresenter initPresenter();
	
	protected void getData() {
		
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBasePresenter.onDestroy();
    }
}
