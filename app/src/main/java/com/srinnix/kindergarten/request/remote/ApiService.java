package com.srinnix.kindergarten.request.remote;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.HealthTotal;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.BulletinResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.model.LoginResponse;
import com.srinnix.kindergarten.request.model.PostResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
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
    Single<ApiResponse<LoginResponse>> login(@Field("email") String email,
                                             @Field("password") String password);

    @POST(AppConstant.API_UPDATE_REG_ID)
    @FormUrlEncoded
    Single<ApiResponse<Boolean>> updateRegId(@Header("x-access-token") String header,
                                             @Field("_id") String id,
                                             @Field("reg_id") String regId);

    @POST(AppConstant.API_RESET_PASSWORD)
    @FormUrlEncoded
    Single<ApiResponse<Boolean>> resetPassword(@Header("x-access-token") String token,
                                               @Field("_id_user") String id,
                                               @Field("email") String email,
                                               @Field("new_password") String newPassword,
                                               @Field("new_password_encrypted") String newPasswordEncrypted);

    @GET(AppConstant.API_GET_ACCOUNT_INFO)
    Single<ApiResponse<User>> getAccountInfo(@Header("x-access-token") String token,
                                             @Query("_id_user") String idUser,
                                             @Query("account_type") int accountType);

    @POST(AppConstant.API_CHANGE_PASSWORD)
    @FormUrlEncoded
    Single<ApiResponse<Boolean>> changePassword(@Header("x-access-token") String token,
                                                @Field("_id_user") String idUser,
                                                @Field("old_password") String oldPassword,
                                                @Field("new_password") String newPassword);

    @Multipart
    @POST(AppConstant.API_UPDATE_INFO)
    Single<ApiResponse<User>> updateInfo(@Header("x-access-token") String token,
                                         @Part("_id_user") RequestBody idUser,
                                         @Part("account_type") RequestBody accountType,
                                         @Part("gender") RequestBody gender,
                                         @Part("DOB") RequestBody dob,
                                         @Part("phone_number") RequestBody phoneNumber,
                                         @Part MultipartBody.Part image);

    //USER END

    //POST START
    @GET(AppConstant.API_GET_POST_MEMBER)
    Single<ApiResponse<BulletinResponse>> getListPostMember(@Header("x-access-token") String token,
                                                            @Query("_id_user") String idUser,
                                                            @Query("last_id") String id);

    @GET(AppConstant.API_GET_POST_GUEST)
    Single<ApiResponse<ArrayList<Post>>> getListPostGuest(@Query("last_id") String id);

    @POST(AppConstant.API_LIKE_POST)
    @FormUrlEncoded
    Single<ApiResponse<LikeResponse>> likePost(@Header("x-access-token") String token,
                                               @Field("_id_user") String idUser,
                                               @Field("name_user") String name,
                                               @Field("image_user") String image,
                                               @Field("account_type") int accountType,
                                               @Field("_id_post") String idPost);

    @POST(AppConstant.API_UNLIKE_POST)
    @FormUrlEncoded
    Single<ApiResponse<LikeResponse>> unlikePost(@Header("x-access-token") String token,
                                                 @Field("_id_user") String idUser,
                                                 @Field("_id_post") String idPost);

    @GET(AppConstant.API_GET_LIST_NUMBER_LIKE)
    Single<ApiResponse<ArrayList<LikeModel>>> getListNumberLike(@Query("_id_post") String id,
                                                                @Query("id_prev_like") String timePrevLike);

    @GET(AppConstant.API_GET_COMMENT)
    Single<ApiResponse<ArrayList<Comment>>> getComment(@Query("_id_post") String idPost,
                                                       @Query("id_last_comment") String timeLastComment);

    @POST(AppConstant.API_INSERT_COMMENT)
    @FormUrlEncoded
    Single<ApiResponse<Comment>> insertComment(@Header("x-access-token") String token,
                                               @Field("_id_post") String idPost,
                                               @Field("_id_user") String idUser,
                                               @Field("name_user") String name,
                                               @Field("image_user") String image,
                                               @Field("account_type") int accountType,
                                               @Field("comment") String comment);

    @GET(AppConstant.API_GET_NEW_POST)
    Single<ApiResponse<BulletinResponse>> getNewPost(@Header("x-access-token") String token,
                                                     @Query("is_user_signed_in") boolean isUserSignIn,
                                                     @Query("_id_user") String userId,
                                                     @Query("prev_id") String idLastPost);

    @GET(AppConstant.API_GET_IMPORTANT_POST)
    Single<ApiResponse<BulletinResponse>> getImportantPost(@Header("x-access-token") String token,
                                                           @Query("_id_user") String userId,
                                                           @Query("min_id") String minId);

    @Multipart
    @POST(AppConstant.API_INSERT_POST)
    Single<ApiResponse<PostResponse>> insertPost(@Header("x-access-token") String token,
                                                 @Part("content") RequestBody content,
                                                 @Part List<MultipartBody.Part> listImage,
                                                 @Part("notification_type") RequestBody notiType,
                                                 @Part("notification_range") RequestBody notiRangeBody,
                                                 @Part("is_now") RequestBody isScheduleBody,
                                                 @Part("year") RequestBody yearBody,
                                                 @Part("month") RequestBody monthBody,
                                                 @Part("day") RequestBody dayBody,
                                                 @Part("hour") RequestBody hourBody,
                                                 @Part("minute") RequestBody minuteBody);

    @GET(AppConstant.API_GET_DETAIL_POST)
    Single<ApiResponse<Post>> getDetailPost(@Header("x-access-token") String token,
                                            @Query("_id_post") String idPost,
                                            @Query("_id_user") String idUser);

    //POST END

    //CLASS START
    @GET(AppConstant.API_GET_LIST_CLASS)
    Single<ApiResponse<ArrayList<Class>>> getListClass();

    @GET(AppConstant.API_GET_DETAIL_CLASS)
    Single<ApiResponse<ClassResponse>> getClassInfo(@Query("_id_class") String classId,
                                                    @Query("is_teacher") boolean isTeacher);

    @GET(AppConstant.API_GET_INFO_TEACHER)
    Single<ApiResponse<Teacher>> getTeacherInfo(@Query("_id_teacher") String teacherId);

    @GET(AppConstant.API_GET_IMAGE_CLASS)
    Single<ApiResponse<ArrayList<Image>>> getImageClass(@Query("_id_class") String classId,
                                                        @Query("time_prev_image") long timePrevImage);

    @GET(AppConstant.API_GET_TIMETABLE)
    Single<ApiResponse<Timetable>> getTimeTable(@Query("time") String time);

    @GET(AppConstant.API_GET_STUDY_TIMETABLE)
    Single<ApiResponse<ArrayList<StudyTimetable>>> getStudyTimeTable(@Query("time") String time,
                                                                     @Query("group") String group);

    @Multipart
    @POST(AppConstant.API_INSERT_IMAGE)
    Single<ApiResponse<ArrayList<Image>>> insertImages(@Header("x-access-token") String token,
                                                       @Part("_id_class") RequestBody classID,
                                                       @Part List<MultipartBody.Part> listImage);
    //CLASS END

    //CHILDREN START
    @GET(AppConstant.API_GET_INFO_CHILDREN)
    Single<ApiResponse<Child>> getInfoChildren(@Header("x-access-token") String header,
                                               @Query("_id_child") String id);

    @GET(AppConstant.API_GET_HEALTH_INDEX_CHILDREN)
    Single<ApiResponse<ArrayList<HealthTotal>>> getTimelineChildren(@Header("x-access-token") String token,
                                                                    @Query("_id_child") String childId,
                                                                    @Query("time_prev") long timePrev);


    @GET(AppConstant.API_GET_LIST_CHILDREN)
    Single<ApiResponse<ArrayList<Child>>> getListChildren(@Header("x-access-token") String token,
                                                          @Query("_id_class") String classId);
    //CHILDREN END


    @GET(AppConstant.API_GET_MESSAGE)
    Observable<ApiResponse<ArrayList<Message>>> getHistoryMessage(@Header("x-access-token") String token,
                                                                  @Query("conversation_id") String conversationID,
                                                                  @Query("time_first_message") long timeFirstMessage);
}
