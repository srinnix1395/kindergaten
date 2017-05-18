package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.HeaderTimetable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 5/5/2017.
 */

public class HeaderTimetableViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_subject)
    TextView tvSubject;

    @BindView(R.id.textview_content)
    TextView tvContent;

    public HeaderTimetableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(HeaderTimetable data, int position) {
        if (data == null) {
            tvSubject.setVisibility(View.GONE);
            tvContent.setVisibility(View.GONE);
            return;
        }

        if (data.getSubject() == null) {
            tvSubject.setVisibility(View.GONE);
        } else {
            tvSubject.setVisibility(View.VISIBLE);
            tvSubject.setText(data.getSubject());
        }

        if (data.getContent() == null) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(data.getContent());
        }

        if (data.isColor()) {
            itemView.setBackgroundColor(Color.parseColor("#1A000000"));
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
