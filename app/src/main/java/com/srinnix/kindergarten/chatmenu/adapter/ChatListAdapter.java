package com.srinnix.kindergarten.chatmenu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chatmenu.adapter.viewholder.ChatViewHolder;
import com.srinnix.kindergarten.model.ChatMember;

import java.util.ArrayList;

/**
 * Created by DELL on 2/6/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatViewHolder> {
	private ArrayList<ChatMember> arrayList;
	private OnClickItemChatListener onClickItemChatListener;
	
	public ChatListAdapter(ArrayList<ChatMember> arrayList, OnClickItemChatListener onClickItemChatListener) {
		this.arrayList = arrayList;
		this.onClickItemChatListener = onClickItemChatListener;
	}
	
	@Override
	public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent
				, false);
		return new ChatViewHolder(view, onClickItemChatListener);
	}
	
	@Override
	public void onBindViewHolder(ChatViewHolder holder, int position) {
		holder.bindData(arrayList.get(position), position);
	}
	
	@Override
	public int getItemCount() {
		return arrayList.size();
	}
	
	public interface OnClickItemChatListener {
		void onClick(int position);
	}
}
