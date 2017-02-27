package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2/13/2017.
 */

public interface ApiService {
    @POST(AppConstant.API_LOGIN)
    @FormUrlEncoded
    Observable<ApiResponse<DataLogin>> login(@Field("email") String email,
                                             @Field("password") String password);

    @POST(AppConstant.API_GET_POST)
    @FormUrlEncoded
    Observable<ApiResponse<ArrayList<Post>>> getListPost(@Header("token") String token,
                                                         @Field("time_prev_post") long time);

    @GET(AppConstant.API_GET_LIST_CLASS)
    Observable<ApiResponse<ArrayList<Class>>> getListClass();

    @GET(AppConstant.API_GET_DETAIL_CLASS)
    Observable<ApiResponse> getDetailClass();

    @GET("demo")
    Observable<Object> demo(@Query("demo") String a);
}
