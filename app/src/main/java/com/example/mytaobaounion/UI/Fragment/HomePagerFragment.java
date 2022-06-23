package com.example.mytaobaounion.UI.Fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytaobaounion.Adapter.HomePagerContentAdapter;
import com.example.mytaobaounion.Adapter.LooperPagerAdapter;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.Presenter.ICatagoryPagerPresenter;
import com.example.mytaobaounion.Presenter.IItemInfo;
import com.example.mytaobaounion.Presenter.ITicketPresenter;
import com.example.mytaobaounion.Presenter.Impl.CatagoryPagerPresenterImpl;
import com.example.mytaobaounion.R;
//import com.example.mytaobaounion.UI.Custom.TbNestedScrollView;
import com.example.mytaobaounion.UI.Activity.TicketActivity;
import com.example.mytaobaounion.UI.Custom.AutoLoopViewPager;
import com.example.mytaobaounion.Utils.Constant;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.Utils.SizeUtils;
import com.example.mytaobaounion.Utils.TicketUtils;
import com.example.mytaobaounion.Utils.ToastUtils;
import com.example.mytaobaounion.View.ICatagoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.View.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICatagoryPagerCallback, HomePagerContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperClickListener {

    private ICatagoryPagerPresenter mCatagoryPagerPresenter;
    private int materialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    private HomePagerContentAdapter mHomePagerContentAdapter;


    //自定义的轮播图，主要是为了实现自动定时轮转
    @BindView(R.id.looper_pager)
    public AutoLoopViewPager mLooperPager;
    private LooperPagerAdapter mLooperPagerAdapter;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;


    //homePagerFragment最外层的布局，也就是他本身，后面有重要作用
    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedScroller;


    //轮播图那一块的布局，后面重要作用
    @BindView(R.id.home_pager_header_container)
    public LinearLayout homePagerHeaderContainer;

    @Override
    public void onResume() {
        super.onResume();

        //只要页面来到前台，就让轮播图开始轮转
        mLooperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();

        //只要页面退出前台，就让轮播图停止轮转
        mLooperPager.stopLoop();
    }


    /**
        我们在homeFragment的逻辑层拿到分类数据catagories，传给View层，再传给homePagerAdapter，里面的item都是homePagerFragment；我们将传入到Adapter中的分类数据
     catagories作为homePagerFragment的构造方法参数传给它，并利用Bundle保存起来，后面在loadData()中拿出来，显示在界面上
     */
    public static HomePagerFragment getInstance(Catagories.DataDTO dataDTO){
        HomePagerFragment homePagerFragment=new HomePagerFragment();
        Bundle bundle=new Bundle();

        //根据官方api文档，分页内容需要知道id和page
        bundle.putString(Constant.KEY_HOME_PAGER_TITLE,dataDTO.getTitle());
        bundle.putInt(Constant.KEY_HOME_PAGER_MATERIAL_ID,dataDTO.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }



    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {                          //设置每一个itemView之间的间距
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=8;
                outRect.bottom=8;
            }
        });

        mHomePagerContentAdapter = new HomePagerContentAdapter();
        mContentList.setAdapter(mHomePagerContentAdapter);

        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPager.setAdapter(mLooperPagerAdapter);

        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);

    }


    @Override
    protected void initListener() {
        //根据轮播图调整指示器（轮播图下面的点）
        mLooperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //防止下面分母为0崩溃
                if(mLooperPagerAdapter.getDataSize()==0){
                    return;
                }
                int targetPos=position%mLooperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.i(HomePagerFragment.class,"加载更多成功");
                if(mCatagoryPagerPresenter!=null){
                    mCatagoryPagerPresenter.loadMore(materialId);
                }
            }
        });


        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取轮播图那部分的高度，这个和解决加载过多无关，是为了解决滑动冲突用的
                int headerHeight=homePagerHeaderContainer.getMeasuredHeight();
                homePagerNestedScroller.setLooperHeight(headerHeight);

                //获取整个homePagerFragment的高度
                int measuredHeight = homePagerParent.getMeasuredHeight();

                //将RecyclerView的高度设置成这个homePagerFragment的高度（这个只是为了不用一次性全部创建所有ViewHolder，和嵌套滑动无关，解决滑动冲突在自定义的sv中），设置完之后移除观察者
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height=measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if(layoutParams.height!=0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        mHomePagerContentAdapter.setListItemClickListener(this);                                    //必须在UI层设置得设置，否则ClickListener一直为null，点击没反应

        mLooperPagerAdapter.setLooperClickListener(this);

    }



    @Override
    protected void initPresenter() {
        mCatagoryPagerPresenter = PresenterManager.getInstance().getmCatagoryPagerPresenter();
        mCatagoryPagerPresenter.registerViewCallback(this);
    }




    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        materialId = bundle.getInt(Constant.KEY_HOME_PAGER_MATERIAL_ID);


        if(mCatagoryPagerPresenter!=null){
            mCatagoryPagerPresenter.getContentByCategoryId(materialId);
        }

        String title = bundle.getString(Constant.KEY_HOME_PAGER_TITLE);

        //就是轮播图下面那个“-推荐-“标题
        if(currentCategoryTitle!=null){
            currentCategoryTitle.setText(title);
        }

    }





    @Override
    public void onContentLoaded(List<HomePagerContent.DataDTO> contents) {
        setUpState(State.SUCCESS);                                                                  //必须得有，要不然一直显示加载
        mHomePagerContentAdapter.setData(contents);
    }




    @Override
    public int getResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }



    //下面所有loadMore的方法都要注意调用finishLoadmore把刷新转圈动画消除，不是onFinishLoadMore，否则只能loadmore一次！！！！！
    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataDTO> contents) {
        mHomePagerContentAdapter.setLoadMoreData(contents);
        if(twinklingRefreshLayout!=null){
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast(getContext(),"加载了"+contents.size()+"个宝贝");
    }

    @Override
    public void onLoadMoreError() {
        if(twinklingRefreshLayout!=null){
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast(getContext(),"网络错误，请稍后重试......");
    }

    @Override
    public void onLoadMoreEmpty() {
        if(twinklingRefreshLayout!=null){
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast(getContext(),"没有更多内容了");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataDTO> contents) {
        mLooperPagerAdapter.setData(contents);

        //为了防止轮播图一开始不能左滑，将初始位置设置在大数中间
        int currentPos=(Integer.MAX_VALUE/2)%contents.size();
        int firstPos=Integer.MAX_VALUE/2-currentPos;
        mLooperPager.setCurrentItem(firstPos);

        //加载轮播图上的白点
        looperPointContainer.removeAllViews();
        int dpSize= SizeUtils.dip2px(getContext(),8);
        for(int i=0;i<contents.size();i++){
            View point=new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpSize, dpSize);
            params.setMargins(SizeUtils.dip2px(getContext(),5),0,SizeUtils.dip2px(getContext(),5),0);
            point.setLayoutParams(params);
            looperPointContainer.addView(point);
        }
    }

    //在initListener中调用，滑动轮播图时监听变化
    private void updateLooperIndicator(int targetPos) {
        for(int i=0;i<looperPointContainer.getChildCount();i++){
            View point=looperPointContainer.getChildAt(i);
            if(i==targetPos){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }
            else{
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    public int getCatagoryId() {
        return materialId;
    }

    @Override
    protected void release() {
        if(mCatagoryPagerPresenter!=null){
            mCatagoryPagerPresenter.unregistViewCallback(this);
        }
    }

    @Override
    public void OnItemClick(IItemInfo ItemData) {
        handleItemClick(ItemData);
    }


    @Override
    public void OnLooperItemClick(HomePagerContent.DataDTO LooperData) {
        handleItemClick(LooperData);
    }

    private void handleItemClick(IBaseInfo itemData) {
        /**
         * 下面留着做个参考模板，虽然所有的跳转淘口令代码都是一样的，但区别在于传进来的dataDTO不一样，因此需要定义一个接口去抽象下面公共代码中的公共方法
         */

        /*String title = itemData.getTitle();

        //这个才是领券界面url
        String url = itemData.getCoupon_click_url();
        if(TextUtils.isEmpty(url)){
            //这个是详情界面的url
            url=itemData.getClick_url();
        }


        String cover_url = itemData.getPict_url();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover_url);

        Intent intent=new Intent(this.getContext(),TicketActivity.class);
        startActivity(intent);*/


        TicketUtils.toTicketPage(getContext(),itemData);
    }
}