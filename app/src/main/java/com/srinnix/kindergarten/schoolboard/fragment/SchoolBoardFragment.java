package com.srinnix.kindergarten.schoolboard.fragment;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListPost.setLayoutManager(linearLayoutManager);
        rvListPost.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
                mPresenter.onLoadMore(rvListPost, arrPost, postAdapter);
            }
        });

        arrPost = new ArrayList<>();
        arrPost.add(new LoadingItem());

        postAdapter = new PostAdapter(mContext, arrPost,
                () -> mPresenter.onLoadMore(rvListPost, arrPost, postAdapter),
                (idPost, isLike) -> mPresenter.onClickLike(arrPost, idPost, isLike));
        rvListPost.setAdapter(postAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SchoolBoardPresenter(this);
        return mPresenter;
    }

    @Override
    public void updateSchoolBoard(ArrayList<Post> arrayList) {
        int size = arrPost.size();
        arrPost.addAll(size - 1, arrayList);
        postAdapter.notifyItemRangeInserted(size - 1, arrayList.size());
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
