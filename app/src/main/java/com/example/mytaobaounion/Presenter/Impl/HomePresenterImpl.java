package com.example.mytaobaounion.Presenter.Impl;

import androidx.annotation.NonNull;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.Presenter.IHomePresenter;
import com.example.mytaobaounion.UI.Fragment.HomeFragment;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.View.IHomeCallback;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mCallback=null;

    @Override
    public void getCatagories() {
        if(mCallback!=null){
            mCallback.onLoading();
        }

        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        Retrofit retrofit = retrofitManager.getRetrofit();
        API api = retrofit.create(API.class);
        Call<Catagories> call = api.getCatagories();
        call.enqueue(new Callback<Catagories>() {
            @Override
            public void onResponse(@NonNull Call<Catagories> call, @NonNull Response<Catagories> response) {
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    Catagories catagories = response.body();
                    if(mCallback!=null){
                        if(catagories==null||catagories.getData().size()==0){
                            mCallback.onEmpty();
                        }
                        else{
                            mCallback.onCatagoriesLoad(catagories);
                        }
                    }
                }
                else{
                    if(mCallback!=null){
                        mCallback.onNetworkError();
                    }
                    LogUtils.w(HomePresenterImpl.class,"请求失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Catagories> call, @NonNull Throwable throwable) {
                if(mCallback!=null){
                    mCallback.onNetworkError();
                }
                LogUtils.e(HomePresenterImpl.class,"请求错误"+throwable);
            }
        });
    }


    @Override
    public void registerViewCallback(IHomeCallback callback) {
        mCallback=callback;
    }

    @Override
    public void unregistViewCallback(IHomeCallback iHomeCallback) {
        mCallback=null;
    }
}