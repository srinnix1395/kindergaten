package com.srinnix.kindergarten.clazz.helper;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
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

    public Single<ApiResponse<ArrayList<Class>>> getListClass() {
        return mApiService.getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ClassResponse>> getClassInfo(String classId, boolean isTeacher) {

        return mApiService.getClassInfo(classId, isTeacher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<Image>>> getClassImage(String classId, long time) {
        return mApiService.getImageClass(classId, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<Timetable>> getTimeTable(String time) {
        return mApiService.getTimeTable(time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<StudyTimetable>>> getStudyTimeTable(String time, String group) {
        return mApiService.getStudyTimeTable(time, group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<Image>>> postImage(String token, String classId, ArrayList<MediaLocal> mListImage) {
        List<MultipartBody.Part> listFile;
        listFile = new ArrayList<>();
        for (MediaLocal mediaLocal : mListImage) {
            MultipartBody.Part part = prepareFilePart(mediaLocal);
            if (part != null) {
                listFile.add(part);
            }
        }

        RequestBody bodyClassId = RequestBody.create(MediaType.parse("text/plain"), classId);

        return mApiService.insertImages(token, bodyClassId, listFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private MultipartBody.Part prepareFilePart(MediaLocal mediaLocal) {
        File file = new File(mediaLocal.getPath());

        if (!file.exists()) {
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }
}
