package com.srinnix.kindergarten.children.helper;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthTotal;
import com.srinnix.kindergarten.request.model.ApiResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/8/2017.
 */

public class ChildrenHelper extends BaseHelper {

    public ChildrenHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public Single<ApiResponse<Child>> getInfoChildren(String token, String id) {
        return mApiService.getInfoChildren(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<Child>>> getListChildrenTeacher(String token, String idClass) {
        return mApiService.getListChildren(token, idClass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ApiResponse<ArrayList<HealthTotal>>> getTimelineChildren(String token, String idChildren, long timePrev) {
        return mApiService.getTimelineChildren(token, idChildren, timePrev)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
