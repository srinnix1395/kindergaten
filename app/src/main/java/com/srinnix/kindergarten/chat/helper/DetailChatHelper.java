package com.srinnix.kindergarten.chat.helper;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.DebugLog;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Administrator on 3/3/2017.
 */

public class DetailChatHelper {

    private ApiService mApi;
    private CompositeDisposable mDisposable;

    public DetailChatHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
        mApi = RetrofitClient.getApiService();
    }

    public void getPreviousMessage(Realm realm, String conversationID
            , ArrayList<Object> listMessage, String token, DetailChatHelperListener listener) {

        long timeFirstMessage;
        if (listMessage.size() == 1) {
            timeFirstMessage = System.currentTimeMillis();
        } else {
            timeFirstMessage = ((Message) listMessage.get(1)).getCreatedAt();
        }

        Disposable disposable = Observable.concat(getMessageDB(realm, conversationID, timeFirstMessage),
                getMessageApi(realm, token, conversationID, timeFirstMessage))
                .filter(arrayList -> arrayList.size() > 0)
                .first(new ArrayList<>())
                .compose(applyScheduler())
                .subscribe(o -> {
                    if (listener != null) {
                        listener.onLoadMessageSuccessfully(listMessage);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onLoadMessageFail(throwable);
                    }
                });
        mDisposable.add(disposable);
    }

    public <T> SingleTransformer<T, T> applyScheduler(){
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<ArrayList<Message>> getMessageApi(Realm realm, String token, String conversationID, long timeFirstMessage) {
        return mApi.getHistoryMessage(token, conversationID, timeFirstMessage)
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
                    if (messages != null) {
                        saveMessage(realm, messages);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private void saveMessage(Realm realm, ArrayList<Message> messages) {
        realm.beginTransaction();
        realm.copyToRealm(messages);
        realm.commitTransaction();
    }

    private Observable<ArrayList<Message>> getMessageDB(Realm realm, String conversationID, long timeFirstMessage) {
        return Observable.fromCallable(() -> realm.where(Message.class)
                .equalTo("conversationId", conversationID)
                .lessThan("created_at", timeFirstMessage)
                .findAllSorted("created_at", Sort.DESCENDING))
                .filter(messages -> messages.size() > 0)
                .map(messages -> {
                    ArrayList<Message> arrayList = new ArrayList<>();
                    int size = messages.size() > ChatConstant.ITEM_MESSAGE_PER_PAGE ? ChatConstant.ITEM_MESSAGE_PER_PAGE : messages.size();
                    for (int i = 0; i < size; i++) {
                        arrayList.add(0, messages.get(i));
                    }
                    return arrayList;
                })
                .subscribeOn(Schedulers.io());
    }

    public interface DetailChatHelperListener {
        void onLoadMessageSuccessfully(ArrayList<Object> arrayList);

        void onLoadMessageFail(Throwable throwable);
    }
}
