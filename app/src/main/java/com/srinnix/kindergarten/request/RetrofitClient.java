package com.srinnix.kindergarten.request;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.remote.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2/13/2017.
 */

public class RetrofitClient {
    private static Retrofit sRetrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    public static ApiService getApiService(){
        return getClient(AppConstant.BASE_URL).create(ApiService.class);
    }
}
