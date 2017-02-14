package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2/13/2017.
 */

public interface ApiService {

    @POST(AppConstant.API_LOGIN)
    @FormUrlEncoded
    Observable<LoginResponse> login(@Field("email") String email,
                                    @Field("password") String password);
}
