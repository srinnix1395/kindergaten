package com.srinnix.kindergarten.request.service;

import com.srinnix.kindergarten.constant.AppConstant;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2/13/2017.
 */

public interface ApiService {
    @POST(AppConstant.API_LOGIN)
    Observable<Object> login();
}
