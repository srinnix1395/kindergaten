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
    protected BasePresenter<BaseDelegate> mBasePresenter;
    protected boolean isFirst;

    public BaseFragment() {
        mBasePresenter = initPresenter();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter<BaseDelegate>(this);
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

        if (isFirst) {
            initData();
        }

        initChildView();

        if (isFirst) {
            mBasePresenter.onStart();
            isFirst = false;
        } else {
            mBasePresenter.onResume();
        }

    }

    protected void initData() {

    }

    @Override
    public void setData() {

    }

    protected abstract void initChildView();

    protected abstract BasePresenter<BaseDelegate> initPresenter();

    protected void getData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mBasePresenter.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBasePresenter.onDestroy();
    }

    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getFragmentManager().popBackStack();
        } else {
            // back Activity;
            getActivity().finish();
        }
    }
}
