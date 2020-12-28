package com.o2o.action.server.util;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    //객체생성
    Retrofit retrofit = new Retrofit.Builder()
            //서버 url설정
            .baseUrl(TaskServer.ip)
            .client(okHttpClient)
            //데이터 파싱 설정
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //객체정보 반환
            .build();

    public RestAPI apiService = retrofit.create(RestAPI.class);
}