package com.example.mytaobaounion.Presenter.Impl;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Presenter.ICatagoryPagerPresenter;
import com.example.mytaobaounion.UI.Fragment.HomeFragment;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.Utils.UrlUtils;
import com.example.mytaobaounion.View.ICatagoryPagerCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CatagoryPagerPresenterImpl implements ICatagoryPagerPresenter {
    //一次可能会请求多个分类页面，比如一次请求推荐和男装页面，就需要不同的CallBack去通知View层显示
    private List<ICatagoryPagerCallback> callbacks=new ArrayList<>();

    //设置成包装类，因为后面要判空
    private Integer currentPage;

    private static final int DEFAULT_PAGE=1;

    //储存catagoryId——pageId，这两个数据用来组成url
    private HashMap<Integer,Integer> pageInfo=new HashMap<>();





    @Override
    public void registerViewCallback(ICatagoryPagerCallback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    @Override
    public void unregistViewCallback(ICatagoryPagerCallback callback) {
        callbacks.remove(callback);
    }



    @Override
    public void getContentByCategoryId(int catagoryId) {
        for(ICatagoryPagerCallback callback:callbacks){
            if(callback!=null&&callback.getCatagoryId()==catagoryId){
                callback.onLoading();
            }
        }

        Integer targetPage = pageInfo.get(catagoryId);
        if(targetPage==null){
            targetPage=DEFAULT_PAGE;
            pageInfo.put(catagoryId,targetPage);
        }

        //将getCall封装，处理loadMore时可复用
        Call<HomePagerContent> call = getHomePagerContentCall(catagoryId, targetPage);
        call.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    HomePagerContent homePagerContent = response.body();

                    //之所以要抽取这两个handle方法，是因为callback有好几个，要找到id对得上的那个，代码里for循环不是很方便
                    handleHomePagerContentResult(homePagerContent,catagoryId);
                }
                else{
                    handleNetworkError(catagoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.e(CatagoryPagerPresenterImpl.class,t.toString());
            }
        });

    }


    private Call<HomePagerContent> getHomePagerContentCall(int catagoryId, Integer targetPage) {
        RetrofitManager retrofitManager=RetrofitManager.getInstance();
        Retrofit retrofit = retrofitManager.getRetrofit();
        API api = retrofit.create(API.class);
        Call<HomePagerContent> call = api.getHomePagerContent(UrlUtils.createHomePagerUrl(catagoryId, targetPage));
        return call;
    }


    private void handleHomePagerContentResult(HomePagerContent homePagerContent,int catagoryId){
        List<HomePagerContent.DataDTO> data = homePagerContent.getData();
        for(ICatagoryPagerCallback callback:callbacks){
            if(callback.getCatagoryId()==catagoryId){
                if(homePagerContent==null||data.size()==0){
                    callback.onEmpty();
                }
                else{
                    List<HomePagerContent.DataDTO> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }


    private void handleNetworkError(int categoryId) {
        for (ICatagoryPagerCallback callback:callbacks) {
            if(callback.getCatagoryId()==categoryId) {
                callback.onNetworkError();
            }
        }
    }




    @Override
    public void loadMore(int catagoryId) {
        currentPage = pageInfo.get(catagoryId);

        if(currentPage ==null){
            currentPage=1;
        }

        currentPage++;
        Call<HomePagerContent> loadMoreCall = getHomePagerContentCall(catagoryId, currentPage);
        loadMoreCall.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    HomePagerContent loadMoreContent = response.body();
                    handleLoadMoreResult(loadMoreContent,catagoryId);
                }
                else{
                    handleLoadMoreError(catagoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleLoadMoreError(catagoryId);
            }
        });
    }



    private void handleLoadMoreResult(HomePagerContent loadMoreContent, int catagoryId) {
        List<HomePagerContent.DataDTO> data = loadMoreContent.getData();
        for(ICatagoryPagerCallback callback:callbacks){
            if(callback.getCatagoryId()==catagoryId){
                if(loadMoreContent==null||data.size()==0){
                    currentPage--;
                    //这里不用HashMap来put回去，因为原来map里储存的就是currentPage--
                    callback.onLoadMoreEmpty();
                }
                else{
                    pageInfo.put(catagoryId,currentPage);
                    callback.onLoadMoreLoaded(data);
                }
            }
        }
    }


    private void handleLoadMoreError(int catagoryId) {
        for(ICatagoryPagerCallback callback:callbacks){
            if(callback.getCatagoryId()==catagoryId){
                currentPage--;
                callback.onLoadMoreError();
            }
        }
    }


    @Override
    public void reload() {

    }
}