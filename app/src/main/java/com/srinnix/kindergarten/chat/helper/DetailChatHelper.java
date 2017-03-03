package com.srinnix.kindergarten.chat.helper;

import com.srinnix.kindergarten.chat.adapter.ChatAdapter;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Administrator on 3/3/2017.
 */

public class DetailChatHelper {

    private ApiService mApi;

    public DetailChatHelper() {
        mApi = RetrofitClient.getApiService();
    }

    public void getPreviousMessage(Realm realm, String conversationID
            , ArrayList<Object> listMessage, ChatAdapter adapter, String token, DetailChatHelperListener listener) {
        long timeFirstMessage;
        if (listMessage.size() == 1) {
            timeFirstMessage = System.currentTimeMillis();
        } else {
            timeFirstMessage = ((Message) listMessage.get(1)).getCreatedAt();
        }

        Observable<ArrayList<Message>> messageDB = Observable.fromCallable(() -> realm.where(Message.class)
                .equalTo("conversationID", conversationID)
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

        Observable<ArrayList<Message>> messageAPI = mApi.getHistoryMessage(token, timeFirstMessage)
                .doOnNext(messages -> {

                })
                .subscribeOn(Schedulers.io());

        Observable.concat(messageDB, messageDB)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (listener != null) {
                        listener.onLoadMessageSuccessfully(listMessage);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onLoadMessageFail(throwable);
                    }
                });

        Observable.defer((Callable<ObservableSource<?>>) () -> {
            RealmResults<Message> results = realm.where(Message.class)
                    .equalTo("conversationID", conversationID)
                    .lessThan("created_at", timeFirstMessage)
                    .findAllSorted("created_at", Sort.DESCENDING);

            ArrayList<Message> arrayList = new ArrayList<>();
            int size = results.size() > ChatConstant.ITEM_MESSAGE_PER_PAGE ? ChatConstant.ITEM_MESSAGE_PER_PAGE : results.size();
            for (int i = 0; i < size; i++) {
                arrayList.add(0, results.get(i));
            }

            if (arrayList.size() == 0) {
                return mApi.getHistoryMessage(token, timeFirstMessage);
            }

            return Observable.just(arrayList);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (listener != null) {
                        listener.onLoadMessageSuccessfully(listMessage);
                    }
                }, throwable -> {
                    if (listener != null) {
                        listener.onLoadMessageFail(throwable);
                    }
                });

    }

    public interface DetailChatHelperListener {
        void onLoadMessageSuccessfully(ArrayList<Object> arrayList);

        void onLoadMessageFail(Throwable throwable);
    }
}
