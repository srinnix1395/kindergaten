package com.srinnix.kindergarten.bulletinboard.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.BulletinBoardDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.BulletinBoardPresenter;
import com.srinnix.kindergarten.custom.EndlessScrollListener;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/3/2017.
 */

public class BulletinBoardFragment extends BaseFragment implements BulletinBoardDelegate {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerview_schoolboard)
    RecyclerView rvListPost;

    private BulletinBoardPresenter mPresenter;
    private PostAdapter postAdapter;
    private ArrayList<Object> arrPost;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bulletin_board;
    }

    @Override
    protected void initChildView() {
        arrPost = new ArrayList<>();
        arrPost.add(new LoadingItem());

        postAdapter = new PostAdapter(mContext, arrPost,
                () -> mPresenter.onLoadMore(rvListPost, arrPost, postAdapter),
                new PostAdapter.PostListener() {
                    @Override
                    public void onClickLike(int position) {
                        mPresenter.onClickLike(arrPost, (Post) arrPost.get(position));
                    }

                    @Override
                    public void onClickNumberLike(int position) {
                        mPresenter.onClickNumberLike(((Post) arrPost.get(position)).getId());
                    }

                    @Override
                    public void onClickImage(int position) {
                        mPresenter.onClickImages((Post) arrPost.get(position));
                    }

                    @Override
                    public void onClickComment(int position, boolean isShowKeyboard) {
                        mPresenter.onClickComment(((Post) arrPost.get(position)), isShowKeyboard);
                    }
                });
        rvListPost.setAdapter(postAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListPost.setLayoutManager(linearLayoutManager);
        rvListPost.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
                mPresenter.onLoadMore(rvListPost, arrPost, postAdapter);
            }
        });

        refreshLayout.setOnRefreshListener(() -> mPresenter.refresh());
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new BulletinBoardPresenter(this);
        return mPresenter;
    }

    @Override
    public void updateSchoolBoard(ArrayList<Post> arrayList, boolean isLoadFirst) {
        int size = arrPost.size();
        if (size == 0) {
            //// TODO: 3/13/2017 khi không còn tin nào
        } else {
            arrPost.addAll(size - 1, arrayList);
            postAdapter.notifyItemRangeInserted(size - 1, arrayList.size());
        }

        if (isLoadFirst) {
            rvListPost.scrollToPosition(0);
        }
    }

    @Override
    public void setErrorItemLoading() {
        int size = arrPost.size();
        ((LoadingItem) arrPost.get(size - 1)).setLoadingState(LoadingItem.STATE_ERROR);
        postAdapter.notifyItemChanged(size - 1);

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
        postAdapter.notifyItemChanged(position, payloads);
    }
}
