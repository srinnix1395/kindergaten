package com.srinnix.kindergarten.chat.presenter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.MediaPickerFragment;
import com.srinnix.kindergarten.chat.delegate.DetailChatDelegate;
import com.srinnix.kindergarten.chat.helper.DetailChatHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.ViewManager;

import org.greenrobot.eventbus.EventBus;

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
    private String name;
    private int status;
    private String urlImage;
    private int accountType;

    private PublishSubject<Boolean> mSubjectTyping;
    private PublishSubject<ArrayList<Object>> mSubjectLoadMore;
    private boolean mIsUserTyping;

    private DetailChatHelper mHelper;
    private Realm mRealm;
    private DetailChatDelegate mDetailChatDelegate;
    private boolean isLoadingDataFirst = true;

    public DetailChatPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDetailChatDelegate = (DetailChatDelegate) mDelegate;
        mSocketUtil = KinderApplication.getInstance().getSocketUtil();
        mDisposable = new CompositeDisposable();

        mHelper = new DetailChatHelper(mDisposable);
        mRealm = Realm.getDefaultInstance();

        mSubjectTyping = PublishSubject.create();
        mDisposable.add(mSubjectTyping.doOnNext(aBoolean -> mIsUserTyping = false)
                .debounce(5, TimeUnit.SECONDS)
                .subscribe(o -> mSocketUtil.sendStatusTyping(mMyId, mFriendId, false)));

        mSubjectLoadMore = PublishSubject.create();
        mDisposable.add(
                mSubjectLoadMore
                        .debounce(1, TimeUnit.SECONDS)
                        .flatMap(listMessage -> {
                            String token = SharedPreUtils.getInstance(mContext).getToken();
                            return mHelper.getPreviousMessage(mContext, mConversationID, listMessage, token);
                        })
                        .subscribe(messageArrayList -> {
                            if (mDetailChatDelegate != null) {
                                mDetailChatDelegate.loadMessageSuccess(messageArrayList, isLoadingDataFirst);
                            }
                            if (isLoadingDataFirst) {
                                isLoadingDataFirst = false;
                            }
                        }, throwable -> {
                            DebugLog.i(throwable.getMessage());
                            if (mDetailChatDelegate != null) {
                                mDetailChatDelegate.loadMessageFail(isLoadingDataFirst, throwable.getMessage());
                            }
                            if (isLoadingDataFirst) {
                                isLoadingDataFirst = false;
                            }
                        }));
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);

        mFriendId = bundle.getString(AppConstant.KEY_ID);
        mMyId = SharedPreUtils.getInstance(mContext).getUserID();
        name = bundle.getString(AppConstant.KEY_NAME);
        MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);
        if (message != null) {
            if (message.arrayList.contains(mFriendId)) {
                status = ChatConstant.STATUS_ONLINE;
            } else {
                status = ChatConstant.STATUS_OFFLINE;
            }
        }
        urlImage = bundle.getString(AppConstant.KEY_ICON);
        accountType = bundle.getInt(AppConstant.KEY_ACCOUNT_TYPE);

        if (bundle.containsKey(AppConstant.KEY_CONVERSATION_ID)) {
            mConversationID = bundle.getString(AppConstant.KEY_CONVERSATION_ID);
        } else {
            if (mMyId.compareTo(mFriendId) > 0) {
                mConversationID = mMyId + mFriendId;
            } else {
                mConversationID = mFriendId + mMyId;
            }
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
                mSubjectTyping.onNext(false);
            }
        });
    }

    private void enableOrDisableBtnSend(CharSequence message, ImageView imvSend) {
        if (message.length() > 0) {
            imvSend.setImageLevel(2);
        } else {
            imvSend.setImageLevel(1);
        }
    }

    public void onClickSend(ArrayList<Object> listMessage, EditText editText, int level) {
        mRealm.beginTransaction();
        long l = System.currentTimeMillis();
        Message message = mRealm.createObject(Message.class);
        message.setId(String.valueOf(l));
        message.setIdSender(mMyId);
        message.setIdReceiver(mFriendId);
        message.setConversationId(mConversationID);
        if (level == 1 || level == 0) {
            message.setMessageType(ChatConstant.MSG_TYPE_ICON_HEART);
        } else {
            message.setMessage(editText.getText().toString().trim());
            editText.setText("");
        }
        message.setStatus(ChatConstant.PENDING);
        message.setCreatedAt(l);
        mRealm.commitTransaction();

        sendImage(listMessage, message);
    }

    private void sendImage(ArrayList<Object> listMessage, Message message) {
        if (listMessage.isEmpty()) {
            if (mDetailChatDelegate != null) {
                mDetailChatDelegate.addMessage(message, 0);
            }
        } else {
            Object objectLast = listMessage.get(listMessage.size() - 1);
            if (objectLast instanceof Message && ((Message) objectLast).isTypingMessage()) {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.addMessageWhileFriendTyping(message);
                }
            } else {
                if (mDetailChatDelegate != null) {
                    mDetailChatDelegate.addMessage(message, listMessage.size());
                }
            }
        }

        KinderApplication.getInstance().getSocketUtil().sendMessage(mContext, message);
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
//            if (size >= 2) {
//                Object o1 = listMessage.get(size - 2);
//                if (o1 instanceof Message && ((Message) o1).getIdReceiver().equals(message.getIdReceiver())) {
//                    ((Message) o1).setDisplayIcon(false);
//                    mDetailChatDelegate.changeDataMessage(size - 2, false);
//                }
//            }
            Message m = (Message) objectLast;
            m.setId(message.getId());
            m.setIdSender(message.getIdSender());
            m.setIdReceiver(message.getIdReceiver());
            m.setConversationId(message.getConversationId());
            m.setMessage(message.getMessage());
            m.setMessageType(message.getMessageType());
            m.setCreatedAt(message.getCreatedAt());
            m.setTypingMessage(false);
