package com.srinnix.kindergarten.clazz.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.ActionTimetable;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anhtu on 5/4/2017.
 */

public class ActionTimetableViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_action)
    TextView tvAction;

    @BindView(R.id.textview_content)
    TextView tvContent;

    public ActionTimetableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(ActionTimetable data) {
        tvAction.setBackgroundResource(UiUtils.randomBackgroundActionTimeTable());

        tvAction.setText(data.getAction());
        tvContent.setText(data.getContent());
    }
}
