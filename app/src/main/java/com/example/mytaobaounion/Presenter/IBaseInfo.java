package com.example.mytaobaounion.Presenter;


//跳转淘口令界面时需要根据传入data的不同，获取图片url，url和title三个数据去请求淘口令的数据，因此只需要用到data中的三个数据，所以将他们抽取出来，让model层不同的data实现该接口
public interface IBaseInfo {
    String getCover();


    String getTitle();

    String getUrl();
}
