package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.Class;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.imageview_icon)
    CircleImageView imvIcon;

    @BindView(R.id.textview_class_name)
    TextView tvClassName;

    public ClassViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Class aClass) {
        tvClassName.setText(aClass.getName());

    }
}
