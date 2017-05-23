package com.srinnix.kindergarten.bulletinboard.fragment;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.BulletinBoardDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.BulletinBoardPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.EndlessScrollDownListener;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.messageeventbus.MessageLogout;
import com.srinnix.kindergarten.messageeventbus.MessageNumberComment;
import com.srinnix.kindergarten.messageeventbus.MessagePostSuccessfully;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.BulletinResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2/3/2017.
 */

public class BulletinBoardFragment extends BaseFragment implements BulletinBoardDelegate {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerview_schoolboard)
    RecyclerView rvListPost;

    @BindView(R.id.floatbutton_post)
    FloatingActionButton fabPost;

    private BulletinBoardPresenter mPresenter;
    private PostAdapter mPostAdapter;
    private ArrayList<Object> mListPost;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bulletin_board;
    }

    @Override
    protected void initData() {
        super.initData();
        mListPost = new ArrayList<>();
        mListPost.add(new LoadingItem());

        mPostAdapter = new PostAdapter(mListPost,
                () -> mPresenter.onLoadMore(rvListPost, mListPost, mPostAdapter),
                new PostAdapter.PostListener() {
                    @Override
                    public void onClickLike(int position) {
                        mPresenter.onClickLike(mListPost, (Post) mListPost.get(position));
                    }

                    @Override
                    public void onClickNumberLike(int position) {
                        mPresenter.onClickNumberLike(getChildFragmentManager(), ((Post) mListPost.get(position)));
                    }

                    @Override
                    public void onClickImage(int position) {
                        mPresenter.onClickImages((Post) mListPost.get(position));
                    }

                    @Override
                    public void onClickComment(int position, boolean isShowKeyboard) {
                        mPresenter.onClickComment(((Post) mListPost.get(position)));
                    }

                    @Override
                    public void onClickShare(int position) {
                        mPresenter.onClickShare(((Post) mListPost.get(position)));
                    }
                });
    }

    @Override
    protected void initChildView() {
        rvListPost.setAdapter(mPostAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListPost.setLayoutManager(linearLayoutManager);
        rvListPost.addOnScrollListener(new EndlessScrollDownListener(linearLayoutManager) {
            Handler handlerFab = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fabPost.animate()
                            .alpha(1f)
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .setDuration(200)
                            .start();
                }
            };

            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
                int size = mListPost.size();
                if (mListPost.get(size - 1) instanceof Post) {
                    return;
                }
                mPresenter.onLoadMore(rvListPost, mListPost, mPostAdapter);
            }

            @Override
            public void onStateChanged(int newState) {
                super.onStateChanged(newState);
                if (fabPost.getVisibility() != View.VISIBLE) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    handlerFab.removeCallbacks(runnable);
                    fabPost.animate()
                            .translationY(fabPost.getHeight() / 2)
                            .alpha(0f)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .setDuration(200)
                            .start();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    handlerFab.postDelayed(runnable, 1000);
                }
            }
        });

        refreshLayout.setOnRefreshListener(() ->{
            if (!mListPost.isEmpty() && mListPost.get(0) instanceof LoadingItem) {
                mPresenter.onLoadMore(rvListPost, mListPost, mPostAdapter);
            } else {
                mPresenter.refresh(refreshLayout, mListPost);
            }
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new BulletinBoardPresenter(this);
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

    @OnClick(R.id.floatbutton_post)
    void onClickPost() {
        mPresenter.onClickAddPost();
    }

    @Subscribe
    public void onEventLoginSuccessfully(MessageLoginSuccessfully message) {
        if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_TEACHERS) {
            UiUtils.showView(fabPost);
        }
        mPresenter.getPostAfterLogin(refreshLayout, mListPost);
    }

    @Subscribe
    public void onEventNumberComment(MessageNumberComment message) {
        mPresenter.updateNumberComment(message, mListPost);
    }

    @Subscribe
    public void onEventPostSuccessfully(MessagePostSuccessfully message) {
        mListPost.add(0, message.post);
        mPostAdapter.notifyItemInserted(0);

        rvListPost.scrollToPosition(0);
    }

    @Subscribe
    public void onEventLogout(MessageLogout message) {
        UiUtils.hideView(fabPost);
        mPresenter.logout(mListPost);
    }

    @Override
    public void updateSchoolBoard(ArrayList<Post> data, boolean isLoadFirst) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

        int sizeNewData = data.size();
        int sizeTotal = mListPost.size();

        if (sizeNewData < AppConstant.ITEM_POST_PER_PAGE) {
            if (!mListPost.isEmpty() && mListPost.get(sizeTotal - 1) instanceof LoadingItem) {
                mListPost.remove(sizeTotal - 1);
                mPostAdapter.notifyItemRemoved(sizeTotal - 1);
            }
            if (sizeNewData > 0) {
                sizeTotal = mListPost.size();

                mListPost.addAll(data);
                mPostAdapter.notifyItemRangeInserted(sizeTotal, data.size());
            }
        } else {
            mListPost.addAll(sizeTotal - 1, data);
            mPostAdapter.notifyItemRangeInserted(sizeTotal - 1, data.size());
        }

        if (SharedPreUtils.getInstance(mContext).isUserSignedIn() &&
                SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_TEACHERS) {
            UiUtils.showView(fabPost);
        }

        if (isLoadFirst && !mListPost.isEmpty()) {
            rvListPost.scrollToPosition(0);
        }
    }

    @Override
    public void setErrorItemLoading() {
        int size = mListPost.size();
        ((LoadingItem) mListPost.get(size - 1)).setLoadingState(LoadingItem.STATE_ERROR);
        mPostAdapter.notifyItemChanged(size - 1);

        AlertUtils.showToast(mContext, R.string.error_post);
    }

    @Override
    public void handleLikePost(Integer position, boolean like, int numberOfLikes) {
        if (like) {
            AlertUtils.showToastSuccess(mContext, R.drawable.ic_heart_white, R.string.liked);
        } else {
            AlertUtils.showToastSuccess(mContext, R.drawable.ic_heart_broken_white, R.string.unliked);
        }
        ArrayList<Object> payloads = new ArrayList<>();
        payloads.add(like);
        payloads.add(numberOfLikes);
        mPostAdapter.notifyItemChanged(position, payloads);
    }

    @Override
    public void onRefreshResult(boolean result, BulletinResponse data) {
        if (!result) {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            return;
        }

        if (!data.getListPost().isEmpty()) {
            ArrayList<Post> listPost = data.getListPost();

            int i = 0;
            for (Post post : listPost) {
                if (i == data.getListLikes().size()) {
                    break;
                }

                if (data.getListLikes().contains(post.getId())) {
                    post.setUserLike(true);
                    i++;
                }
            }

            mListPost.addAll(0, listPost);
            mPostAdapter.notifyItemRangeInserted(0, listPost.size());

            rvListPost.scrollToPosition(0);
        }

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateNumberComment(int position, int numberOfComments) {
        mPostAdapter.notifyItemChanged(position, numberOfComments);
    }

    @Override
    public void updateLogout() {
        mPostAdapter.notifyItemRangeChanged(0, mListPost.size() - 1, false);
    }

    @Override
    public void deletePost(int i) {
        mPostAdapter.notifyItemRemoved(i);
    }

    @Override
    public void onGetImportantPost(boolean result, BulletinResponse data) {
        if (!result) {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            return;
        }

        if (!data.getListPost().isEmpty()) {
            ArrayList<Post> listPost = data.getListPost();

            int i = 0;
            for (Post post : listPost) {
                if (i == data.getListLikes().size()) {
                    break;
                }

                if (data.getListLikes().contains(post.getId())) {
                    post.setUserLike(true);
                    i++;
                }
            }

            for (int j = 0, size = mListPost.size(); j < size; j++) {
                if (listPost.isEmpty()) {
                    break;
                }

                if (mListPost.get(j) instanceof Post && listPost.get(0).getCreatedAt() >= ((Post) mListPost.get(j)).getCreatedAt()) {
                    mListPost.add(j, listPost.get(0));
                    mPostAdapter.notifyItemInserted(j);
                    size++;
                    listPost.remove(0);
                }
            }

            rvListPost.scrollToPosition(0);
        }

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
