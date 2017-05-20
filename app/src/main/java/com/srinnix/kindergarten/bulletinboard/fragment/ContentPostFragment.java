package com.srinnix.kindergarten.bulletinboard.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.MediaPostAdapter;
import com.srinnix.kindergarten.bulletinboard.presenter.ContentPostPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.custom.SquareItemLayout;
import com.srinnix.kindergarten.messageeventbus.MessageEnabledNotificationRange;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ContentPostFragment extends BaseFragment {
    @BindView(R.id.edittext_content)
    EditText etContent;

    @BindView(R.id.recyclerview_image)
    RecyclerView rvImages;

    @BindView(R.id.radio_normal)
    RadioButton radioNormal;

    private ArrayList<MediaLocal> mListMedia;
    private MediaPostAdapter mAdapter;
    private ContentPostPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_content;
    }

    @Override
    protected void initData() {
        super.initData();
        mListMedia = new ArrayList<>();
        mAdapter = new MediaPostAdapter(mListMedia, position -> {
            mListMedia.remove(position);
            mAdapter.notifyItemRemoved(position);

            if (!mListMedia.isEmpty()) {
                ((PostFragment) getParentFragment()).setEnabledTvPost(true);
            } else {
                rvImages.setVisibility(View.GONE);

                ((PostFragment) getParentFragment()).setEnabledTvPost(false);
            }
        }, SquareItemLayout.TYPE_HEIGHT);
    }

    @Override
    protected void initChildView() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 || !mListMedia.isEmpty()) {
                    ((PostFragment) getParentFragment()).setEnabledTvPost(true);
                } else {
                    ((PostFragment) getParentFragment()).setEnabledTvPost(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        radioNormal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                EventBus.getDefault().post(new MessageEnabledNotificationRange(false));
            } else {
                EventBus.getDefault().post(new MessageEnabledNotificationRange(true));
            }
        });

        rvImages.setVisibility(View.GONE);
        rvImages.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(gridLayoutManager);

        rvImages.addItemDecoration(new SpacesItemDecoration(mContext, mAdapter, 2, 1, true));
    }

    @OnClick({R.id.imageview_image, R.id.imageview_video,
            R.id.imageview_setting, R.id.imageview_facebook})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_image: {
                UiUtils.hideKeyboard(getActivity());
                mPresenter.onClickImage(mListMedia);
                break;
            }
            case R.id.imageview_video: {
                mPresenter.onClickVideo(mListMedia);
                break;
            }
            case R.id.imageview_facebook: {
                mPresenter.onClickFacebook();
                break;
            }
            case R.id.imageview_setting: {
                ((PostFragment) getParentFragment()).gotoSettingFragment();
                break;
            }
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ContentPostPresenter(this);
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

        int sizeTotal = mListMedia.size();
        if (sizeTotal > 0) {
            mListMedia.clear();
            mAdapter.notifyItemRangeRemoved(0, sizeTotal);
        }
        mListMedia.addAll(message.mListImage);
        mAdapter.notifyItemRangeInserted(0, message.mListImage.size());

        if (!mListMedia.isEmpty()) {
            ((PostFragment) getParentFragment()).setEnabledTvPost(true);
        } else {
            ((PostFragment) getParentFragment()).setEnabledTvPost(false);
        }
    }

    public String getContentPost() {
        return etContent.getText().toString();
    }

    public ArrayList<MediaLocal> getListImage() {
        return mListMedia;
    }

    public int getNotificationType() {
        return radioNormal.isChecked() ? AppConstant.POST_NORMAL : AppConstant.POST_IMPORTANT;
    }
}
