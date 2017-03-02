package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.model.Child;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anhtu on 3/2/2017.
 */

public class ChildrenGridViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    CircleImageView imvIcon;

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
                .into(imvIcon);

        tvName.setText(child.getName());
    }
}
