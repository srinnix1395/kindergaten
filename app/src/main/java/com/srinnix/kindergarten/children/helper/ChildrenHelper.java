package com.srinnix.kindergarten.children.helper;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthTotal;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/8/2017.
 */

public class ChildrenHelper extends BaseHelper{

    public ChildrenHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public void getInfoChildren(String token, String id, ResponseListener<Child> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getInfoChildren(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void getListChildrenTeacher(String token, String idClass, ResponseListener<ArrayList<Child>> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(
                mApiService.getListChildren(token, idClass)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(listener::onFinally)
                        .subscribe(listener::onSuccess, listener::onFail));

    }

    public void getTimelineChildren(String token, String idChildren, long time, ResponseListener<ArrayList<HealthTotal>> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getTimelineChildren(token, idChildren, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
