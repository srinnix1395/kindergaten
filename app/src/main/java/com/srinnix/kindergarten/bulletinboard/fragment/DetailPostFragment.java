package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImageDetailPostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.DetailPostDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.DetailPostPresenter;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostFragment extends BaseFragment implements DetailPostDelegate{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progress_bar)
    ProgressBar pbLoading;

    @BindView(R.id.textview_created_at)
    TextView tvCreatedAt;

    @BindView(R.id.textview_content)
    TextView tvContent;

    @BindView(R.id.recycler_view_image)
    RecyclerView rvImage;

    private ImageDetailPostAdapter mAdapter;
    private ArrayList<Image> mListImage;

    private DetailPostPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_post;
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Thông báo");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

//        mAdapter = new RecyclerViewAdapter<>(new ImageDetailPostViewHolder(), mListImage, R.layout.image)

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new DetailPostPresenter(this);
        return mPresenter;
    }

    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry(){
        layoutRetry.setVisibility(View.GONE);

        UiUtils.showProgressBar(pbLoading);
        mPresenter.getDetailPost();
    }

    @Override
    public void onSuccess(Post data) {

    }

    @Override
    public void onFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);
        tvRetry.setTextColor(resError);

        layoutRetry.setVisibility(View.VISIBLE);
    }
}
