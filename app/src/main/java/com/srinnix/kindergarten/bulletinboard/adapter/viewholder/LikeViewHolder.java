package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 4/4/2017.
 */

public class LikeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_time)
    TextView tvTime;

    public LikeViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(LikeModel likeModel) {
        if (likeModel.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            Glide.with(itemView.getContext())
                    .load(likeModel.getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_parent)
                    .into(imvIcon);
        } else {
            Glide.with(itemView.getContext())
                    .load(likeModel.getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon);
        }

        tvName.setText(likeModel.getName());
        tvTime.setText(StringUtil.getTimeAgo(itemView.getContext(), likeModel.getCreatedAt()));
    }
}
