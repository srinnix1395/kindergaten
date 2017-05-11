package com.srinnix.kindergarten.bulletinboard.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.LikeAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.LikeDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.LikePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.LoadingItem3State;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 3/28/2017.
 */

public class LikeDialogFragment extends BottomSheetDialogFragment implements LikeDelegate {
    @BindView(R.id.toolbar_detail_like)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview_like)
    RecyclerView mRvLike;

    @BindView(R.id.layout_retry)
    RelativeLayout mRelRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar mPbLoading;

    @BindView(R.id.textview_retry)
    TextView mTvRetry;

    private ArrayList<Object> mListLike;
    private LikeAdapter mAdapter;
    private LikePresenter mPresenter;

    private int numberLike;
    private Context mContext;

    public LikeDialogFragment() {
        initPresenter();
    }

    private void getData() {
        Bundle bundle = getArguments();
        numberLike = bundle.getInt(AppConstant.KEY_LIKE);
        mPresenter.getData(bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (mPresenter != null) {
            mPresenter.setContext(mContext);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_like, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initChildView();
        mPresenter.onStart();
    }

    protected void initChildView() {
        getData();
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> this.dismiss());
        mToolbar.setTitle(String.format("%s %s", String.valueOf(numberLike), mContext.getString(R.string.likes)));
        mToolbar.setTitleTextColor(Color.WHITE);

        mPbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        mRvLike.setLayoutManager(new LinearLayoutManager(mContext));
        mListLike = new ArrayList<>();
        mAdapter = new LikeAdapter(mListLike, () -> {
            if (!mListLike.isEmpty() && (mListLike.get(0) instanceof LoadingItem3State)) {
                ((LoadingItem3State) mListLike.get(0)).setLoadingState(LoadingItem3State.STATE_LOADING);
                mAdapter.notifyItemChanged(0);
            }
            mPresenter.getListLike(((LikeModel) mListLike.get(1)).getCreatedAt());
        });
        mRvLike.setAdapter(mAdapter);
    }


    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        mRelRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(mPbLoading);
        mPresenter.getListLike(System.currentTimeMillis());
    }

    private void initPresenter() {
        mPresenter = new LikePresenter(this);
    }

    @Override
    public void onLoadSuccess(ArrayList<LikeModel> data, boolean isLoadFirst) {
        if (data.size() < AppConstant.ITEM_LIKE_PER_PAGE) {
            if (!mListLike.isEmpty() && mListLike.get(0) instanceof LoadingItem3State) {
                mListLike.remove(0);
                mAdapter.notifyItemRemoved(0);
            }
            if (!data.isEmpty()) {
                mListLike.addAll(data);
                mAdapter.notifyItemRangeInserted(0, data.size());
            }
        } else {
            if (mListLike.isEmpty()) {
                mListLike.add(0, new LoadingItem3State());
                mAdapter.notifyItemInserted(0);
            } else if ((mListLike.get(0) instanceof LoadingItem3State)) {
                ((LoadingItem3State) mListLike.get(0)).setLoadingState(LoadingItem3State.STATE_IDLE);
                mAdapter.notifyItemChanged(0);
            }
            mListLike.addAll(1, data);
            mAdapter.notifyItemRangeInserted(1, data.size());
        }

        if (isLoadFirst) {
            if (!mListLike.isEmpty()) {
                mRvLike.smoothScrollToPosition(data.size() - 1);
            }
            mRvLike.setVisibility(View.VISIBLE);
            UiUtils.hideProgressBar(mPbLoading);
        }
    }

    @Override
    public void onLoadFail(int resError, boolean isLoadFirst) {
        if (isLoadFirst) {
            UiUtils.hideProgressBar(mPbLoading);

            mTvRetry.setText(resError);
            UiUtils.showView(mRelRetry);
        } else if (!mListLike.isEmpty() && (mListLike.get(0) instanceof LoadingItem3State)) {
            ((LoadingItem3State) mListLike.get(0)).setLoadingState(LoadingItem3State.STATE_IDLE);
            mAdapter.notifyItemChanged(0);
        }
    }
}
