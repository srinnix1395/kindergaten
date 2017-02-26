package com.srinnix.kindergarten.chat.presenter;

import android.widget.ImageView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by DELL on 2/9/2017.
 */

public class DetailChatPresenter extends BasePresenter {

    public DetailChatPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void onClickMenuItemInfo() {
        //// TODO: 2/10/2017 menu item info on click
    }

    public void onMessageTextChanged(CharSequence message, ImageView imvSend) {
        if (message.length() > 0) {
            if (!imvSend.isEnabled()) {
                imvSend.setEnabled(true);
            }
        } else {
            imvSend.setEnabled(false);
        }
    }

    public void onClickSend(String message, Realm realm, ArrayList<Message> listMessage
            , ChatAdapter adapter, String idSender, String idReceiver) {
        int size = listMessage.size();

        realm.beginTransaction();
        Message chatItem = realm.createObject(Message.class);
        chatItem.setId(String.valueOf(System.currentTimeMillis()));
        chatItem.setIdSender(idSender);
        chatItem.setIdReceiver(idReceiver);
        chatItem.setMessage(message);
        chatItem.setStatus(ChatConstant.PENDING);
        chatItem.setCreatedAt(System.currentTimeMillis());
        realm.commitTransaction();

        if (size == 0) {
            chatItem.setLayoutType(ChatConstant.SINGLE);
        } else {
            Message prevMessage = listMessage.get(size - 1);
            if (!prevMessage.getIdSender().equals(idSender)) {
                chatItem.setLayoutType(ChatConstant.SINGLE);
            } else if (prevMessage.getLayoutType() == ChatConstant.SINGLE) {
                prevMessage.setLayoutType(ChatConstant.FIRST);
                chatItem.setLayoutType(ChatConstant.LAST);
            } else {
                prevMessage.setLayoutType(ChatConstant.MIDDLE);
                chatItem.setLayoutType(ChatConstant.LAST);
            }
        }
        listMessage.add(chatItem);
        adapter.notifyItemInserted(size);

        KinderApplication.getInstance().getSocketUtil().sendMessage(chatItem);
    }


    public void onServerReceived(Message data, String id, ArrayList<Message> listMessage
            , ChatAdapter adapter) {

        int i;
        for (i = listMessage.size() - 1; i >= 0; i--) {
            if (listMessage.get(i).getId().equals(id)) {
                listMessage.get(i).setId(data.getId());
                listMessage.get(i).setCreatedAt(data.getCreatedAt());
                listMessage.get(i).setStatus(data.getStatus());
                break;
            }
        }
        adapter.notifyItemChanged(i);
    }

    public void onMessage(Message message, ArrayList<Message> listMessage, ChatAdapter adapter) {
        int size = listMessage.size();
        if (size == 0) {
            message.setLayoutType(ChatConstant.SINGLE);
        } else {
            Message prevMessage = listMessage.get(size - 1);
            if (!prevMessage.getIdSender().equals(idSender)) {
                message.setLayoutType(ChatConstant.SINGLE);
            } else if (prevMessage.getLayoutType() == ChatConstant.SINGLE) {
                prevMessage.setLayoutType(ChatConstant.FIRST);
                message.setLayoutType(ChatConstant.LAST);
            } else {
                prevMessage.setLayoutType(ChatConstant.MIDDLE);
                message.setLayoutType(ChatConstant.LAST);
            }
        }

        listMessage.add(message.message);
        adapter.notifyItemInserted(listMessage.size() - 1);
    }
}
