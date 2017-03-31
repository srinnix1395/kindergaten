package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.StringUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/11/2017.
 */

public class PostedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_type_noti)
    TextView tvTitle;

    @BindView(R.id.textview_created_at)
    TextView tvCreatedAt;

    @BindView(R.id.textview_content)
    TextView tvContent;

    @BindView(R.id.imageview_like)
    ImageView imvLike;

    @BindView(R.id.textview_number_like)
    TextView tvNumberLike;

    @BindView(R.id.textview_number_comment)
    TextView tvNumberComment;

    @BindView(R.id.layout_image)
    FrameLayout frameLayoutImage;

    @BindView(R.id.imageview_first_image)
    ImageView imvFirstImage;

    @BindView(R.id.textview_number_image)
    TextView tvNumberImage;

    @BindView(R.id.view_line)
    View viewLine;

    private PostAdapter.PostListener mPostListener;
    private int position;

    public PostedViewHolder(View view, PostAdapter.PostListener postListener, int viewType) {
        super(view);
        ButterKnife.bind(this, itemView);
        mPostListener = postListener;
    }

    public void bindData(Post post, int position) {
        this.position = position;

        switch (post.getType()) {
            case AppConstant.POST_NORMAL: {
                tvTitle.setText("Thông báo thường");
                break;
            }
            case AppConstant.POST_IMPORTANT: {
                tvTitle.setText("Thông báo quan trọng");
                break;
            }
        }
        tvContent.setText(post.getContent());
        tvCreatedAt.setText(StringUtil.getTime(post.getCreatedAt()));

        int size = post.getListImage().size();
        if (size == 0) {
            frameLayoutImage.setVisibility(View.GONE);
            imvFirstImage.setImageDrawable(null);
        } else {
            frameLayoutImage.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext())
                    .load(post.getListImage().get(0))
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.dummy_image)
                    .into(imvFirstImage);
        }

        if (size <= 1) {
            tvNumberImage.setVisibility(View.GONE);
        } else {
            tvNumberImage.setVisibility(View.VISIBLE);
            tvNumberImage.setText(String.format(Locale.getDefault(), "%d ảnh", size));
        }

        bindImageLike(post.isUserLike(), post.getNumberOfLikes());
        bindComment(post.getNumberOfComments());

        if (post.getListImage().isEmpty()) {
            viewLine.setVisibility(View.VISIBLE);
        } else {
            viewLine.setVisibility(View.INVISIBLE);
        }
    }

    public void bindComment(int numberOfComments) {
        tvNumberComment.setText(String.format(Locale.getDefault(),
                "%d %s", numberOfComments, itemView.getContext().getString(R.string.comment1)));
    }

    public void bindImageLike(boolean userLike, int numberLike) {
        imvLike.setImageResource(userLike ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
        tvNumberLike.setText(String.format(Locale.getDefault(),
                "%d %s", numberLike, itemView.getContext().getString(R.string.likes)));
    }

    @OnClick(R.id.imageview_like)
    void onClickLike() {
        if (mPostListener != null) {
            mPostListener.onClickLike(position);
        }
    }

    @OnClick(R.id.textview_number_like)
    void onClickNumberLike() {
        if (mPostListener != null) {
            mPostListener.onClickNumberLike(position);
        }
    }

    @OnClick(R.id.imageview_first_image)
    void onClickImage() {
        if (mPostListener != null) {
            mPostListener.onClickImage(position);
        }
    }

    @OnClick({R.id.imageview_comment, R.id.textview_number_comment})
    void onClickComment(View v) {
        if (mPostListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageview_comment:{
                mPostListener.onClickComment(position, true);
                break;
            }
            case R.id.textview_number_comment:{
                mPostListener.onClickComment(position, false);
                break;
            }
        }

    }
}
