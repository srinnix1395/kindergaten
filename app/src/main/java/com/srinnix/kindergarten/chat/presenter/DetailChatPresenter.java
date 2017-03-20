package com.srinnix.kindergarten.chat.presenter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.delegate.DetailChatDelegate;
import com.srinnix.kindergarten.chat.helper.DetailChatHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;
import com.srinnix.kindergarten.util.StringUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

/**
 * Created by DELL on 2/9/2017.
 */

public class DetailChatPresenter extends BasePresenter {

    private SocketUtil mSocketUtil;
    private String mMyId;
    private String mFriendId;
    private String mConversationID;
    private PublishSubject<Boolean> mSubject;
    private boolean mIsUserTyping;

    private DetailChatHelper mHelper;
    private Realm mRealm;
    private CompositeDisposable mDisposable;
    private DetailChatDelegate mDetailChatDelegate;
    private boolean mIsLoadingDataFirst = true;

    public DetailChatPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDetailChatDelegate = (DetailChatDelegate) mDelegate;
        mSocketUtil = KinderApplication.getInstance().getSocketUtil();
        mDisposable = new CompositeDisposable();

        mSubject = PublishSubject.create();
        mDisposable.add(mSubject.doOnNext(aBoolean -> mIsUserTyping = false)
                .debounce(5, TimeUnit.SECONDS)
                .subscribe(o -> mSocketUtil.sendStatusTyping(mMyId, mFriendId, false)));

