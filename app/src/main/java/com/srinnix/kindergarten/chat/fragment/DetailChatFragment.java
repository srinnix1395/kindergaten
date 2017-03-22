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
import com.srinnix.kindergarten.chat.adapter.payload.ImagePayload;
import com.srinnix.kindergarten.chat.adapter.payload.StatusMessagePayload;
import com.srinnix.kindergarten.chat.delegate.DetailChatDelegate;
import com.srinnix.kindergarten.chat.presenter.DetailChatPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.custom.EndlessScrollListener;
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageDisconnect;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.messageeventbus.MessageTyping;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_status)
    TextView tvStatus;

    private DetailChatPresenter mPresenter;
    private ChatAdapter adapter;
    private ArrayList<Object> listMessage;

    private String name;
    private int status;
    private String urlImage;
    private int accountType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_chat;
    }

    @Override
    protected void getData() {
        super.getData();
        Bundle bundle = getArguments();
        name = bundle.getString(AppConstant.KEY_NAME);
        status = bundle.getInt(AppConstant.KEY_STATUS);
        urlImage = bundle.getString(AppConstant.KEY_IMAGE);
        accountType = bundle.getInt(AppConstant.KEY_ACCOUNT_TYPE);
    }

    @Override
    protected void initChildView() {
        setupToolbar();

        listMessage = new ArrayList<>();

        rvChat.setVisibility(View.INVISIBLE);
//        rvChat.setItemAnimator(new ItemChatAnimator());
        adapter = new ChatAdapter(mContext, listMessage, urlImage, accountType,
                () -> mPresenter.onLoadMore(listMessage));
        rvChat.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvChat.addOnScrollListener(new EndlessScrollListener(layoutManager
                , EndlessScrollListener.POSITION_UP, ChatConstant.ITEM_MESSAGE_PER_PAGE) {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore(listMessage);
            }
        });
        rvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom && !listMessage.isEmpty()) {
                rvChat.postDelayed(() -> rvChat.smoothScrollToPosition(listMessage.size() - 1), 100);
            }
        });
        rvChat.setLayoutManager(layoutManager);

        mPresenter.setupTextChange(etMessage, imvSend);

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        mPresenter.onLoadMore(listMessage);
    }

    private void setupToolbar() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(name);
        toolbar.inflateMenu(R.menu.menu_detail_chat);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            UiUtils.hideKeyboard(getActivity());
            onBackPressed();
        });

        tvName.setText(name);
        tvStatus.setText(StringUtil.getStatus(mContext, status));
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
        mPresenter.onClickSend(listMessage, etMessage, imvSend.getDrawable().getLevel());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventConnect(MessageContactStatus message) {
        mPresenter.onEventConnect(message.arrayList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessageIncoming(MessageChat message) {
        mPresenter.onMessage(message.message, listMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventServerReceied(MessageServerReceived message) {
        mPresenter.onServerReceived(message.data, listMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFriendReceived(MessageFriendReceived message) {
        mPresenter.onFriendReceived(message.data, listMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFriendTyping(MessageTyping message) {
        if (isResumed()) {
            mPresenter.onFriendTyping(message.mMessage, listMessage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUserConnect(MessageUserConnect message) {
        mPresenter.onUserConnect(message, tvStatus, listMessage);
    }

    @Subscribe
    public void onEventDisconnect(MessageDisconnect message) {
        mPresenter.onDisconnect(tvStatus, listMessage);
    }

    @Override
    public void changeDataMessage(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void changeDataMessage(int position, boolean isDisplayIcon) {
        adapter.notifyItemChanged(position, new ImagePayload(isDisplayIcon));
    }

    @Override
    public void changeDataMessage(int position, int statusMessagePayload) {
        adapter.notifyItemChanged(position, new StatusMessagePayload(statusMessagePayload));
    }

    @Override
    public void loadMessageSuccess(ArrayList<Message> arrayList, boolean isLoadingDataFirst) {
        if (arrayList.size() == 0) {
            if (!listMessage.isEmpty() && (listMessage.get(0) instanceof LoadingItem)) {
                listMessage.remove(0);
                adapter.notifyItemRemoved(0);
            }
        } else if (arrayList.size() == ChatConstant.ITEM_MESSAGE_PER_PAGE) {
            if (listMessage.isEmpty() || !(listMessage.get(0) instanceof LoadingItem)) {
                listMessage.add(new LoadingItem());
                adapter.notifyItemInserted(0);
            }
            listMessage.addAll(1, arrayList);
            adapter.notifyItemRangeInserted(1, arrayList.size());
        } else {
            if (!listMessage.isEmpty() && (listMessage.get(0) instanceof LoadingItem)) {
                listMessage.remove(0);
                adapter.notifyItemRemoved(0);
            }
            listMessage.addAll(0, arrayList);
            adapter.notifyItemRangeInserted(0, arrayList.size());
        }

//        if (listMessage.size() > 0) {
//            rvChat.smoothScrollToPosition(listMessage.size() - 1);
//        }
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

        if (!listMessage.isEmpty() && listMessage.get(0) instanceof LoadingItem) {
            ((LoadingItem) listMessage.get(0)).setLoadingState(LoadingItem.STATE_ERROR);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void addMessage(Message message, int position) {
        listMessage.add(position, message);
        adapter.notifyItemInserted(position);
        rvChat.smoothScrollToPosition(listMessage.size() - 1);
    }

    @Override
    public void removeMessage(int position) {
        listMessage.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void setStatus(String status) {
        tvStatus.setText(status);
    }
}
