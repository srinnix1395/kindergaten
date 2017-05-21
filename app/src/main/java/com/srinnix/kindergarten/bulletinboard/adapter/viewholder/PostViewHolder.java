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
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/11/2017.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
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

    @BindView(R.id.imageview_video)
    ImageView imvVideo;

    private PostAdapter.PostListener mPostListener;

    public PostViewHolder(View view, PostAdapter.PostListener postListener) {
        super(view);
        ButterKnife.bind(this, itemView);
        mPostListener = postListener;
    }

    public void bindData(Post post) {
        switch (post.getType()) {
            case AppConstant.POST_NORMAL: {
                tvTitle.setText(R.string.normal_post);
                break;
            }
            case AppConstant.POST_IMPORTANT: {
                tvTitle.setText(R.string.important_post);
                break;
            }
        }
        tvContent.setText(post.getContent());
        tvCreatedAt.setText(StringUtil.getTimeAgo(itemView.getContext(), post.getCreatedAt()));

        if (post.getListImage() == null) {
            UiUtils.showView(viewLine);

            UiUtils.hideView(frameLayoutImage);
            imvFirstImage.setImageDrawable(null);
            UiUtils.hideView(imvVideo);
        } else {
            Image media = post.getListImage().get(0);

            UiUtils.showView(frameLayoutImage);
            if (media.isVideo()) {
                UiUtils.showView(imvVideo);
                Glide.with(itemView.getContext())
                        .load(media.getThumbnailUrl())
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.dummy_image)
                        .into(imvFirstImage);
            } else {
                UiUtils.hideView(imvVideo);
                Glide.with(itemView.getContext())
                        .load(media.getUrl())
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.dummy_image)
                        .into(imvFirstImage);
            }

            if (post.getListImage().size() == 1) {
                UiUtils.hideView(tvNumberImage);
            } else {
                UiUtils.showView(tvNumberImage);
                tvNumberImage.setText(String.format(Locale.getDefault(), "%d ảnh", post.getListImage().size()));
            }

            viewLine.setVisibility(View.INVISIBLE);
        }

        bindImageLike(post.isUserLike(), post.getNumberOfLikes());
        bindComment(post.getNumberOfComments());
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

    public void bindImageLike(boolean userLike) {
        imvLike.setImageResource(userLike ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
    }

    @OnClick(R.id.imageview_share)
    void onClickShare() {
        if (mPostListener != null) {
            mPostListener.onClickShare(getAdapterPosition());
        }
    }

    @OnClick(R.id.imageview_like)
    void onClickLike() {
        if (mPostListener != null) {
            mPostListener.onClickLike(getAdapterPosition());
        }
    }

    @OnClick(R.id.textview_number_like)
    void onClickNumberLike() {
        if (mPostListener != null) {
            mPostListener.onClickNumberLike(getAdapterPosition());
        }
    }

    @OnClick(R.id.imageview_first_image)
    void onClickImage() {
        if (mPostListener != null) {
            mPostListener.onClickImage(getAdapterPosition());
        }
    }

    @OnClick({R.id.imageview_comment, R.id.textview_number_comment})
    void onClickComment(View v) {
        if (mPostListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageview_comment: {
                mPostListener.onClickComment(getAdapterPosition(), true);
                break;
            }
            case R.id.textview_number_comment: {
                mPostListener.onClickComment(getAdapterPosition(), false);
                break;
            }
        }

    }
}
