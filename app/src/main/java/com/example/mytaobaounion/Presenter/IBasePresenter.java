package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.UI.Fragment.HomeFragment;


//所有逻辑层必须的方法，注册View层的callback接口用于回调，这里单独抽取出来，只是为了稍微方便一点
public interface IBasePresenter<T> {
    public void registerViewCallback(T callback);

    public void unregistViewCallback(T callback);


}