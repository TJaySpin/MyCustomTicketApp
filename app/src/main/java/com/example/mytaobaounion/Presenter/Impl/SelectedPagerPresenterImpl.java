package com.example.mytaobaounion.Presenter.Impl;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.SelectedContent;
import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;
import com.example.mytaobaounion.Presenter.ISelectedPagePresenter;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.Utils.UrlUtils;
import com.example.mytaobaounion.View.ISelectedPageCallback;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagerPresenterImpl implements ISelectedPagePresenter {

    private ISelectedPageCallback mViewCallback;
    private final API api;

    //设置成成员变量，用在reload里
    private SelectedPagerCatagories.DataDTO curCatagoryItem;

    public SelectedPagerPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(API.class);
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        mViewCallback = callback;
    }

    @Override
    public void unregistViewCallback(ISelectedPageCallback callback) {
        mViewCallback=null;
    }



    @Override
    public void getCatagories() {
        //在精选界面，这部分（左边）先加载出来，所以需要Loading，右边分类那部分不需要Loading
        if(mViewCallback!=null){
            mViewCallback.onLoading();
        }

        Call<SelectedPagerCatagories> call = api.getSelectedCatagories();
        call.enqueue(new Callback<SelectedPagerCatagories>() {
            @Override
            public void onResponse(Call<SelectedPagerCatagories> call, Response<SelectedPagerCatagories> response) {
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    SelectedPagerCatagories selectedPagerCatagories = response.body();
                    if(mViewCallback!=null){
                        mViewCallback.onCatagoriesLoad(selectedPagerCatagories);
                    }
                }
                else{
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPagerCatagories> call, Throwable t) {
                onLoadedError();
            }
        });
    }



    @Override
    public void getContentByCategoryId(SelectedPagerCatagories.DataDTO item) {
        curCatagoryItem=item;
        if(curCatagoryItem!=null) {
            int CatagoryId = item.getFavorites_id();
            String contentUrl = UrlUtils.getSelectedContentUrl(CatagoryId);
            Call<SelectedContent> call = api.getSelectedContent(contentUrl);
            call.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int code=response.code();
                    if(code==HttpsURLConnection.HTTP_OK){
                        SelectedContent selectedContent = response.body();

                        //其实这里和CatagoryPagerPresenterImpl里最好都进行OnEmpty()判断，方法和HomePresenterImpl一样
                        if(mViewCallback!=null){
                            mViewCallback.onContentLoaded(selectedContent);
                        }
                    }
                    else{
                        onLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                    onLoadedError();
                }
            });
        }
    }


    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }


    @Override
    public void reload() {
        //重新加载，断网回复后必须加载左边的item，如果加载右边的content会出不来内容，因为右边的content是根据左边加载出来的
        getCatagories();
    }
}
