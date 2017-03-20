package com.srinnix.kindergarten.service;

import android.content.Context;

import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.SharedPreUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/4/2017.
 */

public class UpdateFirebaseRegId {
    public static void updateRegId(Context context, Disposable disposable, String token, String id, String regID) {
        ApiService apiService = RetrofitClient.getApiService();

        disposable = apiService.updateRegId(token, id, regID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response == null) {
                        DebugLog.e(ErrorConstant.RESPONSE_NULL);
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(context, response.error);
                        return;
                    }

                    SharedPreUtils.getInstance(context).setHasDeviceToken(true);
                    DebugLog.i("Update regID successfully");
                }, throwable -> DebugLog.e(throwable.getMessage()));
    }
}
