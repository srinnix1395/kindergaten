package com.srinnix.kindergarten.chat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.presenter.ChatListPresenter;
import com.srinnix.kindergarten.model.ChatMember;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class ChatListFragment extends BaseFragment {
	@BindView(R.id.recyclerview_chat_list)
	RecyclerView recyclerView;
	
	private ArrayList<ChatMember> arrayList;
	private ChatListAdapter adapter;
	private ChatListPresenter mPresenter;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_chat_list;
	}
	
	@Override
	protected void initChildView() {
		arrayList = new ArrayList<>();
		adapter = new ChatListAdapter(arrayList, position -> {
			mPresenter.onClickItemChat(arrayList.get(position));
		});
		
		recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		recyclerView.setAdapter(adapter);
	}
	
	@Override
	protected BasePresenter initPresenter() {
		mPresenter = new ChatListPresenter(this);
		return mPresenter;
	}
}
