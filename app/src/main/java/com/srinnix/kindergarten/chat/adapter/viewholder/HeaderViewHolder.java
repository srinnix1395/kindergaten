package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 4/20/2017.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_header)
    TextView tvHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(String header) {
        tvHeader.setText(header);
    }
}
