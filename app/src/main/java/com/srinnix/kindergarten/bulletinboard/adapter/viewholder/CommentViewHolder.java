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

    public CommentViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(Comment comment) {
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
        tvTime.setText(StringUtil.getTime(comment.getCreatedAt()));
    }
}
