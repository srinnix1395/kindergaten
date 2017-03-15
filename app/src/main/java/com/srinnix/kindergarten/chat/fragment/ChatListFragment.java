package com.srinnix.kindergarten.chat.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.chat.delegate.ChatListDelegate;
import com.srinnix.kindergarten.chat.presenter.ChatListPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageListContact;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2/5/2017.
 */

public class ChatListFragment extends BaseFragment implements ChatListDelegate {
    @BindView(R.id.recyclerview_chat_list)
    RecyclerView recyclerView;

    private ArrayList<Contact> listContact;
    private ChatListAdapter adapter;
    private ChatListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected void initChildView() {
        listContact = new ArrayList<>();
        adapter = new ChatListAdapter(listContact, position
                -> mPresenter.onClickItemChat(listContact.get(position)));

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        mPresenter.getContactFromDatabase();
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
    public void onDisconnect() {
        mPresenter.onDisconnect(listContact);
    }

    @Subscribe
    public void onSetupContactList(MessageListContact message) {
        if (!listContact.isEmpty()) {
            listContact.clear();
        }
        listContact.addAll(message.arrayList);
        adapter.notifyItemRangeInserted(0, message.arrayList.size());
    }

    @Subscribe
    public void onSetupContactStatus(MessageContactStatus message) {
        mPresenter.onSetupContactStatus(message, listContact);
    }

    @Override
    public void addContactTeacher(ArrayList<ContactTeacher> contactTeachers) {
        if (listContact.size() > 0) {
            listContact.clear();
        }
        listContact.addAll(contactTeachers);
        adapter.notifyItemRangeInserted(0, contactTeachers.size());
    }

    @Override
    public void addContactParent(ArrayList<ContactParent> contactParents) {
        if (listContact.size() > 0) {
            listContact.clear();
        }
        listContact.addAll(contactParents);
        adapter.notifyItemRangeInserted(0, contactParents.size());
    }

    @Override
    public void updateStatus() {
        adapter.notifyItemRangeChanged(0, listContact.size());
    }
}
