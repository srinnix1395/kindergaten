package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PostFragmentAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.PostDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.PostPresenter;
import com.srinnix.kindergarten.messageeventbus.MessagePostSuccessfully;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/1/2017.
 */

public class PostFragment extends BaseFragment implements PostDelegate {
    @BindView(R.id.viewpager_post)
    ViewPager viewPager;

    @BindView(R.id.toolbar_post)
    Toolbar toolbar;

    @BindView(R.id.textview_post)
    TextView tvPost;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_success)
    ImageView imvSuccess;

    private PostPresenter mPresenter;

    private ContentPostFragment contentPostFragment;
    private SettingPostFragment settingPostFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post;
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Đăng vào bảng tin");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        tvPost.setEnabled(false);
        tvPost.setTextColor(Color.parseColor("#99ffffff"));

        ArrayList<Fragment> arrayList = new ArrayList<>();
        contentPostFragment = new ContentPostFragment();
        arrayList.add(contentPostFragment);
        settingPostFragment = new SettingPostFragment();
        arrayList.add(settingPostFragment);

        PostFragmentAdapter adapter = new PostFragmentAdapter(getChildFragmentManager(), arrayList);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new PostPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.textview_post)
    public void onClickPost() {
        UiUtils.hideKeyboard(getActivity());
        tvPost.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);
        mPresenter.onClickPost(contentPostFragment.getContentPost(),
                contentPostFragment.getListImage(),
                contentPostFragment.getNotificationType(),
                settingPostFragment.getNotificationRange());
    }

    public void setEnabledTvPost(boolean enabled) {
        tvPost.setEnabled(enabled);

        if (enabled) {
            tvPost.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tvPost.setTextColor(Color.parseColor("#99ffffff"));
        }
    }

    public void setVisibility(int resId, int visibility) {
        switch (resId) {
            case R.id.textview_post: {
                tvPost.setVisibility(visibility);
                break;
            }
            case R.id.progressbar_loading: {
                if (visibility == View.VISIBLE) {
                    UiUtils.showProgressBar(pbLoading);
                } else {
                    UiUtils.hideProgressBar(pbLoading);
                }
                break;
            }
            case R.id.imageview_success: {
                imvSuccess.setVisibility(visibility);
                break;
            }
        }
    }

    public void gotoSettingFragment() {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onBackPressed() {
        if (contentPostFragment.getContentPost().length() > 0 ||
                !contentPostFragment.getListImage().isEmpty()) {
            AlertUtils.showDialogCancelPost(mContext, (dialog, which) -> {
                PostFragment.super.onBackPressed();
            });
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
