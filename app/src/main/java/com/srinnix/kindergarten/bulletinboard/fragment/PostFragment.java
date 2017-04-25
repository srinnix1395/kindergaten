package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImagePostAdapter;
import com.srinnix.kindergarten.bulletinboard.presenter.PostPresenter;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.util.AlertUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostFragment extends BaseFragment {
    @BindView(R.id.toolbar_post)
    Toolbar toolbar;

    @BindView(R.id.textview_post)
    TextView tvPost;

    @BindView(R.id.textview_content)
    TextView tvContent;

    @BindView(R.id.recycler_view_image)
    RecyclerView rvImages;

    private ArrayList<String> mListImage;
    private ImagePostAdapter mAdapter;
    private PostPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post;
    }

    @Override
    protected void initData() {
        super.initData();
        mListImage = new ArrayList<>();
        mAdapter = new ImagePostAdapter(mListImage, position -> {
            mListImage.remove(position);
            mAdapter.notifyItemRemoved(position);

            if (!mListImage.isEmpty()) {
                rvImages.setVisibility(View.GONE);

                tvPost.setEnabled(true);
                tvPost.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tvPost.setEnabled(false);
                tvPost.setTextColor(Color.parseColor("#80ffffff"));
            }
        });
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Đăng vào bảng tin");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        tvPost.setEnabled(false);
        tvPost.setTextColor(Color.parseColor("#80ffffff"));

        tvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tvPost.setEnabled(true);
                    tvPost.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    tvPost.setEnabled(false);
                    tvPost.setTextColor(Color.parseColor("#80ffffff"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvImages.setVisibility(View.GONE);
        rvImages.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(gridLayoutManager);

        rvImages.addItemDecoration(new SpacesItemDecoration(mContext, mAdapter, 2, 1, true));
    }

    @OnClick({R.id.textview_post, R.id.imageview_image})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_post: {
                mPresenter.onClickPost(tvContent.getText());
                break;
            }
            case R.id.imageview_image: {
                mPresenter.onClickImage();
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
            case R.id.imageview_schedule: {
                mPresenter.onClickSchedule();
                break;
            }
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new PostPresenter(this);
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

    @Subscribe
    public void onEventPickImageLocal(MessageImageLocal message) {
        if (rvImages.getVisibility() != View.VISIBLE) {
            rvImages.setVisibility(View.VISIBLE);
        }
        int size = mListImage.size();

        mListImage.addAll(message.mListImage);
        mAdapter.notifyItemRangeInserted(size, message.mListImage.size());

        if (!mListImage.isEmpty()) {
            tvPost.setEnabled(true);
            tvPost.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tvPost.setEnabled(false);
            tvPost.setTextColor(Color.parseColor("#80ffffff"));
        }
    }

    @Override
    public void onBackPressed() {
        if (tvContent.getText().length() > 0 || !mListImage.isEmpty()) {
            AlertUtils.showDialogCancelPost(mContext, super::onBackPressed);
            return;
        }

        super.onBackPressed();
    }
}
