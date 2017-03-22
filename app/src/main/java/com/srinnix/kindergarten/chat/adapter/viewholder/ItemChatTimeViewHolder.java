package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srinnix.kindergarten.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2/9/2017.
 */

public class ItemChatTimeViewHolder extends RecyclerView.ViewHolder {
	@BindView(R.id.textview_time)
	TextView tvTime;
	
	public ItemChatTimeViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}
	
	public void bindData(String time) {
		tvTime.setText(time);
	}
}
