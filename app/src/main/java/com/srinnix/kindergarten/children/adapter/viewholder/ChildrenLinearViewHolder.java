package com.srinnix.kindergarten.children.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.Child;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ChildrenLinearViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    CircleImageView imvIcon;

    @BindView(R.id.textview_child_name)
    TextView tvName;

    @BindView(R.id.textview_child_age)
    TextView tvAge;

    public ChildrenLinearViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> onClickItem());
    }

    public void bindData(Child child) {
        Glide.with(itemView.getContext())
                .load(child.getImage())
                .into(imvIcon);

        tvName.setText(child.getName());
    }

    private void onClickItem() {

    }
}
