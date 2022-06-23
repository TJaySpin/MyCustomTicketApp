package com.example.mytaobaounion.Utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.Presenter.ITicketPresenter;
import com.example.mytaobaounion.UI.Activity.TicketActivity;

public class TicketUtils {

    //通过dataDTO实现IBaseInfo接口，调用三个公共的方法
    public static void toTicketPage(Context context, IBaseInfo baseInfo){
        String title = baseInfo.getTitle();

        String url = baseInfo.getUrl();
        if(TextUtils.isEmpty(url)){
            url=baseInfo.getUrl();
        }


        String cover_url = baseInfo.getCover();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover_url);

        //context用来跳转的，而且只有view才有context
        Intent intent=new Intent(context, TicketActivity.class);
        context.startActivity(intent);
    }
}
