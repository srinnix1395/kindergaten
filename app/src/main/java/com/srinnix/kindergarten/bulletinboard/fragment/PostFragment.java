package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImagePostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.PostDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.PostPresenter;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.messageeventbus.MessagePostSuccessfully;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostFragment extends BaseFragment implements PostDelegate {
    @BindView(R.id.toolbar_post)
    Toolbar toolbar;

    @BindView(R.id.textview_post)
    TextView tvPost;

    @BindView(R.id.edittext_content)
    EditText etContent;

    @BindView(R.id.recycler_view_image)
    RecyclerView rvImages;

    @BindView(R.id.radio_normal)
    RadioButton radioNormal;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_success)
    ImageView imvSuccess;

    private ArrayList<ImageLocal> mListImage;
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
                tvPost.setEnabled(true);
                tvPost.setTextColor(Color.parseColor("#ffffff"));
            } else {
                rvImages.setVisibility(View.GONE);

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

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 || !mListImage.isEmpty()) {
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
                tvPost.setVisibility(View.GONE);
                UiUtils.showProgressBar(pbLoading);
                mPresenter.onClickPost(etContent.getText().toString(), mListImage, radioNormal.isChecked());
                break;
            }
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
            tvPost.setTextColor(Color.parseColor("#80ffffff"));
        }
    }

    @Override
    public void onBackPressed() {
        if (etContent.getText().length() > 0 || !mListImage.isEmpty()) {
            AlertUtils.showDialogCancelPost(mContext, super::onBackPressed);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onFail() {
        UiUtils.hideProgressBar(pbLoading);
        tvPost.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Post data) {
        UiUtils.hideProgressBar(pbLoading);
        imvSuccess.setVisibility(View.VISIBLE);

        EventBus.getDefault().post(new MessagePostSuccessfully(data));
        super.onBackPressed();
    }
}
