package com.example.mytaobaounion.Utils;

import com.example.mytaobaounion.Presenter.ICatagoryPagerPresenter;
import com.example.mytaobaounion.Presenter.IHomePresenter;
import com.example.mytaobaounion.Presenter.IRedPacketPresenter;
import com.example.mytaobaounion.Presenter.ISearchPresenter;
import com.example.mytaobaounion.Presenter.ISelectedPagePresenter;
import com.example.mytaobaounion.Presenter.ITicketPresenter;
import com.example.mytaobaounion.Presenter.Impl.CatagoryPagerPresenterImpl;
import com.example.mytaobaounion.Presenter.Impl.HomePresenterImpl;
import com.example.mytaobaounion.Presenter.Impl.RedPacketPresenterImpl;
import com.example.mytaobaounion.Presenter.Impl.SearchPresenterImpl;
import com.example.mytaobaounion.Presenter.Impl.SelectedPagerPresenterImpl;
import com.example.mytaobaounion.Presenter.Impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager myInstance=new PresenterManager();


    private final ICatagoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectedPagePresenter mSelectedPagePresenter;
    private final IRedPacketPresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public ITicketPresenter getmTicketPresenter() {
        return mTicketPresenter;
    }

    public IHomePresenter getmHomePresenter() {
        return mHomePresenter;
    }

    public ICatagoryPagerPresenter getmCatagoryPagerPresenter() {
        return mCategoryPagePresenter;
    }

    public static PresenterManager getInstance() {
        return myInstance;
    }

    public ISelectedPagePresenter getSelectedPagerPresenter() {
        return mSelectedPagePresenter;
    }

    public IRedPacketPresenter getmRedPacketPresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getmSearchPresenter() {
        return mSearchPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CatagoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagerPresenterImpl();
        mOnSellPagePresenter = new RedPacketPresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();
    }
}
