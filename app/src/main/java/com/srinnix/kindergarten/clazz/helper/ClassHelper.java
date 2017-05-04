package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.ImageResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/16/2017.
 */

public class ClassHelper extends BaseHelper{

    public ClassHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public void getListClass(ResponseListener<ArrayList<Class>> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApiService.getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void getClassInfo(String classId, boolean isTeacher, ResponseListener<ClassResponse> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(
                mApiService.getClassInfo(classId, isTeacher)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(listener::onFinally)
                        .subscribe(listener::onSuccess, listener::onFail)
        );
    }

    public void getClassImage(String classId, long time, ResponseListener<ImageResponse> listener) {
        if (listener == null) {
            return;
        }
        mDisposable.add(mApiService.getImageClass(classId, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void getTimeTable(String time, ResponseListener<Timetable> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getTimeTable(time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void getStudyTimeTable(String time, String group, ResponseListener<StudyTimetable> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getStudyTimeTable(time, group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }
}
