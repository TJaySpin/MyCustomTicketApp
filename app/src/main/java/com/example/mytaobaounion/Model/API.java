package com.example.mytaobaounion.Model;

import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Model.Domain.RedPacketContent;
import com.example.mytaobaounion.Model.Domain.SearchContent;
import com.example.mytaobaounion.Model.Domain.SearchRecommend;
import com.example.mytaobaounion.Model.Domain.SelectedContent;
import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;
import com.example.mytaobaounion.Model.Domain.TicketModel;
import com.example.mytaobaounion.Model.Domain.TicketParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface API {
    @GET("discovery/categories")
    public Call<Catagories> getCatagories();

    @GET
    public Call<HomePagerContent> getHomePagerContent(@Url String url);


    //api文档中指明body并包含两个属性，因此直接定义了一个TicketParams类作为body
    @POST("tpwd")
    Call<TicketModel> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPagerCatagories> getSelectedCatagories();

    @GET
    Call<SelectedContent> getSelectedContent(@Url String url);

    @GET
    Call<RedPacketContent> getRedPacketContent(@Url String url);

    //这里在api文档中没有特别指明
    @GET("search/recommend")
    Call<SearchRecommend> getRecommendContent();

    //api文档中接口是/search，参数是page和keywords，这里用Query查询
    @GET("search")
    Call<SearchContent> getSearchContent(@Query("page") int page,@Query("keyword") String keyword);
}
