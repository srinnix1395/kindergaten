package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.LikeResponse;
import com.srinnix.kindergarten.request.model.PostResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 3/3/2017.
 */

public class BulletinBoardHelper extends BaseHelper {

    public BulletinBoardHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public Single<ApiResponse<LikeResponse>> likePost(String token, String idUser, String idPost,
                                                      String name, String image, int accountType) {
        return mApiService.likePost(token, idUser, name, image, accountType, idPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<LikeResponse>> unlikePost(String token, String idUser, String idPost) {
        return mApiService.unlikePost(token, idUser, idPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Object> getPostSignIn(String token, String idUser, long timePrevPost) {
        return mApiService.getListPostMember(token, idUser, timePrevPost)
                .flatMap(response -> {
                    if (response == null) {
                        return Single.error(new NullPointerException());
                    }

                    if (response.result == ApiResponse.RESULT_OK) {
                        ArrayList<String> listLikes = response.getData().getListLikes();
                        ArrayList<Post> listPost = response.getData().getListPost();
                        int i = 0;
                        for (Post post : listPost) {
                            if (i == listLikes.size()) {
                                break;
                            }
                            if (listLikes.contains(post.getId())) {
                                post.setUserLike(true);
                                i++;
                            }
                        }
                        return Single.just(listPost);
                    }

                    return Single.just(response.error);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<Object> getPostUnsignIn(long timePrevPost) {
        return mApiService.getListPostGuest(timePrevPost)
                .flatMap(response -> {
                    if (response == null) {
                        return Single.error(new NullPointerException());
                    }

                    if (response.result == ApiResponse.RESULT_OK) {
                        return Single.just(response.getData());
                    }

                    return Single.just(response.error);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<LikeModel>>> getListNumberLike(String idPost, long timePrevLike) {
        return mApiService.getListNumberLike(idPost, timePrevLike)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<PostResponse>> getNewPost(boolean userSignedIn, String token, String userId, long timePrev) {
        return mApiService.getNewPost(token, userSignedIn, userId, timePrev)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<PostResponse>> getImportantPost(String token, String userId, long minTime) {
        return mApiService.getImportantPost(token, userId, minTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<ArrayList<Comment>>> getComment(String idPost, long timeLastComment) {
        return mApiService.getComment(idPost, timeLastComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<Comment>> sendComment(String token, String idPost, String idUser, String name, String image,
                                                    int accountType, String comment) {
        return mApiService.insertComment(token, idPost, idUser, name, image, accountType, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<Post>> getDetailPost(String token, String idPost, String idUser) {
        return mApiService.getDetailPost(token, idPost, idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<Post>> post(String token, String content, ArrayList<ImageLocal> listImage, int notiType, int notificationRange) {
        List<MultipartBody.Part> listFile = null;
        if (!listImage.isEmpty()) {
            listFile = new ArrayList<>();
            for (ImageLocal imageLocal : listImage) {
                MultipartBody.Part part = RetrofitClient.prepareFilePart(imageLocal.getPath());
                if (part != null) {
                    listFile.add(part);
                }
            }
        }

        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content.trim());
        RequestBody notiTypeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(notiType));
        RequestBody notiRangeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(notificationRange));

        return mApiService.insertPost(token, contentBody, listFile, notiTypeBody, notiRangeBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
