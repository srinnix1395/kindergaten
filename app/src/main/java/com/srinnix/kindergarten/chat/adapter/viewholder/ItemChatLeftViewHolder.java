package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ModelConstant;
import com.srinnix.kindergarten.model.ChatItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2/9/2017.
 */

public class ItemChatLeftViewHolder extends RecyclerView.ViewHolder {
	@BindView(R.id.imageview_chatleft_icon)
	ImageView imvIcon;
	
	@BindView(R.id.textview_itemchatleft_message)
	TextView tvMessage;
	
	public ItemChatLeftViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}
	
	public void bindData(ChatItem chatItem) {
		
		tvMessage.setText(chatItem.getMessage());
		switch (chatItem.getLayoutType()) {
			case ModelConstant.FIRST: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_first);
				break;
			}
			case ModelConstant.MIDDLE: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_middle);
				break;
			}
			case ModelConstant.LAST: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_last);
				break;
			}
			case ModelConstant.SINGLE: {
				tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_single);
				break;
			}
		}
	}
}
