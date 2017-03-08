package com.srinnix.kindergarten.children.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.TimeLineChildren;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 3/8/2017.
 */

public class InfoChildrenViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.imageview_mark)
    ImageView imvMark;

    @BindView(R.id.textview_time)
    TextView tvTime;

    public InfoChildrenViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(TimeLineChildren timeLineChildren) {

    }
}
