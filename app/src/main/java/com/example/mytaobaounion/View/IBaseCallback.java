package com.example.mytaobaounion.View;


//所有View层的callback必须的方法，用View层的Fragment实现callback接口，Prensenter层通过注册该接口，再在内部调用callback接口里的方法通知View层更新，而方法具体实现就在View层的Fragment里
public interface IBaseCallback {
    public void onLoading();

    public void onEmpty();

    public void onNetworkError();
}

