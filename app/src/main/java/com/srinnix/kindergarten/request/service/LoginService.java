package com.srinnix.kindergarten.request.service;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by anhtu on 2/12/2017.
 */

public interface LoginService {
    @POST("/user/login")
    Observable<Object> login();
}
