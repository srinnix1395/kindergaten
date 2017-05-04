package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.HealthTotal;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.ImageResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.request.model.PostResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2/13/2017.
 */

public interface ApiService {
    //USER START
    @POST(AppConstant.API_LOGIN)
    @FormUrlEncoded
    Observable<ApiResponse<LoginResponse>> login(@Field("email") String email,
                                                 @Field("password") String password);

    @POST(AppConstant.API_UPDATE_REG_ID)
    @FormUrlEncoded
    Observable<ApiResponse<Boolean>> updateRegId(@Header("x-access-token") String header,
                                                 @Field("_id") String id,
                                                 @Field("reg_id") String regId);

    @POST(AppConstant.API_RESET_PASSWORD)
    @FormUrlEncoded
    Observable<ApiResponse<Boolean>> resetPassword(@Header("x-access-token") String token,
                                                   @Field("_id_user") String id,
                                                   @Field("email") String email,
                                                   @Field("new_password") String newPassword,
                                                   @Field("new_password_encrypted") String newPasswordEncrypted);

    @GET(AppConstant.API_GET_ACCOUNT_INFO)
    Observable<ApiResponse<User>> getAccountInfo(@Header("x-access-token") String token,
                                                 @Query("_id_user") String idUser);
    //USER END

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
                                                   @Field("name_user") String name,
                                                   @Field("image_user") String image,
                                                   @Field("account_type") int accountType,
                                                   @Field("_id_post") String idPost);

    @POST(AppConstant.API_UNLIKE_POST)
    @FormUrlEncoded
    Observable<ApiResponse<LikeResponse>> unlikePost(@Header("x-access-token") String token,
                                                     @Field("_id_user") String idUser,
                                                     @Field("_id_post") String idPost);

    @GET(AppConstant.API_GET_LIST_NUMBER_LIKE)
    Observable<ApiResponse<ArrayList<LikeModel>>> getListNumberLike(@Query("_id_post") String id,
                                                                    @Query("time_prev_like") long timePrevLike);

    @GET(AppConstant.API_GET_COMMENT)
    Observable<ApiResponse<ArrayList<Comment>>> getComment(@Query("_id_post") String idPost,
                                                           @Query("time_prev_comment") long timeLastComment);

    @POST(AppConstant.API_INSERT_COMMENT)
    @FormUrlEncoded
    Observable<ApiResponse<Comment>> insertComment(@Header("x-access-token") String token,
                                                   @Field("_id_post") String idPost,
                                                   @Field("_id_user") String idUser,
                                                   @Field("name_user") String name,
                                                   @Field("image_user") String image,
                                                   @Field("account_type") int accountType,
                                                   @Field("comment") String comment);

    @GET(AppConstant.API_GET_NEW_POST)
    Observable<ApiResponse<PostResponse>> getNewPost(@Header("x-access-token") String token,
                                                     @Query("is_user_signed_in") boolean isUserSignIn,
                                                     @Query("_id_user") String userId,
                                                     @Query("time") long listId);

    @GET(AppConstant.API_GET_IMPORTANT_POST)
    Observable<ApiResponse<PostResponse>> getImportantPost(@Header("x-access-token") String token,
                                                           @Query("_id_user") String userId,
                                                           @Query("min_time") long minTime);

    @Multipart
    @POST(AppConstant.API_INSERT_POST)
    Observable<ApiResponse<Post>> insertPost(@Header("x-access-token") String token,
                                             @Part("content") RequestBody content,
                                             @Part List<MultipartBody.Part> listImage,
                                             @Part("noti_type") RequestBody notiType,
                                             @Part("noti_range") RequestBody notiRangeBody);

    @GET(AppConstant.API_GET_DETAIL_POST)
    Observable<ApiResponse<Post>> getDetailPost(@Header("x-access-token") String token,
                                                @Query("_id_post") String idPost,
                                                @Query("_id_user") String idUser);

    //POST END

    //CLASS START
    @GET(AppConstant.API_GET_LIST_CLASS)
    Observable<ApiResponse<ArrayList<Class>>> getListClass();

    @GET(AppConstant.API_GET_DETAIL_CLASS)
    Observable<ApiResponse<ClassResponse>> getClassInfo(@Query("_id_class") String classId,
                                                        @Query("is_teacher") boolean isTeacher);

    @GET(AppConstant.API_GET_INFO_TEACHER)
    Observable<ApiResponse<Teacher>> getTeacherInfo(@Query("_id_teacher") String teacherId);

    @GET(AppConstant.API_GET_IMAGE_CLASS)
    Observable<ApiResponse<ImageResponse>> getImageClass(@Query("_id_class") String classId,
                                                         @Query("time_prev_image") long timePrevImage);

    @GET(AppConstant.API_GET_TIMETABLE)
    Observable<ApiResponse<Timetable>> getTimeTable(@Query("time") String time);

    @GET(AppConstant.API_GET_STUDY_TIMETABLE)
    Observable<ApiResponse<StudyTimetable>> getStudyTimeTable(@Query("time") String time,
                                                              @Query("group") String group);
    //CLASS END

    //CHILDREN START
    @GET(AppConstant.API_GET_INFO_CHILDREN)
    Observable<ApiResponse<Child>> getInfoChildren(@Header("x-access-token") String header,
                                                   @Query("_id_child") String id);

    @GET(AppConstant.API_GET_HEALTH_INDEX_CHILDREN)
    Observable<ApiResponse<ArrayList<HealthTotal>>> getTimelineChildren(@Header("x-access-token") String token,
                                                                        @Query("_id_child") String childId,
                                                                        @Query("time_prev") long time);


    @GET(AppConstant.API_GET_LIST_CHILDREN)
    Observable<ApiResponse<ArrayList<Child>>> getListChildren(@Header("x-access-token") String token,
                                                              @Query("_id_class") String classId);
    //CHILDREN END


    @GET(AppConstant.API_GET_MESSAGE)
    Observable<ApiResponse<ArrayList<Message>>> getHistoryMessage(@Header("x-access-token") String token,
                                                                  @Query("conversation_id") String conversationID,
                                                                  @Query("time_first_message") long timeFirstMessage);

}
