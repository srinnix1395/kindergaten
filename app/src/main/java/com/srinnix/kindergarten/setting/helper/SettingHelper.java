package com.srinnix.kindergarten.setting.helper;

import android.support.v4.util.Pair;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.model.ApiResponse;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by anhtu on 4/20/2017.
 */

public class SettingHelper extends BaseHelper {

    public SettingHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public Single<Pair<ApiResponse<User>, ArrayList<Child>>> getAccountInfo(String token, String idUser, int accountType) {

        return Single.zip(getUserInfo(token, idUser, accountType), getChildren(), Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<ApiResponse<User>> getUserInfo(String token, String idUser, int accountType) {
        return mApiService.getAccountInfo(token, idUser, accountType);

    }

    private Single<ArrayList<Child>> getChildren() {
        return Single.fromCallable(() -> {
            RealmResults<Child> results = Realm.getDefaultInstance().where(Child.class).findAll();
            return new ArrayList<>(results.subList(0, results.size()));
        });
    }
}
