package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImagePostAdapter;
import com.srinnix.kindergarten.clazz.delegate.PostImageDelegate;
import com.srinnix.kindergarten.clazz.presenter.PostImagePresenter;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.MessageImagePostSuccessfully;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/11/2017.
 */

public class PostImageFragment extends BaseFragment implements PostImageDelegate {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textview_post)
    TextView tvPost;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_success)
    ImageView imvSuccess;

    @BindView(R.id.recyclerview_image)
    RecyclerView rvImages;

    private ArrayList<ImageLocal> mListImage;
    private ImagePostAdapter mAdapter;

    private PostImagePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_image;
    }

    @Override
    protected void initData() {
        super.initData();
        mListImage = new ArrayList<>();
        mAdapter = new ImagePostAdapter(mListImage, position -> {
            mListImage.remove(position);
            mAdapter.notifyItemRemoved(position);

            if (!mListImage.isEmpty()) {
                tvPost.setEnabled(true);
                tvPost.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tvPost.setEnabled(false);
                tvPost.setTextColor(Color.parseColor("#99ffffff"));
            }
        });
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Đăng ảnh vào lớp");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        tvPost.setEnabled(false);
        tvPost.setTextColor(Color.parseColor("#99ffffff"));

        rvImages.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
        rvImages.setLayoutManager(gridLayoutManager);

        rvImages.addItemDecoration(new SpacesItemDecoration(mContext, mAdapter, 2, 2, true));
    }

    @OnClick(R.id.textview_post)
    public void onClickPost() {
        UiUtils.hideKeyboard(getActivity());
        tvPost.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);
        mPresenter.onClickPost(mListImage);
    }

    @OnClick({R.id.imageview_image, R.id.imageview_video,
            R.id.imageview_facebook})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_image: {
                mPresenter.onClickImage(mListImage);
                break;
            }
            case R.id.imageview_video: {
                mPresenter.onClickVideo();
                break;
            }
            case R.id.imageview_facebook: {
                mPresenter.onClickFacebook();
                break;
            }
        }
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

    @Subscribe
    public void onEventPickImageLocal(MessageImageLocal message) {
        int sizeTotal = mListImage.size();
        if (sizeTotal > 0) {
            mListImage.clear();
            mAdapter.notifyItemRangeRemoved(0, sizeTotal);
        }
        mListImage.addAll(message.mListImage);
        mAdapter.notifyItemRangeInserted(0, message.mListImage.size());

        if (!mListImage.isEmpty()) {
            tvPost.setEnabled(true);
            tvPost.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tvPost.setEnabled(false);
            tvPost.setTextColor(Color.parseColor("#99ffffff"));
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new PostImagePresenter(this);
        return mPresenter;
    }

    @Override
    public void onSuccess(ArrayList<Image> data) {
        UiUtils.hideProgressBar(pbLoading);
        UiUtils.showView(imvSuccess);

        EventBus.getDefault().post(new MessageImagePostSuccessfully(data));
        super.onBackPressed();
    }

    @Override
    public void onFail() {
        UiUtils.hideProgressBar(pbLoading);
        UiUtils.showView(tvPost);
    }

    @Override
    public void onBackPressed() {
        if (!mListImage.isEmpty()) {
            AlertUtils.showDialogCancelPost(mContext, R.string.message_cancel_images, (dialog, which) -> {
                PostImageFragment.super.onBackPressed();
            });
            return;
        }

        super.onBackPressed();
    }
}
