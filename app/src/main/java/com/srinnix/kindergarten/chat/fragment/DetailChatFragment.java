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
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

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
    private ArrayList<Object> listMessage;

    private Realm realm;
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
        realm = Realm.getDefaultInstance();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(contact != null ? contact.getName() : "");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            UiUtils.hideKeyboard(getActivity());
            getActivity().finish();
        });

        listMessage = new ArrayList<>();
        listMessage.add(new LoadingItem());

        adapter = new ChatAdapter(mContext, listMessage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);


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

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
