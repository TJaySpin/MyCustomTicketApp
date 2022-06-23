package com.example.mytaobaounion.Presenter;


//这个接口专门为了首页和搜索页Adapter的复用，因为传入的数据不同，所以创建该接口并让他们的数据类去实现，同时为了实现复用跳转淘口令，该接口必须继承IBaseInfo
public interface IItemInfo extends IBaseInfo{
    //setData中需要的数据就是下面三个+IBaseInfo里实现的那几个

    //SearchContent中这里默认是String，直接在类里面全部替换成long，下面的volume同理
    public long getCouponAmount();

    public String getFinalPrice();

    public long getVolume();

}
