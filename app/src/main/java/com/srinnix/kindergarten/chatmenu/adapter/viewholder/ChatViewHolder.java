package com.srinnix.kindergarten.chatmenu.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chatmenu.adapter.ChatListAdapter;
import com.srinnix.kindergarten.model.ChatMember;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2/6/2017.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
	@BindView(R.id.text_view_name)
	TextView tvName;
	
	@BindView(R.id.image_view_status)
	ImageView imvStatus;
	
	private int position;
	
	public ChatViewHolder(View itemView, ChatListAdapter.OnClickItemChatListener onClickItemChatListener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		itemView.setOnClickListener(view -> {
			if (onClickItemChatListener != null) {
				onClickItemChatListener.onClick(position);
			}
		});
	}
	
	public void bindData(ChatMember chatMember, int position) {
		this.position = position;
		
		tvName.setText(chatMember.getName());
		if (position == ChatMember.ONLINE) {
			Glide.with(itemView.getContext())
					.load(R.drawable.ic_state_online)
					.into(imvStatus);
		} else {
			Glide.with(itemView.getContext())
					.load(R.drawable.ic_state_offline)
					.into(imvStatus);
		}
	}
}
