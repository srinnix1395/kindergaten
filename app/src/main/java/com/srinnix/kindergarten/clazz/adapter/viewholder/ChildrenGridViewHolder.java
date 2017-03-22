package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 3/2/2017.
 */

public class ChildrenGridViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    private String id;

    public ChildrenGridViewHolder(View view, ChildrenAdapter.OnClickChildListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(id);
            }
        });
    }

    public void bindData(Child child) {
        id = child.getId();
        Glide.with(itemView.getContext())
                .load(child.getImage())
                .thumbnail(0.1f)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.image_children)
                .into(imvIcon);

        tvName.setText(StringUtil.getNameChildren(child.getName()));
    }
}
