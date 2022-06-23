package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.View.IHomeCallback;


//HomeFragment的逻辑层只需获取TabLayout里的分类，具体显示的内容在每个分类下的HomePagerFragment，那将定义新的逻辑层
public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    public void getCatagories();

}
