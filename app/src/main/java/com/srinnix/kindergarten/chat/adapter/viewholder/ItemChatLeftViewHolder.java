package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2/9/2017.
 */

public class ItemChatLeftViewHolder extends RecyclerView.ViewHolder {
	@BindView(R.id.imageview_chatleft_icon)
	ImageView imvIcon;

    @BindView(R.id.textview_chatleft_time)
    TextView tvTime;

	@BindView(R.id.textview_itemchatleft_message)
	TextView tvMessage;
	
	public ItemChatLeftViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}
	
	public void bindData(Message message) {
        tvMessage.setText(message.getMessage());
        tvTime.setText(String.valueOf(message.getCreatedAt()));

        switch (message.getLayoutType()) {
			case ChatConstant.FIRST: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_first);
				break;
			}
			case ChatConstant.MIDDLE: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_middle);
				break;
			}
			case ChatConstant.LAST: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_last);
				break;
			}
			case ChatConstant.SINGLE: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_single);
				break;
			}
		}
	}
}
