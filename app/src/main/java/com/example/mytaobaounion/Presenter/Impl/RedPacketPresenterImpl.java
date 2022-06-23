package com.example.mytaobaounion.Presenter.Impl;

import android.view.LayoutInflater;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.RedPacketContent;
import com.example.mytaobaounion.Presenter.IRedPacketPresenter;
import com.example.mytaobaounion.Utils.Constant;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.Utils.UrlUtils;
import com.example.mytaobaounion.View.IRedPacketCallback;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RedPacketPresenterImpl implements IRedPacketPresenter {
    private IRedPacketCallback redPacketCallback;

    private static final int DEFAULT_PAGE=1;
    private int currentPage=DEFAULT_PAGE;
    private final API api;

    private boolean isLoading=false;

    public RedPacketPresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(API.class);
    }


    @Override
    public void registerViewCallback(IRedPacketCallback callback) {
        redPacketCallback=callback;
    }

    @Override
    public void unregistViewCallback(IRedPacketCallback callback) {
        redPacketCallback=null;
    }


    @Override
    public void getRedPacketContent() {
        //防止加载的时候多次加载
        if(isLoading){
            return;
        }
        isLoading=true;

        if(redPacketCallback!=null){
            redPacketCallback.onLoading();
        }

        String contentUrl = UrlUtils.getRedPacketContentUrl(currentPage);
        Call<RedPacketContent> call = api.getRedPacketContent(contentUrl);
        call.enqueue(new Callback<RedPacketContent>() {
            @Override
            public void onResponse(Call<RedPacketContent> call, Response<RedPacketContent> response) {
                isLoading=false;
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    RedPacketContent redPacketContent = response.body();

                    //把之前的步骤封装了，本质上一样
                    onLoadSuccess(redPacketContent);
                }
                else{
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<RedPacketContent> call, Throwable t) {
                LogUtils.i(RedPacketPresenterImpl.class,t.toString());
                onLoadError();
            }
        });
    }

    private void onLoadSuccess(RedPacketContent redPacketContent) {
        if(redPacketCallback != null) {
            try {
                if(isEmpty(redPacketContent)) {
                    onEmpty();
                } else {
                    redPacketCallback.onContentLoaded(redPacketContent);
                }
            } catch(Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }


    }

    private void onEmpty() {
        if(redPacketCallback != null) {
            redPacketCallback.onEmpty();
        }

    }

    private boolean isEmpty(RedPacketContent redPacketContent) {
        int size=redPacketContent.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size==0;
    }

    private void onLoadError() {
        isLoading = false;
        if (redPacketCallback != null) {
            redPacketCallback.onNetworkError();
        }
    }


    @Override
    public void reload() {
        this.getRedPacketContent();
    }


    @Override
    public void loadMore() {
        if(isLoading){
            return;
        }
        isLoading=true;

        currentPage++;
        String contentUrl = UrlUtils.getRedPacketContentUrl(currentPage);
        Call<RedPacketContent> call = api.getRedPacketContent(contentUrl);
        call.enqueue(new Callback<RedPacketContent>() {
            @Override
            public void onResponse(Call<RedPacketContent> call, Response<RedPacketContent> response) {
                isLoading=false;
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    RedPacketContent redPacketContent = response.body();
                    onLoadMoreSuccess(redPacketContent);
                }
                else{
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<RedPacketContent> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }

    private void onLoadMoreError() {
        currentPage--;
        isLoading=false;
        if(redPacketCallback!=null){
            redPacketCallback.onLoadMoreError();
        }
    }

    private void onLoadMoreSuccess(RedPacketContent redPacketContent) {
        if(redPacketCallback != null) {
            if(isEmpty(redPacketContent)) {
                currentPage--;                                                                      //为空--，只在loadMore相关时使用
                redPacketCallback.onLoadMoreEmpty();
            } else {
                redPacketCallback.onContentLoadMore(redPacketContent);
            }
        }
    }
}
