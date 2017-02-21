package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.request.model.PostResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2/13/2017.
 */

public interface ApiService {
    @POST(AppConstant.API_LOGIN)
    @FormUrlEncoded
    Observable<LoginResponse> login(@Field("email") String email,
                                    @Field("password") String password);

    @POST(AppConstant.API_GET_POST)
    @FormUrlEncoded
    Observable<PostResponse> getListPost(@Header("token") String token,
                                         @Field("page") int page);

//    @GET(AppConstant.API_GET_LIST_CLASS)
//    Observable<ListClassResponse> getListClass(@P)
}
