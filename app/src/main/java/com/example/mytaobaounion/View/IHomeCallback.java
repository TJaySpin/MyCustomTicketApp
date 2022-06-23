package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.Catagories;

//同样的，只需要将分类数据，显示到View层，这个通过在逻辑层注册callback，调用该方法实现
public interface IHomeCallback extends IBaseCallback{

    public void onCatagoriesLoad(Catagories catagories);

}
