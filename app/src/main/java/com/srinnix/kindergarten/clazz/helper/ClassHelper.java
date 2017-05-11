package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.request.model.ClassResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by anhtu on 3/16/2017.
 */

public class ClassHelper extends BaseHelper {

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

    public void getClassImage(String classId, long time, ResponseListener<ArrayList<Image>> listener) {
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

    public void getStudyTimeTable(String time, String group, ResponseListener<ArrayList<StudyTimetable>> listener) {
        if (listener == null) {
            return;
        }

        mDisposable.add(mApiService.getStudyTimeTable(time, group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    public void postImage(String token, String classId, ArrayList<ImageLocal> mListImage, ResponseListener<ArrayList<Image>> listener) {
        if (listener == null) {
            return;
        }

        List<MultipartBody.Part> listFile;
        listFile = new ArrayList<>();
        for (ImageLocal imageLocal : mListImage) {
            MultipartBody.Part part = prepareFilePart(imageLocal);
            if (part != null) {
                listFile.add(part);
            }
        }

        RequestBody bodyClassId = RequestBody.create(MediaType.parse("text/plain"), classId);

        mDisposable.add(mApiService.insertImages(token, bodyClassId, listFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    private MultipartBody.Part prepareFilePart(ImageLocal imageLocal) {
        File file = new File(imageLocal.getPath());

        if (!file.exists()) {
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }
}
