package com.srinnix.kindergarten.schoolboard.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.custom.EndlessScrollListener;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.schoolboard.presenter.SchoolBoardPresenter;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardFragment extends BaseFragment implements SchoolBoardDelegate {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerview_schoolboard)
    RecyclerView rvListPost;

    private SchoolBoardPresenter mPresenter;
    private PostAdapter postAdapter;
    private ArrayList<Object> arrPost;

    public static SchoolBoardFragment newInstance() {
        return new SchoolBoardFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_school_board;
    }

    @Override
    protected void initChildView() {
        arrPost = new ArrayList<>();
        arrPost.add(new LoadingItem());

        postAdapter = new PostAdapter(mContext, arrPost,
                () -> mPresenter.onLoadMore(rvListPost, arrPost, postAdapter),
                new PostAdapter.LikeListener() {
                    @Override
                    public void onClickLike(String idPost, boolean isLike) {
                        mPresenter.onClickLike(arrPost, idPost, isLike);
                    }

                    @Override
                    public void onClickNumberLike(String id) {
                        mPresenter.onClickNumberLike(id);
                    }
                });
        rvListPost.setAdapter(postAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListPost.setLayoutManager(linearLayoutManager);
        rvListPost.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
//                mPresenter.onLoadMore(rvListPost, arrPost, postAdapter);
            }
        });

        refreshLayout.setOnRefreshListener(() -> mPresenter.refresh());
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SchoolBoardPresenter(this);
        return mPresenter;
    }

    @Override
    public void updateSchoolBoard(ArrayList<Post> arrayList) {
        int size = arrPost.size();
        if (size == 0) {
            //// TODO: 3/13/2017 khi không còn tin nào
        } else {
            arrPost.addAll(size - 1, arrayList);
            postAdapter.notifyItemRangeInserted(size - 1, arrayList.size());
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
    public void handleLikePost(Integer position) {
        postAdapter.notifyItemChanged(position);
    }
}
