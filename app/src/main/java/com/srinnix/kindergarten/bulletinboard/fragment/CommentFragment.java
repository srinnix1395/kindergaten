package com.srinnix.kindergarten.bulletinboard.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.CommentAdapter;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.CommentViewHolder;
import com.srinnix.kindergarten.bulletinboard.delegate.CommentDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.CommentPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageNumberComment;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.LoadingItem3State;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentFragment extends BaseFragment implements CommentDelegate {
    @BindView(R.id.toolbar_detail_commentt)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview_comment)
    RecyclerView mRvComment;

    @BindView(R.id.edittext_comment)
    EditText mEtComment;

    @BindView(R.id.imageview_send)
    ImageView mImvSend;

    @BindView(R.id.layout_retry)
    RelativeLayout mRelRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar mPbLoading;

    @BindView(R.id.textview_retry)
    TextView mTvRetry;

    private ArrayList<Object> mListComment;
    private CommentAdapter mAdapter;
    private CommentPresenter mPresenter;

    private int numberComment =0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_comment;
    }

    @Override
    protected void initChildView() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setTitle(mContext.getString(R.string.comment));
        mToolbar.setTitleTextColor(Color.WHITE);

        mPbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        mRvComment.setLayoutManager(new LinearLayoutManager(mContext));

        mListComment = new ArrayList<>();
        mAdapter = new CommentAdapter(mListComment, () -> {
            if (!mListComment.isEmpty() && (mListComment.get(0) instanceof LoadingItem3State)) {
                ((LoadingItem3State) mListComment.get(0)).setLoadingState(LoadingItem3State.STATE_IDLE);
                mAdapter.notifyItemChanged(0);
            }
            mPresenter.getComment(((Comment) mListComment.get(1)).getCreatedAt());
        }, new CommentViewHolder.CommentListener() {
            @Override
            public void onClickRetry(int position) {
                if (!((Comment) mListComment.get(position)).isSuccess()) {
                    mPresenter.onResendComment((Comment) mListComment.get(position), position);
                }
            }

            @Override
            public void onLongClick(int position) {
                mPresenter.onLongClickComment((Comment) mListComment.get(position));
            }
        });
        mRvComment.setAdapter(mAdapter);

        mImvSend.setEnabled(false);
    }

    @OnTextChanged(R.id.edittext_comment)
    public void onTextChanged(CharSequence s) {
        if (s.toString().isEmpty()) {
            mImvSend.setImageLevel(1);
            mImvSend.setEnabled(false);
        } else {
            mImvSend.setImageLevel(2);
            mImvSend.setEnabled(true);
        }
    }

    @OnClick(R.id.imageview_send)
    public void onClickSend() {
        mPresenter.onClickSend(mEtComment);
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        mRelRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(mPbLoading);
        mPresenter.getComment(System.currentTimeMillis());
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new CommentPresenter(this);
        return mPresenter;
    }

    @Override
    public void onLoadCommentSuccess(ArrayList<Comment> commentArrayList, boolean isLoadFirst) {
        if (commentArrayList.size() < AppConstant.ITEM_COMMENT_PER_PAGE) {
            if (!mListComment.isEmpty() && mListComment.get(0) instanceof LoadingItem3State) {
                mListComment.remove(0);
                mAdapter.notifyItemRemoved(0);
            }
            if (!commentArrayList.isEmpty()) {
                mListComment.addAll(commentArrayList);
                mAdapter.notifyItemRangeInserted(0, commentArrayList.size());
            }
        } else {
            if (mListComment.isEmpty()) {
                mListComment.add(0, new LoadingItem3State());
                mAdapter.notifyItemInserted(0);
            } else if ((mListComment.get(0) instanceof LoadingItem3State)) {
                ((LoadingItem3State) mListComment.get(0)).setLoadingState(LoadingItem3State.STATE_IDLE);
                mAdapter.notifyItemChanged(0);
            }
            mListComment.addAll(1, commentArrayList);
            mAdapter.notifyItemRangeInserted(1, commentArrayList.size());
        }

        if (isLoadFirst) {
            if (!mListComment.isEmpty()) {
                mRvComment.smoothScrollToPosition(commentArrayList.size() - 1);
            }
            mRvComment.setVisibility(View.VISIBLE);
            UiUtils.hideProgressBar(mPbLoading);
        }
    }

    @Override
    public void onLoadCommentFail(int resError, boolean isLoadFirst) {
        if (isLoadFirst) {
            UiUtils.hideProgressBar(mPbLoading);

            mTvRetry.setText(resError);
            mRelRetry.setVisibility(View.VISIBLE);
        } else if (!mListComment.isEmpty() && (mListComment.get(0) instanceof LoadingItem3State)) {
            ((LoadingItem3State) mListComment.get(0)).setLoadingState(LoadingItem3State.STATE_IDLE);
            mAdapter.notifyItemChanged(0);
        }
    }

    @Override
    public void insertComment(Comment comment) {
        mListComment.add(comment);
        mAdapter.notifyItemInserted(mListComment.size() - 1);
        mRvComment.smoothScrollToPosition(mListComment.size() - 1);
    }

    @Override
    public void updateIdComment(long oldId, Comment comment) {
        for (int i = mListComment.size() - 1; i >= 0; i--) {
            if (mListComment.get(i) instanceof Comment) {
                if (((Comment) mListComment.get(i)).getId().equals(String.valueOf(oldId))) {
                    ((Comment) mListComment.get(i)).setId(comment.getId());
                    ((Comment) mListComment.get(i)).setCreatedAt(comment.getCreatedAt());
                    mAdapter.notifyItemChanged(i, comment.getCreatedAt());
                    numberComment++;
                    break;
                }
            }
        }
    }

    @Override
    public void updateStateComment(long id) {
        for (int i = mListComment.size() - 1; i >= 1; i--) {
            if (mListComment.get(i) instanceof Comment) {
                if (((Comment) mListComment.get(i)).getId().equals(String.valueOf(id))) {
                    ((Comment) mListComment.get(i)).setSuccess(false);
                    mAdapter.notifyItemChanged(i, false);
                    break;
                }
            }
        }
    }

    @Override
    public void updateStateComment(int position, boolean state) {
        ((Comment) mListComment.get(position)).setSuccess(state);
        mAdapter.notifyItemChanged(position, state);
    }

    @Override
    public void onBackPressed() {
        if (numberComment != 0) {
            EventBus.getDefault().post(new MessageNumberComment(mPresenter.getIdPost(),numberComment));
        }
        super.onBackPressed();
    }
}
