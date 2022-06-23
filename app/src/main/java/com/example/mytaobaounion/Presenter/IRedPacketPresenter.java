package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.View.IRedPacketCallback;

public interface IRedPacketPresenter extends IBasePresenter<IRedPacketCallback>{
    public void getRedPacketContent();

    public void reload();

    public void loadMore();
}
