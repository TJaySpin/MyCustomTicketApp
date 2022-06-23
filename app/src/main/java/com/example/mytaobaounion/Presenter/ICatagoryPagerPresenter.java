package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.View.ICatagoryPagerCallback;

public interface ICatagoryPagerPresenter extends IBasePresenter<ICatagoryPagerCallback> {

    public void getContentByCategoryId(int categoryId);

    //加载更多，往上拖的时候触发
    public void loadMore(int catagoryId);

    public void reload();
}
