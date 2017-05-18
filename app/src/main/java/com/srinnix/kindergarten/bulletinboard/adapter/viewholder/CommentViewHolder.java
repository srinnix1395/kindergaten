package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_comment)
    TextView tvComment;

    @BindView(R.id.textview_time)
    TextView tvTime;

    @BindView(R.id.textview_fail)
    TextView tvFail;

    private int position;

    public CommentViewHolder(View view, CommentViewHolder.CommentListener commentListener) {
        super(view);
        ButterKnife.bind(this, view);
        itemView.setOnLongClickListener(v -> {
            commentListener.onLongClick(position);
            return false;
        });
        itemView.setOnClickListener(v -> commentListener.onClickRetry(position));
    }

    public void bindData(Comment comment, int position) {
        this.position = position;
        if (comment.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            Glide.with(itemView.getContext())
                    .load(comment.getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_parent)
                    .into(imvIcon);
        } else {
            Glide.with(itemView.getContext())
                    .load(comment.getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon);
        }

        tvComment.setText(StringUtil.getComment(comment));

        bindStatus(comment.isSuccess());
        bindTime(comment.getCreatedAt());
    }

    public void bindTime(long createdAt) {
        tvTime.setText(StringUtil.getTimeAgoComment(itemView.getContext(), createdAt));
    }

    public void bindStatus(boolean success) {
        if (success) {
            tvTime.setVisibility(View.VISIBLE);
            tvFail.setVisibility(View.GONE);
        } else {
            tvTime.setVisibility(View.GONE);
            tvFail.setVisibility(View.VISIBLE);
        }
    }

    public interface CommentListener {
        void onClickRetry(int position);

        void onLongClick(int position);
    }
}
