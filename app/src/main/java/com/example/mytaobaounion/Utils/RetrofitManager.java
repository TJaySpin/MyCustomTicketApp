package com.example.mytaobaounion.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager mretrofitManager=new RetrofitManager();
    private Retrofit mRetrofit;

    public static RetrofitManager getInstance(){
        return mretrofitManager;
    }

    private RetrofitManager(){
        mRetrofit=new Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }

}