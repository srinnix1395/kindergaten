package com.srinnix.kindergarten.chat.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.chat.presenter.DetailChatPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.custom.EndlessScrollListener;
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.messageeventbus.MessageTyping;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2/9/2017.
 */

public class DetailChatFragment extends BaseFragment {
    @BindView(R.id.recyclerview_detailchat)
    RecyclerView rvChat;

    @BindView(R.id.toolbar_detail_chat)
    Toolbar toolbar;

    @BindView(R.id.edittext_message)
    EditText etMessage;

    @BindView(R.id.imageview_send)
    ImageView imvSend;

    private DetailChatPresenter mPresenter;
    private ChatAdapter adapter;
    private ArrayList<Object> listMessage;

    private Contact contact;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_chat;
    }

    @Override
    protected void getData() {
        super.getData();
        Bundle bundle = getArguments();
        contact = Parcels.unwrap(bundle.getParcelable(AppConstant.KEY_INFO));
        mPresenter.setupDataPresenter(contact);
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(contact != null ? contact.getName() : "");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            UiUtils.hideKeyboard(getActivity());
            getActivity().finish();
        });

        listMessage = new ArrayList<>();
        listMessage.add(new LoadingItem());
        listMessage.add(new Message("dà", "-1", "3", "xin chàsadfffffffffffsdafsdafsdafsdafsdao", 234234234, ChatConstant.SERVER_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("dà", "-1", "3", "xin chàsadfffffffffffsdafsdafsdafsdafsdao", 234234234, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("dà", "-1", "3", "xin chàsadfffffffffffsdafsdafsdafsdafsdao", 234234234, ChatConstant.PENDING, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.PENDING, ChatConstant.MIDDLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad ", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.LAST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệtsa dsa d", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sadf sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sad", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt sad sda", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.FIRST));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.HANDLE_COMPLETE, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệtsdafasd", 234234324, ChatConstant.PENDING, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệt", 234234324, ChatConstant.FRIEND_RECEIVED, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệtsdafsd", 234234324, ChatConstant.PENDING, ChatConstant.SINGLE));
        listMessage.add(new Message("á", "3", "-1", "tạm biệtsdafsd", 234234324, ChatConstant.PENDING, ChatConstant.SINGLE, true));

        adapter = new ChatAdapter(mContext, listMessage, () -> {
//            mPresenter.onLoadMore(listMessage, adapter);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvChat.addOnScrollListener(new EndlessScrollListener(layoutManager
                , EndlessScrollListener.POSITION_UP, ChatConstant.ITEM_MESSAGE_PER_PAGE) {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore(listMessage, adapter);
            }
        });
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(adapter);

        mPresenter.setupTextChange(etMessage, imvSend);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new DetailChatPresenter(this);
        return mPresenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onClickMenuItemInfo();
        return true;
    }

    @OnClick(R.id.imageview_send)
    void onClickSend() {
        mPresenter.onClickSend(etMessage.getText().toString(),
                listMessage, adapter);
    }

    @Subscribe
    void onMessageIncoming(MessageChat message) {
        mPresenter.onMessage(message.message, listMessage, adapter);
    }

    @Subscribe
    void onServerReceied(MessageServerReceived message) {
        mPresenter.onServerReceived(message.data, message.id, listMessage, adapter);
    }

    @Subscribe
    void onFriendReceived(MessageFriendReceived message) {
        mPresenter.onFriendReceived(message.data, listMessage, adapter);
    }

    @Subscribe
    void onFriendTyping(MessageTyping message) {
        mPresenter.onFriendTyping(message.mMessage, listMessage, adapter);
    }
}
