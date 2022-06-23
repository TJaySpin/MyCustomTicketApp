package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.View.ITicketCallBack;

public interface ITicketPresenter extends IBasePresenter<ITicketCallBack>{

    //这里有三个参数，是因为淘口令界面需要title和url请求数据，而cover只是为了显示图片，对于取数据不影响；这三个参数都来自homePagerFragment逻辑层获取的数据
    public void getTicket(String title,String url,String cover);
}
