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
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

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
    private ArrayList<Message> listMessage;
    private String nameConversation;

    private Realm realm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_chat;
    }

    @Override
    protected void getData() {
        super.getData();
        Bundle bundle = getArguments();
        nameConversation = bundle.getString(AppConstant.KEY_NAME_CONVERSATION, "");
        mPresenter.setupDataPresenter(bundle);
    }

    @Override
    protected void initChildView() {
        realm = Realm.getDefaultInstance();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(nameConversation);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            // TODO: 2/9/2017 back
            UiUtils.hideKeyboard(getActivity());
        });

        listMessage = new ArrayList<>();
        adapter = new ChatAdapter(mContext, listMessage);
        rvChat.setLayoutManager(new LinearLayoutManager(mContext));
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
                realm, listMessage, adapter);
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
}
