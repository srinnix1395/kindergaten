package com.srinnix.kindergarten.chat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.presenter.ChatListPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageDisconnect;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;

/**
 * Created by DELL on 2/5/2017.
 */

public class ChatListFragment extends BaseFragment {
    @BindView(R.id.recyclerview_chat_list)
    RecyclerView recyclerView;

    private ArrayList<Contact> arrayList;
    private ChatListAdapter adapter;
    private ChatListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected void initChildView() {
        arrayList = new ArrayList<>();
        adapter = new ChatListAdapter(arrayList, position
                -> mPresenter.onClickItemChat(arrayList.get(position)));

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        Realm realm = KinderApplication.getInstance().getRealm();
        mPresenter.getContactFromDatabase(realm, arrayList, adapter);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ChatListPresenter(this);
        return mPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDisconnect(MessageDisconnect message) {
        mPresenter.onDisconnect(arrayList, adapter);
    }

    @Subscribe
    public void onSetupContactList(MessageListContact message) {
        mPresenter.onSetupContactList(message, arrayList, adapter);
    }

    @Subscribe
    public void onSetupContactsStatus(MessageContactStatus message) {
        mPresenter.onSetupContactStatus(message, arrayList, adapter);
    }
}
