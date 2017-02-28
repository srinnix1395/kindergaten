package com.srinnix.kindergarten.schoolboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Post;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public PostedViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Post post) {
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
        tvNumberLike.setText(String.format(Locale.getDefault(),
                "%d %s", post.getNumberOfLikes(), itemView.getContext().getString(R.string.likes)));


    }
}
