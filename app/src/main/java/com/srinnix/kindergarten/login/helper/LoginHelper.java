package com.srinnix.kindergarten.login.helper;

import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;
import com.srinnix.kindergarten.request.remote.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 3/3/2017.
 */

public class LoginHelper {
    private ApiService mApi;

    public LoginHelper() {
        mApi = RetrofitClient.getApiService();
    }

    public void login(String email, String password, LoginHelperListener mListener) {
        mApi.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (mListener != null) {
                                mListener.onResponseSuccess(response);
                            }
                        },
                        throwable -> {
                            if (mListener != null) {
                                mListener.onResponseFail(throwable);
                            }
                        });
    }

    public interface LoginHelperListener {

        void onResponseSuccess(ApiResponse<DataLogin> dataLogin);

        void onResponseFail(Throwable throwable);
    }
}
