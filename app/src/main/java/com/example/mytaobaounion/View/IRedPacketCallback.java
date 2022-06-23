package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.RedPacketContent;

public interface IRedPacketCallback extends IBaseCallback{
    public void onContentLoaded(RedPacketContent redPacketContent);



    public void onContentLoadMore(RedPacketContent redPacketContent);

    public void onLoadMoreError();

    public void onLoadMoreEmpty();

}
