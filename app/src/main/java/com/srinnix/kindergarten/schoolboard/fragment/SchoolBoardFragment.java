package com.srinnix.kindergarten.schoolboard.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.schoolboard.adapter.EndlessScrollListener;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.schoolboard.presenter.SchoolBoardPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardFragment extends BaseFragment implements SchoolBoardDelegate{
	@BindView(R.id.recyclerview_schoolboard)
	RecyclerView rvListPost;
	
	private SchoolBoardPresenter mPresenter;
    private PostAdapter postAdapter;
    private ArrayList<Object> arrPost;
    private CompositeDisposable mDisposable;

    public static SchoolBoardFragment newInstance() {
        return new SchoolBoardFragment();
    }

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_school_board;
	}
	
	@Override
	protected void initChildView() {
        mDisposable = new CompositeDisposable();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListPost.setLayoutManager(linearLayoutManager);
		rvListPost.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int totalItemCount) {
                mPresenter.onLoadMore(mDisposable, arrPost, totalItemCount);
            }
        });

        arrPost = new ArrayList<>();
        arrPost.add(new LoadingItem());

        postAdapter = new PostAdapter(mContext, arrPost);
        rvListPost.setAdapter(postAdapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SchoolBoardPresenter(this);
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && mDisposable.size() == 0) {
            mDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public void updateSchoolBoard(ArrayList<Post> arrayList) {
        int size = arrPost.size();
        arrPost.addAll(arrayList);
        postAdapter.notifyItemRangeInserted(size, arrayList.size());
    }
}
