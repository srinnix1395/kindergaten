package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.DataLogin;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.model.PostResponse;

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

    //POST START
    @GET(AppConstant.API_GET_POST_MEMBER)
    Observable<ApiResponse<PostResponse>> getListPostMember(@Header("x-access-token") String token,
                                                            @Query("_id_user") String idUser,
                                                            @Query("time_prev_post") long time);

    @GET(AppConstant.API_GET_POST_GUEST)
    Observable<ApiResponse<ArrayList<Post>>> getListPostGuest(@Query("time_prev_post") long time);

    @POST(AppConstant.API_LIKE_POST)
    @FormUrlEncoded
    Observable<ApiResponse<LikeResponse>> likePost(@Header("x-access-token") String token,
                                                   @Field("_id_user") String idUser,
                                                   @Field("_id_post") String idPost);

    @POST(AppConstant.API_UNLIKE_POST)
    @FormUrlEncoded
    Observable<ApiResponse<LikeResponse>> unlikePost(@Header("x-access-token") String token,
                                                     @Field("_id_user") String idUser,
                                                     @Field("_id_post") String idPost);
    //POST END

    @GET(AppConstant.API_GET_LIST_CLASS)
    Observable<ApiResponse<ArrayList<Class>>> getListClass();

    @GET(AppConstant.API_GET_DETAIL_CLASS)
    Observable<ApiResponse> getDetailClass();

    @GET("chat/message")
    Observable<ArrayList<Message>> getHistoryMessage(@Header("x-access-token") String token,
                                                     @Query("time_first_message") long timeFirstMessage);


}
