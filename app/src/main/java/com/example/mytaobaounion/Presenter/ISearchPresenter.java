package com.example.mytaobaounion.Presenter;

import com.example.mytaobaounion.View.ISearchCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback>{
    public void getHistory();

    public void removeHistory();

    public void onSearch(String keyWord);

    public void searchRetry();

    public void loadMore();

    public void getRecommendWords();
}
