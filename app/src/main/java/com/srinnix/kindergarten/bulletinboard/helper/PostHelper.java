package com.srinnix.kindergarten.bulletinboard.helper;

import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostHelper {
    private ApiService mApi;
    private CompositeDisposable mDisposable;

    public PostHelper(CompositeDisposable mDisposable) {
        mApi = RetrofitClient.getApiService();
        this.mDisposable = mDisposable;
    }

    public void post(String token, String content, ArrayList<ImageLocal> listImage, int notiType, int notificationRange, ResponseListener<Post> listener) {
        if (listener == null) {
            return;
        }

        List<MultipartBody.Part> listFile = null;
        if (!listImage.isEmpty()) {
            listFile = new ArrayList<>();
            for (ImageLocal imageLocal : listImage) {
                MultipartBody.Part part = prepareFilePart("image", imageLocal);
                if (part != null) {
                    listFile.add(part);
                }
            }
        }

        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content.trim());
        RequestBody notiTypeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(notiType));
        RequestBody notiRangeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(notificationRange));

        mDisposable.add(mApi.insertPost(token, contentBody, listFile, notiTypeBody, notiRangeBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(listener::onFinally)
                .subscribe(listener::onSuccess, listener::onFail));
    }

    private MultipartBody.Part prepareFilePart(String partName, ImageLocal imageLocal) {
        File file = new File(imageLocal.getPath());

        if (!file.exists()) {
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }
}
