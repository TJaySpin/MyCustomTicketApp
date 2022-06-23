package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Model.Domain.SelectedContent;
import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;

import java.util.List;

public interface ISelectedPageCallback extends IBaseCallback{
    public void onCatagoriesLoad(SelectedPagerCatagories selectedPagerCatagories);

    public void onContentLoaded(SelectedContent contents);
}
