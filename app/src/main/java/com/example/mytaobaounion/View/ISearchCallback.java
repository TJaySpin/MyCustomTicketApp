package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.Histories;
import com.example.mytaobaounion.Model.Domain.SearchContent;
import com.example.mytaobaounion.Model.Domain.SearchRecommend;

import java.util.List;

public interface ISearchCallback extends IBaseCallback{
    public void onLoadHistory(Histories history);

    //这一步主要在删除历史记录后更新UI
    public void onRemoveHistory();


    public void onLoadSearchSuccess(SearchContent searchContent);


    public void onLoadRecommendWords(List<SearchRecommend.DataDTO> dataDTO);


    public void onContentLoadMore(SearchContent searchContent);

    public void onLoadMoreError();

    public void onLoadMoreEmpty();

}
