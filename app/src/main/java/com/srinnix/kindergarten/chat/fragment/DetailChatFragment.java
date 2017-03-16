package com.srinnix.kindergarten.chat.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.chat.delegate.DetailChatDelegate;
import com.srinnix.kindergarten.chat.presenter.DetailChatPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.custom.EndlessScrollListener;
import com.srinnix.kindergarten.custom.ItemChatAnimator;
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.messageeventbus.MessageTyping;
import com.srinnix.kindergarten.messageeventbus.MessageUserDisconnect;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2/9/2017.
 */

public class DetailChatFragment extends BaseFragment implements DetailChatDelegate {
    @BindView(R.id.recyclerview_detailchat)
    RecyclerView rvChat;

    @BindView(R.id.toolbar_detail_chat)
    Toolbar toolbar;

    @BindView(R.id.edittext_message)
    EditText etMessage;

    @BindView(R.id.imageview_send)
    ImageView imvSend;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_error)
    TextView tvError;

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
        contact = bundle.getParcelable(AppConstant.KEY_INFO);
        mPresenter.setupDataPresenter(contact);
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(contact != null ? contact.getName() : "");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            UiUtils.hideKeyboard(getActivity());
            onBackPressed();
        });

        listMessage = new ArrayList<>();

        rvChat.setVisibility(View.INVISIBLE);
        rvChat.setItemAnimator(new ItemChatAnimator());
        adapter = new ChatAdapter(mContext, listMessage, () -> mPresenter.onLoadMore(listMessage));
        rvChat.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvChat.addOnScrollListener(new EndlessScrollListener(layoutManager
                , EndlessScrollListener.POSITION_UP, ChatConstant.ITEM_MESSAGE_PER_PAGE) {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore(listMessage);
            }
        });
        rvChat.setLayoutManager(layoutManager);

        mPresenter.setupTextChange(etMessage, imvSend);

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);
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
        mPresenter.onClickSend(etMessage.getText().toString(), listMessage);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEventMessageIncoming(MessageChat message) {
        mPresenter.onMessage(message.message, listMessage);
    }

    @Subscribe
    public void onEventServerReceied(MessageServerReceived message) {
        mPresenter.onServerReceived(message.data, message.id, listMessage);
    }

    @Subscribe
    public void onEventFriendReceived(MessageFriendReceived message) {
        mPresenter.onFriendReceived(message.data, listMessage);
    }

    @Subscribe
    public void onEventFriendTyping(MessageTyping message) {
        if (isResumed()) {
            mPresenter.onFriendTyping(message.mMessage, listMessage);
        }
    }

    @Subscribe
    public void onEventUserDisconnect(MessageUserDisconnect message) {
        //todo

    }

    @Override
    public void changeMessage(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void loadMessageSuccess(ArrayList<Object> arrayList, boolean isLoadingDataFirst) {
        if (arrayList.size() == ChatConstant.ITEM_MESSAGE_PER_PAGE) {
            if (!(listMessage.get(0) instanceof LoadingItem)) {
                listMessage.add(new LoadingItem());
                adapter.notifyItemInserted(0);
            }
            listMessage.addAll(1, arrayList);
            adapter.notifyItemRangeInserted(1, arrayList.size());
        } else {
            if ((listMessage.get(0) instanceof LoadingItem)) {
                listMessage.remove(0);
                adapter.notifyItemRemoved(0);
            }
            listMessage.addAll(0, arrayList);
            adapter.notifyItemRangeInserted(0, arrayList.size());
        }

        if (isLoadingDataFirst) {
            UiUtils.hideProgressBar(pbLoading);
            rvChat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMessageFail(boolean isLoadingDataFirst) {
        if (isLoadingDataFirst) {
            UiUtils.hideProgressBar(pbLoading);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (listMessage.get(0) instanceof LoadingItem) {
            ((LoadingItem) listMessage.get(0)).setLoadingState(LoadingItem.STATE_ERROR);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void addMessageLast(Message message) {
        listMessage.add(message);
        adapter.notifyItemChanged(listMessage.size() - 2);
        adapter.notifyItemInserted(listMessage.size() - 1);
        rvChat.scrollToPosition(listMessage.size() - 1);
    }

    @Override
    public void addMessageLast(Message message, int position) {
        listMessage.add(message);
        adapter.notifyItemInserted(position);
        rvChat.scrollToPosition(position);
    }

    @Override
    public void removeMessage(int position) {
        listMessage.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
