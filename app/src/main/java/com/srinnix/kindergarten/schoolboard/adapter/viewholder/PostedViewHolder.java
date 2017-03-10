package com.srinnix.kindergarten.schoolboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;

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

    @BindView(R.id.layout_image)
    LinearLayout layoutImage;

    ImageView[] imageViews;
    TextView tvMoreImage;

    private PostAdapter.LikeListener mLikeListener;
    private boolean userLike;
    private String id;

    public PostedViewHolder(View view, PostAdapter.LikeListener likeListener, int viewType) {
        super(view);
        ButterKnife.bind(this, itemView);
        mLikeListener = likeListener;
        inflateLayout(viewType);
    }

    private void inflateLayout(int viewType) {
        if (viewType == PostAdapter.VIEW_TYPE_POSTED_0) {
            layoutImage.setVisibility(View.INVISIBLE);
            return;
        }

        imageViews = new ImageView[5];
        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        View view = null;
        switch (viewType) {
            case PostAdapter.VIEW_TYPE_POSTED_1: {
                view = inflater.inflate(R.layout.layout_1_images, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_2_1: {
                view = inflater.inflate(R.layout.layout_2_images_1, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_2_2: {
                view = inflater.inflate(R.layout.layout_2_images_2, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_3_1: {
                view = inflater.inflate(R.layout.layout_3_images_1, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_3_2: {
                view = inflater.inflate(R.layout.layout_3_images_2, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_4_1: {
                view = inflater.inflate(R.layout.layout_4_images_1, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_4_2: {
                view = inflater.inflate(R.layout.layout_4_images_2, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_4_3: {
                view = inflater.inflate(R.layout.layout_4_images_3, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_5_1: {
                view = inflater.inflate(R.layout.layout_5_images_1, (ViewGroup) itemView, false);
                break;
            }
            case PostAdapter.VIEW_TYPE_POSTED_5_2: {
                view = inflater.inflate(R.layout.layout_5_images_2, (ViewGroup) itemView, false);
                break;
            }
        }
        layoutImage.addView(view);

        if (view != null) {
            imageViews[0] = (ImageView) view.findViewById(R.id.imageview_1);

            if (viewType > PostAdapter.VIEW_TYPE_POSTED_1) {
                imageViews[1] = (ImageView) view.findViewById(R.id.imageview_2);
            }

            if (viewType > PostAdapter.VIEW_TYPE_POSTED_2_2) {
                imageViews[2] = (ImageView) view.findViewById(R.id.imageview_3);
            }

            if (viewType > PostAdapter.VIEW_TYPE_POSTED_3_2) {
                imageViews[3] = (ImageView) view.findViewById(R.id.imageview_4);
            }

            if (viewType > PostAdapter.VIEW_TYPE_POSTED_4_3) {
                imageViews[4] = (ImageView) view.findViewById(R.id.imageview_5);
                tvMoreImage = (TextView) view.findViewById(R.id.textview_more_image);
            }
        }
    }

    public void bindData(Post post) {
        userLike = post.isUserLike();
        id = post.getId();

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

        int size = post.getListImage().size();
        for (int i = 0; i < size; i++) {
            Glide.with(itemView.getContext())
                    .load(post.getListImage().get(i))
                    .into(imageViews[i]);
            if (i == 4) {
                break;
            }
        }
        if (size <= 5) {
            tvMoreImage.setVisibility(View.INVISIBLE);
            tvMoreImage.setText("");
        } else {
            tvMoreImage.setVisibility(View.VISIBLE);
            tvMoreImage.setText(String.format(Locale.getDefault(), "+%d", size - 5));
        }

        tvNumberLike.setText(String.format(Locale.getDefault(),
                "%d %s", post.getNumberOfLikes(), itemView.getContext().getString(R.string.likes)));

        imvLike.setImageResource(userLike ? R.drawable.ic_heart_fill : R.drawable.ic_heart_outline);
    }

    @OnClick(R.id.imageview_like)
    void onClickLike() {
        if (mLikeListener != null) {
            mLikeListener.onClickLike(id, !userLike);
        }
    }
}
