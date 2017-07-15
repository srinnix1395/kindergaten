package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImageDetailPostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.DetailPostDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.DetailPostPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageNumberComment;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/2/2017.
 */

public class DetailPostFragment extends BaseFragment implements DetailPostDelegate {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_created_at)
    TextView tvCreatedAt;

    @BindView(R.id.textview_content)
    TextView tvContent;

    @BindView(R.id.textview_number_like)
    TextView tvNumberLike;

    @BindView(R.id.textview_number_comment)
    TextView tvNumberComment;

    @BindView(R.id.imageview_like)
    ImageView imvLike;

    @BindView(R.id.recyclerview_image)
    RecyclerView rvImage;

    @BindView(R.id.layout_content)
    CoordinatorLayout layoutContent;

    private ImageDetailPostAdapter mAdapter;
    private ArrayList<Image> mListImage;

    private DetailPostPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_post;
    }

    @Override
    protected void initData() {
        super.initData();

        mListImage = new ArrayList<>();
        mAdapter = new ImageDetailPostAdapter(mListImage, position -> {
            mPresenter.onClickImage(mListImage);
        });
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Thông báo");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        rvImage.setLayoutManager(new LinearLayoutManager(mContext));
        rvImage.setAdapter(mAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new DetailPostPresenter(this);
        return mPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.getSrcScreen() == AppConstant.FRAGMENT_BULLETIN_BOARD) {
            super.onBackPressed();
        } else {
            getActivity().onBackPressed();
        }
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        layoutRetry.setVisibility(View.GONE);

        UiUtils.showProgressBar(pbLoading);
        mPresenter.getDetailPost();
    }

    @OnClick(R.id.imageview_like)
    public void onClickLike() {
        mPresenter.onClickLike();
    }

    @OnClick({R.id.textview_number_comment, R.id.imageview_comment})
    public void onClickComment() {
        mPresenter.onClickComment();
    }

    @OnClick(R.id.textview_number_like)
    public void onClickNumberLike() {
        mPresenter.onClickNumberLike(getChildFragmentManager());
    }

    @OnClick(R.id.imageview_share)
    public void onClickShare() {
        mPresenter.onClickShare();
    }

    @Override
    public void likeSuccess(Post post, LikeResponse data) {
        imvLike.setImageResource(data.isLike() ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
        tvNumberLike.setText(String.format(Locale.getDefault(),
                "%d %s", post.getNumberOfLikes(), getString(R.string.likes)));

        if (data.isLike()) {
            AlertUtils.showToastSuccess(mContext, R.drawable.ic_heart_white, R.string.liked);
        } else {
            AlertUtils.showToastSuccess(mContext, R.drawable.ic_heart_broken_white, R.string.unliked);
        }
    }

    @Subscribe
    public void onEventNumberComment(MessageNumberComment message) {
        mPresenter.updateNumberComment(message.numberComment);
        tvNumberComment.setText(String.format(Locale.getDefault(),
                "%d %s", message.numberComment, getString(R.string.comment1)));
    }

    @Override
    public void onSuccess(Post post) {
        UiUtils.hideProgressBar(pbLoading);

        UiUtils.showView(layoutContent);

        tvContent.setText(post.getContent());
        tvCreatedAt.setText(StringUtil.getTimeAgo(mContext, post.getCreatedAt()));
        imvLike.setImageResource(post.isUserLike() ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
        tvNumberLike.setText(String.format(Locale.getDefault(),
                "%d %s", post.getNumberOfLikes(), getString(R.string.likes)));
        tvNumberComment.setText(String.format(Locale.getDefault(),
                "%d %s", post.getNumberOfComments(), getString(R.string.comment1)));

        if (post.getListImage() == null) {
            UiUtils.hideView(rvImage);
        } else {
            UiUtils.showView(rvImage);
            mListImage.addAll(post.getListImage());
            mAdapter.notifyItemRangeInserted(0, post.getListImage().size());
        }
    }

    @Override
    public void onFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);
        tvRetry.setText(resError);

        layoutRetry.setVisibility(View.VISIBLE);
    }
}
