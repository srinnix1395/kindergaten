package com.srinnix.kindergarten.chat.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.chat.helper.DetailChatHelper;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

/**
 * Created by DELL on 2/9/2017.
 */

public class DetailChatPresenter extends BasePresenter {

    private SocketUtil mSocketUtil;
    private String idSender;
    private String idReceiver;
    private String conversationID;
    private PublishSubject<Boolean> mSubject;
    private boolean isUserTyping;

    private DetailChatHelper mHelper;
    private Realm mRealm;

    public DetailChatPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mSocketUtil = KinderApplication.getInstance().getSocketUtil();

        mSubject = PublishSubject.create();
        mSubject.doOnNext(aBoolean -> isUserTyping = false)
                .debounce(5, TimeUnit.SECONDS)
                .subscribe(aBoolean -> mSocketUtil.sendStatusTyping(aBoolean, idSender, idReceiver));

        mHelper = new DetailChatHelper();
        mRealm = KinderApplication.getInstance().getRealm();
    }

    public void setupDataPresenter(Contact contact) {
        idReceiver = contact.getId();
        idSender = SharedPreUtils.getInstance(mContext).getUserID();
    }

    public void onClickMenuItemInfo() {
        //// TODO: 2/10/2017 menu item info on click
    }


    public void setupTextChange(EditText etMessage, ImageView imvSend) {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableOrDisableBtnSend(charSequence, imvSend);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isUserTyping) {
                    isUserTyping = true;
                    mSocketUtil.sendStatusTyping(true, idSender, idReceiver);
                }
                mSubject.onNext(false);
            }
        });
    }


    private void enableOrDisableBtnSend(CharSequence message, ImageView imvSend) {
        if (message.length() > 0) {
            if (!imvSend.isEnabled()) {
                imvSend.setEnabled(true);
            }
        } else {
            imvSend.setEnabled(false);
        }
    }

    public void onClickSend(String message, ArrayList<Object> listMessage
            , ChatAdapter adapter) {

        mRealm.beginTransaction();
        Message chatItem = mRealm.createObject(Message.class);
        chatItem.setId(String.valueOf(System.currentTimeMillis()));
        chatItem.setIdSender(idSender);
        chatItem.setIdReceiver(idReceiver);
        chatItem.setMessage(message);
        chatItem.setStatus(ChatConstant.PENDING);
        chatItem.setCreatedAt(System.currentTimeMillis());
        mRealm.commitTransaction();

        chatItem.setLayoutType(getLayoutType(listMessage, idSender, chatItem.getCreatedAt()));
        listMessage.add(chatItem);
        adapter.notifyItemRangeChanged(listMessage.size() - 2, 2);

        KinderApplication.getInstance().getSocketUtil().sendMessage(chatItem);
    }

    public void onMessage(Message message, ArrayList<Object> listMessage, ChatAdapter adapter) {
        message.setLayoutType(getLayoutType(listMessage, message.getIdSender(), message.getCreatedAt()));

        listMessage.add(message);
        adapter.notifyItemRangeChanged(listMessage.size() - 2, 2);
    }

    public void onServerReceived(Message data, String id, ArrayList<Object> listMessage
            , ChatAdapter adapter) {

        int i;
        for (i = listMessage.size() - 1; i >= 0; i--) {
            if (listMessage.get(i) instanceof Message
                    && ((Message) listMessage.get(i)).getId().equals(id)) {
                Message message = (Message) listMessage.get(i);

                message.setId(data.getId());
                message.setCreatedAt(data.getCreatedAt());
                message.setStatus(data.getStatus());
                break;
            }
        }
        adapter.notifyItemChanged(i);
    }

    public void onFriendReceived(Message data, ArrayList<Object> listMessage, ChatAdapter adapter) {
        int i;
        for (i = listMessage.size() - 1; i >= 0; i--) {
            if (listMessage.get(i) instanceof Message
                    && ((Message) listMessage.get(i)).getId().equals(data.getId())) {
                ((Message) listMessage.get(i)).setStatus(data.getStatus());
                break;
            }
        }

        adapter.notifyItemChanged(i);
    }

    private int getLayoutType(ArrayList<Object> listMessage, String idSender, long createdAt) {
        int size = listMessage.size();

        if (size == 0) {
            return ChatConstant.SINGLE;
        }

        if (!(listMessage.get(size - 1) instanceof Message)) {
            return ChatConstant.SINGLE;
        }

        Message prevMessage = (Message) listMessage.get(size - 1);
        if (!prevMessage.getIdSender().equals(idSender)) {
            return ChatConstant.SINGLE;
        }

        if (createdAt - prevMessage.getCreatedAt() > ChatConstant.TIME_DISTANCE) {
            return ChatConstant.SINGLE;
        }

        if (prevMessage.getLayoutType() == ChatConstant.SINGLE) {
            prevMessage.setLayoutType(ChatConstant.FIRST);
        } else {
            prevMessage.setLayoutType(ChatConstant.MIDDLE);
        }
        return ChatConstant.LAST;
    }

    public void onLoadMore(ArrayList<Object> listMessage, ChatAdapter adapter) {
        Realm realm = KinderApplication.getInstance().getRealm();
        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getPreviousMessage(realm, conversationID, listMessage, adapter, token, new DetailChatHelper.DetailChatHelperListener() {
            @Override
            public void onLoadMessageSuccessfully(ArrayList<Object> arrayList) {
                listMessage.addAll(1, arrayList);
                adapter.notifyItemRangeInserted(1, arrayList.size());
            }

            @Override
            public void onLoadMessageFail(Throwable throwable) {
                ((LoadingItem) listMessage.get(0)).setLoadingState(LoadingItem.STATE_ERROR);
                adapter.notifyItemChanged(0);
            }
        });
    }
}
