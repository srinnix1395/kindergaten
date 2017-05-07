package com.srinnix.kindergarten.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.request.converter.ContactDeserializer;
import com.srinnix.kindergarten.request.remote.ApiService;

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
                    .create();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static ApiService getApiService() {
        return getClient(AppConstant.BASE_URL).create(ApiService.class);
    }
}