//            m.setDisplayIcon(true);

            mDetailChatDelegate.changeDataMessage(listMessage.size() - 1);
        } else {
//            if (objectLast instanceof Message && ((Message) objectLast).getIdSender().equals(message.getIdSender())) {
//                ((Message) objectLast).setDisplayIcon(false);
//                mDetailChatDelegate.changeDataMessage(size - 1, false);
//            }
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
                mDetailChatDelegate.addMessage(message, listMessage.size());
            }
        } else {
            if (o instanceof Message && ((Message) o).isTypingMessage()) {
                mDetailChatDelegate.removeMessage(size - 1);
            }
        }
    }

    public void onLoadMore(ArrayList<Object> listMessage) {
        mSubjectLoadMore.onNext(listMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    public void onEventConnect(ArrayList<String> arrayList) {
        if (mDetailChatDelegate == null) {
            return;
        }

        if (arrayList.contains(mFriendId)) {
            mDetailChatDelegate.setStatus(StringUtil.getStatus(mContext, ChatConstant.STATUS_ONLINE));
        } else {
            mDetailChatDelegate.setStatus(StringUtil.getStatus(mContext, ChatConstant.STATUS_OFFLINE));
        }
    }

    public boolean isRecyclerScrollable(RecyclerView recyclerView) {
        return recyclerView.computeVerticalScrollRange() > recyclerView.getHeight();
    }

    public void onClickImage() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEDIA, null);
        bundle.putInt(AppConstant.KEY_MEDIA_TYPE, AppConstant.TYPE_IMAGE);
        bundle.putInt(AppConstant.KEY_LIMIT, 1);
        bundle.putInt(AppConstant.KEY_FRAGMENT, AppConstant.FRAGMENT_DETAIL_CHAT);

        ViewManager.getInstance().addFragment(new MediaPickerFragment(), bundle,
                R.anim.translate_down_to_up, R.anim.translate_up_to_down);
    }

    public void onSendImage(ArrayList<Object> listMessage, ArrayList<MediaLocal> mListImage) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mDisposable.add(mHelper.uploadImage(token, mListImage)
                .doOnSubscribe(disposable -> AlertUtils.showToast(mContext, R.string.uploading))
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        AlertUtils.showToast(mContext, R.string.error_common);
                        return;
                    }

                    if (response.getData().getUrl().equals("null")) {
                        AlertUtils.showToast(mContext, R.string.error_upload);
                        return;
                    }

                    mRealm.beginTransaction();
                    long l = System.currentTimeMillis();
                    Message message = mRealm.createObject(Message.class);
                    message.setId(String.valueOf(l));
                    message.setIdSender(mMyId);
                    message.setIdReceiver(mFriendId);
                    message.setConversationId(mConversationID);
                    message.setMessageType(ChatConstant.MSG_TYPE_MEDIA);
                    message.setMessage(response.getData().getUrl());
                    message.setStatus(ChatConstant.PENDING);
                    message.setCreatedAt(l);
                    mRealm.commitTransaction();

                    AlertUtils.showToast(mContext, mContext.getString(R.string.sending) );
                    sendImage(listMessage, message);
                }, throwable -> ErrorUtil.handleException(mContext, throwable)));
    }

    public String getUrlImage() {
        return urlImage;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