        mHelper = new DetailChatHelper(mDisposable);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);

        mFriendId = bundle.getString(AppConstant.KEY_ID);
        mMyId = SharedPreUtils.getInstance(mContext).getUserID();

        if (mMyId.compareTo(mFriendId) > 0) {
            mConversationID = mMyId + mFriendId;
        } else {
            mConversationID = mFriendId + mMyId;
        }
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
                if (!mIsUserTyping) {
                    mIsUserTyping = true;
                    mSocketUtil.sendStatusTyping(mMyId, mFriendId, true);
                }
                mSubject.onNext(false);
            }
        });
    }


    private void enableOrDisableBtnSend(CharSequence message, ImageView imvSend) {
        if (message.length() > 0) {
            if (!imvSend.isEnabled()) {
                imvSend.setEnabled(true);
                imvSend.setImageLevel(2);
            }
        } else {
            imvSend.setEnabled(false);
            imvSend.setImageLevel(1);
        }
    }

    public void onClickSend(ArrayList<Object> listMessage, EditText editText) {
        String message = editText.getText().toString().trim();
        editText.setText("");

        mRealm.beginTransaction();
        long l = System.currentTimeMillis();
        Message chatItem = mRealm.createObject(Message.class);
        chatItem.setId(String.valueOf(l));
        chatItem.setIdSender(mMyId);
        chatItem.setIdReceiver(mFriendId);
        chatItem.setConversationId(mConversationID);
        chatItem.setMessage(message);
        chatItem.setStatus(ChatConstant.PENDING);
        chatItem.setCreatedAt(l);
        mRealm.commitTransaction();

        if (listMessage.isEmpty()) {
            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.addMessage(chatItem, 0);
            }
        } else {
            Object o = listMessage.get(listMessage.size() - 1);
            if (o instanceof Message && ((Message) o).isTypingMessage()) {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.addMessage(chatItem, listMessage.size() - 1);
                }
            } else {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.addMessage(chatItem, listMessage.size());
                }
            }
        }


        KinderApplication.getInstance().getSocketUtil().sendMessage(chatItem);
    }

    public void onMessage(Message message, ArrayList<Object> listMessage) {
        if (mDetailChatDelegate == null || !message.getIdSender().equals(mFriendId)) {
            return;
        }

        if (listMessage.isEmpty()) {
            mDetailChatDelegate.addMessage(message, 0);
            return;
        }

        int size = listMessage.size();
        Object objectLast = listMessage.get(size - 1);
        if (objectLast instanceof Message && ((Message) objectLast).isTypingMessage()) {
            if (size >= 2) {
                Object o1 = listMessage.get(size - 2);
                if (o1 instanceof Message && ((Message) o1).getIdReceiver().equals(message.getIdReceiver())) {
                    ((Message) o1).setDisplayIcon(false);
                    mDetailChatDelegate.changeDataMessage(size - 2, false);
                }
            }

            Message m = (Message) objectLast;
            m.setId(message.getId());
            m.setIdSender(message.getIdSender());
            m.setIdReceiver(message.getIdReceiver());
            m.setConversationId(message.getConversationId());
            m.setMessage(message.getMessage());
            m.setCreatedAt(message.getCreatedAt());
            m.setTypingMessage(false);
            m.setDisplayIcon(true);

            mDetailChatDelegate.changeDataMessage(listMessage.size() - 1);
        } else {
            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.addMessage(message, listMessage.size());
            }
        }
    }

    public void onServerReceived(Message data, ArrayList<Object> listMessage) {
        if (data.getIdReceiver().equals(mFriendId)) {
            int positionChanged = getPositionChanged(listMessage, data.getId());
            if (mDetailChatDelegate != null && positionChanged >= 0) {
                mDetailChatDelegate.changeDataMessage(positionChanged, data.getStatus());
            }
        }
    }

    public void onFriendReceived(Message data, ArrayList<Object> listMessage) {
        if (data.getIdReceiver().equals(mFriendId)) {
            int positionChanged = getPositionChanged(listMessage, data.getId());
            if (mDetailChatDelegate != null && positionChanged >= 0) {
                mDetailChatDelegate.changeDataMessage(positionChanged, data.getStatus());
            }
        }
    }

    private int getPositionChanged(ArrayList<Object> listMessage, String id) {
        int i;
        for (i = listMessage.size() - 1; i >= 0; i--) {
            if (listMessage.get(i) instanceof Message
                    && ((Message) listMessage.get(i)).getId().equals(id)) {
                break;
            }
        }
        return i;
    }

    public void onFriendTyping(Message message, ArrayList<Object> listMessage) {
        if (mDetailChatDelegate == null || !message.getIdSender().equals(mFriendId)) {
            return;
        }

        if (listMessage.isEmpty()) {
            if (message.isTypingMessage()) {
                mDetailChatDelegate.addMessage(message, 0);
            }
            return;
        }

        int size = listMessage.size();
        Object o = listMessage.get(size - 1);
        if (message.isTypingMessage()) {
            if (o instanceof Message && !(((Message) o).isTypingMessage())) {
                ((Message) o).setDisplayIcon(false);
                mDetailChatDelegate.changeDataMessage(size - 1);
                mDetailChatDelegate.addMessage(message, listMessage.size());
            }
        } else {
            if (o instanceof Message && ((Message) o).isTypingMessage()) {
                if (size >= 2) {
                    Object o1 = listMessage.get(size - 2);
                    if (o1 instanceof Message && ((Message) o1).getIdReceiver().equals(message.getIdReceiver())) {
                        ((Message) o1).setDisplayIcon(true);
                        mDetailChatDelegate.changeDataMessage(size - 2);
                    }
                }
                mDetailChatDelegate.removeMessage(size - 1);
            }
        }
    }

    public void onLoadMore(ArrayList<Object> listMessage) {
        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getPreviousMessage(mContext, mConversationID, listMessage, token, new DetailChatHelper.DetailChatHelperListener() {
            @Override
            public void onLoadMessageSuccessfully(ArrayList<Object> arrayList) {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.loadMessageSuccess(arrayList, mIsLoadingDataFirst);
                }
                if (mIsLoadingDataFirst) {
                    mIsLoadingDataFirst = false;
                }
            }

            @Override
            public void onLoadMessageFail(Throwable throwable) {
                Log.d("ads", "onLoadMessageFail: " + throwable.getMessage());
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.loadMessageFail(mIsLoadingDataFirst);
                }
                if (mIsLoadingDataFirst) {
                    mIsLoadingDataFirst = false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }

        if (!mRealm.isClosed()) {
            mRealm.close();
        }
    }

    public void onUserConnect(MessageUserConnect message, TextView tvStatus, ArrayList<Object> listMessage) {
        if (mDetailChatDelegate == null || !message.id.equals(mFriendId)) {
            return;
        }

        if (tvStatus != null) {
            tvStatus.setText(StringUtil.getStatus(mContext,
                    message.isConnected ? ChatConstant.STATUS_ONLINE : ChatConstant.STATUS_OFFLINE));
        }

        if (!listMessage.isEmpty()) {
            int size = listMessage.size();
            Object o = listMessage.get(size - 1);
            if (o instanceof Message && ((Message) o).isTypingMessage()) {
                ((Message) o).setTypingMessage(false);
                mDetailChatDelegate.changeDataMessage(size - 1);
            }
        }
    }

    public void onDisconnect(TextView tvStatus, ArrayList<Object> listMessage) {
        if (mDetailChatDelegate == null) {
            return;
        }

        if (tvStatus != null) {
            tvStatus.setText(StringUtil.getStatus(mContext, ChatConstant.STATUS_UNDEFINED));
        }

        if (!listMessage.isEmpty()) {
            int size = listMessage.size();
            Object o = listMessage.get(size - 1);
            if (o instanceof Message && ((Message) o).isTypingMessage()) {
                ((Message) o).setTypingMessage(false);
                mDetailChatDelegate.changeDataMessage(size - 1);
            }
        }
    }

    public void onConnect(TextView tvStatus) {
        if (tvStatus != null) {
//            tvStatus.setText();
            // TODO: 3/20/2017 khi connect laij phai xem trang thai cua thang ng dung laf gi
        }
    }
}
