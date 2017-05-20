package com.srinnix.kindergarten.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.request.converter.ContactDeserializer;
import com.srinnix.kindergarten.request.converter.ImageDeserializer;
import com.srinnix.kindergarten.request.remote.ApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2/13/2017.
 */

public class RetrofitClient {
    private static Retrofit sRetrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (sRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Contact.class, new ContactDeserializer())
                    .registerTypeAdapter(Image.class, new ImageDeserializer())
                    .create();

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .build();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static ApiService getApiService() {
        return getClient(AppConstant.BASE_URL).create(ApiService.class);
    }

    public static MultipartBody.Part prepareFilePart(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
    }
}
