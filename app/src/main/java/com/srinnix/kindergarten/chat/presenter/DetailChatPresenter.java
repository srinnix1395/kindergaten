package com.srinnix.kindergarten.chat.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.delegate.DetailChatDelegate;
import com.srinnix.kindergarten.chat.helper.DetailChatHelper;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;

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
    private String idSender;
    private String idReceiver;
    private String conversationID;
    private PublishSubject<Boolean> mSubject;
    private boolean isUserTyping;

    private DetailChatHelper mHelper;
    private Realm mRealm;
    private CompositeDisposable mDisposable;
    private DetailChatDelegate mDetailChatDelegate;

    public DetailChatPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDetailChatDelegate = (DetailChatDelegate) mDelegate;
        mSocketUtil = KinderApplication.getInstance().getSocketUtil();
        mDisposable = new CompositeDisposable();

        mSubject = PublishSubject.create();
        mDisposable.add(mSubject.doOnNext(aBoolean -> isUserTyping = false)
                .debounce(5, TimeUnit.SECONDS)
                .subscribe(o -> mSocketUtil.sendStatusTyping(idSender, idReceiver, false)));

        mHelper = new DetailChatHelper(mDisposable);
        mRealm = KinderApplication.getInstance().getRealm();
    }

    public void setupDataPresenter(Contact contact) {
        idReceiver = contact.getId();
        idSender = SharedPreUtils.getInstance(mContext).getUserID();

        if (idSender.compareTo(idReceiver) > 0) {
            conversationID = idSender + idReceiver;
        } else {
            conversationID = idReceiver + idSender;
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
                if (!isUserTyping) {
                    isUserTyping = true;
                    mSocketUtil.sendStatusTyping(idSender, idReceiver, true);
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

    public void onClickSend(String message, ArrayList<Object> listMessage) {

        mRealm.beginTransaction();
        long l = System.currentTimeMillis();
        Message chatItem = mRealm.createObject(Message.class, String.valueOf(l));
        chatItem.setIdSender(idSender);
        chatItem.setIdReceiver(idReceiver);
        chatItem.setMessage(message);
        chatItem.setStatus(ChatConstant.PENDING);
        chatItem.setCreatedAt(l);
        mRealm.commitTransaction();

        chatItem.setLayoutType(getLayoutType(listMessage, idSender, chatItem.getCreatedAt()));
        if (mDetailChatDelegate != null) {
            mDetailChatDelegate.addMessageLast(chatItem);
        }

        KinderApplication.getInstance().getSocketUtil().sendMessage(chatItem);
    }

    public void onMessage(Message message, ArrayList<Object> listMessage) {
        if (message.getIdSender().equals(idReceiver)) {
            message.setLayoutType(getLayoutType(listMessage, message.getIdSender(), message.getCreatedAt()));

            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.addMessageLast(message);
            }
        }
    }

    public void onServerReceived(Message data, String id, ArrayList<Object> listMessage) {

        if (data.getIdSender().equals(idReceiver)) {
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
            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.changeMessage(i);
            }
        }
    }

    public void onFriendReceived(Message data, ArrayList<Object> listMessage) {
        if (data.getIdSender().equals(idReceiver)) {
            int i;
            for (i = listMessage.size() - 1; i >= 0; i--) {
                if (listMessage.get(i) instanceof Message
                        && ((Message) listMessage.get(i)).getId().equals(data.getId())) {
                    ((Message) listMessage.get(i)).setStatus(data.getStatus());
                    break;
                }
            }

            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.changeMessage(i);
            }
        }
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

    public void onLoadMore(ArrayList<Object> listMessage) {
        Realm realm = KinderApplication.getInstance().getRealm();
        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getPreviousMessage(realm, conversationID, listMessage, token, new DetailChatHelper.DetailChatHelperListener() {
            @Override
            public void onLoadMessageSuccessfully(ArrayList<Object> arrayList) {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.addAllMessage(arrayList, 1);
                }
            }

            @Override
            public void onLoadMessageFail(Throwable throwable) {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.loadMessageFail();
                }
            }
        });
    }

    public void onFriendTyping(Message message, ArrayList<Object> listMessage) {
        if (message.getIdSender().equals(idReceiver)) {
            if (!listMessage.isEmpty()) {
                Object o = listMessage.get(listMessage.size() - 1);
                if (message.isTypingMessage()) {
                    if (o instanceof Message && !(((Message) o).isTypingMessage())) {
                        if (mDetailChatDelegate != null) {
                            mDetailChatDelegate.addMessageLast(message, listMessage.size() - 1);
                        }
                    }
                } else {
                    if (o instanceof Message && ((Message) o).isTypingMessage()) {
                        if (mDetailChatDelegate != null) {
                            mDetailChatDelegate.removeMessage(listMessage.size()-1);
                        }
                    }
                }
            } else {
                if (message.isTypingMessage()) {
                    if (mDetailChatDelegate != null) {
                        mDetailChatDelegate.addMessageLast(message, listMessage.size() - 1);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
