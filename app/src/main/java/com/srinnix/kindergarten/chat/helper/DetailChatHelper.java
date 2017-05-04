package com.srinnix.kindergarten.chat.helper;

import android.content.Context;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Administrator on 3/3/2017.
 */

public class DetailChatHelper extends BaseHelper{

    public DetailChatHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public void getPreviousMessage(Context mContext, String conversationID, ArrayList<Object> listMessage,
                                   String token, DetailChatHelperListener listener) {

        long timeFirstMessage;
        if (listMessage.isEmpty()) {
            timeFirstMessage = System.currentTimeMillis();
        } else if (listMessage.get(0) instanceof Message) {
            timeFirstMessage = ((Message) listMessage.get(0)).getCreatedAt();
        } else if (listMessage.get(1) instanceof Message){
            timeFirstMessage = ((Message) listMessage.get(1)).getCreatedAt();
        }  else {
            timeFirstMessage = ((Message) listMessage.get(2)).getCreatedAt();
        }

        Disposable disposable = Observable.concat(getMessageDB(conversationID, timeFirstMessage),
                getMessageApi(mContext, token, conversationID, timeFirstMessage))
                .filter(arrayList -> arrayList.size() > 0)
                .first(new ArrayList<>())
                .map(messages -> {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    for (int i = 0, size = messages.size(); i < size; i++) {
                        if (i == 0) {
                            arrayList.add(messages.get(0).getCreatedAt());
                        }
                        arrayList.add(messages.get(i));
                        if (i + 1 <= size - 1 && messages.get(i + 1).getCreatedAt() - messages.get(i).getCreatedAt() > AppConstant.TIME_BETWEEN_2_MESSAGE) {
                            arrayList.add(messages.get(i + 1).getCreatedAt());
                        }
                    }
                    return arrayList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageArrayList -> {
                    if (listener != null) {
                        listener.onLoadMessageSuccessfully(messageArrayList);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onLoadMessageFail(throwable);
                    }
                });
        mDisposable.add(disposable);
    }

    private Observable<ArrayList<Message>> getMessageApi(Context mContext, String token, String conversationID, long timeFirstMessage) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            return Observable.just(new ArrayList<>());
        }

        return mApiService.getHistoryMessage(token, conversationID, timeFirstMessage)
                .map(response -> {
                    if (response == null) {
                        DebugLog.e(ErrorConstant.RESPONSE_NULL);
                        return null;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        DebugLog.i(response.error.message);
                        return null;
                    }

                    return response.getData();
                })
                .doOnNext(messages -> {
                    if (messages != null && messages.size() > 0) {
                        saveMessage(messages);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private void saveMessage(ArrayList<Message> messages) {
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(realm1 -> realm1.copyToRealm(messages));
//        realm.close();
    }

    private Observable<ArrayList<Message>> getMessageDB(String conversationID, long timeFirstMessage) {
        return Observable.just(Realm.getDefaultInstance().where(Message.class)
                .equalTo("conversationId", conversationID)
                .lessThan("createdAt", timeFirstMessage)
                .findAllSorted("createdAt", Sort.DESCENDING))
                .map(results -> {
                    ArrayList<Message> arrayList = new ArrayList<>();
                    int size = results.size() > ChatConstant.ITEM_MESSAGE_PER_PAGE ? ChatConstant.ITEM_MESSAGE_PER_PAGE : results.size();
                    for (int i = 0; i < size; i++) {
                        arrayList.add(results.get(i));
                    }
                    return arrayList;
                });
    }

    public interface DetailChatHelperListener {
        void onLoadMessageSuccessfully(ArrayList<Object> arrayList);

        void onLoadMessageFail(Throwable throwable);
    }
}
