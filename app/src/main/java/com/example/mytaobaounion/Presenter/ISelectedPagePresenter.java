package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;
import com.example.mytaobaounion.View.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback>{
    public void getCatagories();

    public void getContentByCategoryId(SelectedPagerCatagories.DataDTO item);

    public void reload();
}
