package com.example.mytaobaounion.Presenter.Impl;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.Histories;
import com.example.mytaobaounion.Model.Domain.SearchContent;
import com.example.mytaobaounion.Model.Domain.SearchRecommend;
import com.example.mytaobaounion.Presenter.ISearchPresenter;
import com.example.mytaobaounion.Utils.JsonCacheUtils;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.View.ISearchCallback;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private final API api;
    private ISearchCallback searchCallback;

    private static final int DEFAULT_PAGE=1;
    private static final String KEY_HISTORY = "key_history";
    private static final int CACHE_MAX_SIZE=10;

    private int currentPage=DEFAULT_PAGE;
    private String currentKeyword;
    private JsonCacheUtils jsonCacheUtils;

    public SearchPresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(API.class);
        jsonCacheUtils = JsonCacheUtils.getInstance();
    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        this.searchCallback=callback;
    }

    @Override
    public void unregistViewCallback(ISearchCallback callback) {
        searchCallback=null;
    }



    //histories里面只有个List<String>
    @Override
    public void getHistory() {
        Histories histories = jsonCacheUtils.getCache(KEY_HISTORY, Histories.class);
        if(searchCallback != null){
            searchCallback.onLoadHistory(histories);
        }
    }

    @Override
    public void removeHistory() {
        jsonCacheUtils.removeCache(KEY_HISTORY);
        if(searchCallback!=null){
            searchCallback.onRemoveHistory();
        }
    }


    /**
     *储存的history本质是一个String词条，储存目的是把它添加到Historis类中的List<String>里，因此要先拿到之前缓存的Histories类；
     *创建Histories类（关键是里面的List<String>），主要是为了在View层显示出所有的历史词条，而储存的时候都是一个词条一个词条地储存；
     *对Histories类和里面的List<String>都要判空，因为List里面的词条可能会出现被删空的情况
     */
    private void saveHistories(String history){
        Histories cacheHistories = jsonCacheUtils.getCache(KEY_HISTORY, Histories.class);

        //单独创建一个主要是为了后面判空时方便
        List<String> historyList=null;

        if(cacheHistories!=null&&cacheHistories.getHistoriesList()!=null){
            historyList=cacheHistories.getHistoriesList();
            if(historyList.contains(history)){
                historyList.remove(history);
            }
        }
        //不能用else if.....二者可能都为空
        if(cacheHistories==null){
            cacheHistories=new Histories();
        }
        if(historyList==null){
            historyList=new ArrayList<>();
        }


        historyList.add(history);
        cacheHistories.setHistoriesList(historyList);

        //历史词条数目超过最大值，就直接截取最大的显示数目
        if(historyList.size()>CACHE_MAX_SIZE){
            historyList=historyList.subList(0,CACHE_MAX_SIZE);
        }

        //不要把key弄错了，这里的key是固定的而不是history词条
        jsonCacheUtils.saveCache(KEY_HISTORY,cacheHistories);
    }



    @Override
    public void onSearch(String keyWord) {
        //进行新的搜索时，首先保存之前的搜索历史，再将当前keyword设置为新的keyword
        if(currentKeyword==null||!currentKeyword.equals(keyWord)){
            this.saveHistories(keyWord);
            this.currentKeyword=keyWord;
        }

        if(searchCallback!=null){
            searchCallback.onLoading();
        }

        Call<SearchContent> call = api.getSearchContent(currentPage, keyWord);
        call.enqueue(new Callback<SearchContent>() {
            @Override
            public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                int code=response.code();
                if(code==HttpsURLConnection.HTTP_OK){
                    SearchContent searchContent = response.body();
                    handleSearchResult(searchContent);
                }
                else{
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchContent> call, Throwable t) {
                LogUtils.w(SearchPresenterImpl.class,t.toString());
                onError();
            }
        });
    }

    private void onError() {
        if(searchCallback!=null){
            searchCallback.onNetworkError();
        }
    }


    private void handleSearchResult(SearchContent searchContent) {
        if(searchCallback != null) {
            /**
             * 这里的try很细节，主要是因为在isEmpty方法中，获取size()要一长串，中间一旦有一段是空就会崩，马上报异常，所以这里的try一旦捕捉异常就会走到Catch里面的onEmpty()；
             * 特惠界面采用同样的写法，首页是直接catagories.getData().size()判断，精选界面直接没判空；还是这样写更严谨
             */
            try {
                if(isEmpty(searchContent)) {
                    onEmpty();
                } else {
                    searchCallback.onLoadSearchSuccess(searchContent);
                }
            } catch(Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private void onEmpty() {
        if(searchCallback!=null){
            searchCallback.onEmpty();
        }
    }

    private boolean isEmpty(SearchContent searchContent){
        //这里在获取size的过程中，可能出现中间有某一段是空的情况
        int size=searchContent.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size();
        return size==0;
    }


    //和其他界面的reload一样，本质上就是重新获取一遍数据，如loadData；这里是再次执行onSearch但是加了判空
    @Override
    public void searchRetry() {
        if(currentKeyword==null){
            onEmpty();
        }
        else{
            onSearch(currentKeyword);
        }
    }



    @Override
    public void loadMore() {
        //这里我们没有用HashMap保存，因为不像首页和精选页面一样有多个分类，每个分类都可以loadMore，所以不需要储存catagoryId
        currentPage++;

        //onSearch和loadMore都要依赖于currentKeyword，因此使用前都要对其进行判空
        if(currentKeyword==null){
            onEmpty();
        }
        else{
            Call<SearchContent> call = api.getSearchContent(currentPage, currentKeyword);
            call.enqueue(new Callback<SearchContent>() {
                @Override
                public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                    int code=response.code();
                    if(code==HttpsURLConnection.HTTP_OK){
                        SearchContent searchContent = response.body();
                        handleSearchMoreResult(searchContent);
                    }
                    else{
                        onMoreError();
                    }
                }

                @Override
                public void onFailure(Call<SearchContent> call, Throwable t) {
                    LogUtils.w(SearchPresenterImpl.class,t.toString());
                    onMoreError();
                }
            });
        }
    }

    private void onMoreError() {
        currentPage--;
        if(searchCallback!=null){
            searchCallback.onLoadMoreError();
        }
    }

    private void handleSearchMoreResult(SearchContent searchContent) {
        if(searchCallback != null) {
            try {
                if(isEmpty(searchContent)) {
                    //为空也得currentpage--
                    currentPage--;

                    onMoreEmpty();
                } else {
                    searchCallback.onContentLoadMore(searchContent);
                }
            } catch(Exception e) {
                e.printStackTrace();
                onMoreEmpty();
            }
        }
    }

    private void onMoreEmpty() {
        if(searchCallback!=null){
            searchCallback.onLoadMoreEmpty();
        }
    }


    //获取推荐词条的方法中，如果获取失败或者没有数据，也不用管
    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> call = api.getRecommendContent();
        call.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    //这里拿到数据挺方便的，也不用太担心数据为空报异常
                    SearchRecommend searchRecommend = response.body();
                    searchCallback.onLoadRecommendWords(searchRecommend.getData());
                }
            }
            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                //以后养成习惯，onFailure最好都打log；这里应该是前期发现了数据类型的错误导致了异常
                LogUtils.w(SearchPresenterImpl.class,t.toString());
            }
        });
    }




}
