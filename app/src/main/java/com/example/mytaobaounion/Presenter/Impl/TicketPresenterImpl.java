package com.example.mytaobaounion.Presenter.Impl;

import com.example.mytaobaounion.Model.API;
import com.example.mytaobaounion.Model.Domain.TicketModel;
import com.example.mytaobaounion.Model.Domain.TicketParams;
import com.example.mytaobaounion.Presenter.ITicketPresenter;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.RetrofitManager;
import com.example.mytaobaounion.Utils.UrlUtils;
import com.example.mytaobaounion.View.ITicketCallBack;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketCallBack viewCallback;
    private LoadState curState=LoadState.NONE;
    private String mCover;
    private TicketModel ticketModel;

    enum LoadState{
        SUCCESS,ERROR,LOADING,NONE
    }

    @Override
    public void registerViewCallback(ITicketCallBack callback) {
        /**
         * 这里callback可能为空，因为数据不是直接刷新到view层（之前的都是逻辑层直接回调接口+setAdapter来通知view层刷新），而是要跳转到淘口令的Activity（前面一直到setAdapter都一样）;
         * 因此，在多线程情况下，网络IO线程可能先于UI线程完成（即逻辑层先拿到数据，view层才注册完接口）
         * 因此设定了一个标志位curState对应着不同的状态，只要是onTicketLoaded，即使viewCallback为空（来不及注册导致的），也正常更新view层
         */
        viewCallback = callback;

        if(curState!=LoadState.NONE){
            if(curState==LoadState.SUCCESS){
                onTicketLoaded();
            }
            else if(curState==LoadState.LOADING){
                onTicketLoading();
            }
            else if(curState==LoadState.ERROR){
                onTicketError();
            }
        }
    }

    @Override
    public void unregistViewCallback(ITicketCallBack callback) {
        viewCallback=null;
    }

    @Override
    public void getTicket(String title, String url, String cover) {
        mCover = cover;

        //似乎这里的方法和请求图片是没什么区别
        String targetUrl = UrlUtils.getTicketUrl(url);

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        TicketParams ticketParams = new TicketParams(targetUrl,title);
        Call<TicketModel> call = api.getTicket(ticketParams);
        call.enqueue(new Callback<TicketModel>() {
            @Override
            public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                int code=response.code();
                if(code== HttpsURLConnection.HTTP_OK){
                    ticketModel = response.body();
                    onTicketLoaded();
                }
                else{
                    onTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketModel> call, Throwable t) {
                onTicketError();
            }
        });
    }

    private void onTicketError() {
        if(viewCallback!=null){
            viewCallback.onNetworkError();
        }
        else{                                                                                       //这些else就是为了应对上面那些情况，即数据到了但Callback还没创建
            curState=LoadState.ERROR;
        }
    }

    private void onTicketLoaded() {
        if(viewCallback!=null){
            viewCallback.OnTicketCodeLoaded(mCover, ticketModel);
        }
        else{
            curState=LoadState.SUCCESS;
        }
    }

    private void onTicketLoading(){
        if(viewCallback!=null){
            viewCallback.onLoading();
        }
        else{
            curState=LoadState.LOADING;
        }
    }
}
