package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.HomePagerContent;

import java.util.List;

public interface ICatagoryPagerCallback extends IBaseCallback{
    //加载分类内容
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents);


    //加载更多内容
    public void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents);

    public void onLoadMoreError();

    public void onLoadMoreEmpty();



    //加载轮播图
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents);

    //暴露给外部当前的CallBack是通知哪一个分类界面的，根据id判断
    public int getCatagoryId();


}
