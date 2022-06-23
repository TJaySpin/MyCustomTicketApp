package com.example.mytaobaounion.UI.Fragment;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytaobaounion.Adapter.RedpacketContentAdapter;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.Model.Domain.RedPacketContent;
import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.Presenter.IRedPacketPresenter;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.Utils.SizeUtils;
import com.example.mytaobaounion.Utils.TicketUtils;
import com.example.mytaobaounion.Utils.ToastUtils;
import com.example.mytaobaounion.View.IRedPacketCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;


public class RedpacketFragment extends BaseFragment implements IRedPacketCallback, RedpacketContentAdapter.OnRedpacketItemClickListener {

    private IRedPacketPresenter redPacketPresenter;

    @BindView(R.id.redpacket_content_list)
    public RecyclerView mContentList;

    private RedpacketContentAdapter redpacketContentAdapter;

    @BindView(R.id.redpacket_refresh_layout)
    public TwinklingRefreshLayout twinklingRefreshLayout;


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        //拿到rootView布局（包括一个TextView和一个FrameLayout），注意布局里的FrameLayout的id必须是base_container，因为BaseFragment里是通过找到这个根布局，再findViewById找到坑
        return inflater.inflate(R.layout.base_selected_redpacket_fragment_layout,container,false);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        TextView fragmentTitle = rootView.findViewById(R.id.base_fragment_title);
        fragmentTitle.setText("特惠宝贝");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mContentList.setLayoutManager(gridLayoutManager);

        redpacketContentAdapter = new RedpacketContentAdapter();
        mContentList.setAdapter(redpacketContentAdapter);

        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom=SizeUtils.dip2px(getContext(),2.5f);
                outRect.left=SizeUtils.dip2px(getContext(),2.5f);
                outRect.right=SizeUtils.dip2px(getContext(),2.5f);
            }
        });

        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
        twinklingRefreshLayout.setEnableOverScroll(true);

    }

    @Override
    protected void initPresenter() {
        redPacketPresenter = PresenterManager.getInstance().getmRedPacketPresenter();
        redPacketPresenter.registerViewCallback(this);
    }


    @Override
    protected void initListener() {
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if(redPacketPresenter!=null){
                    redPacketPresenter.loadMore();
                }
            }
        });

        redpacketContentAdapter.setRedpacketItemClickListener(this);
    }

    @Override
    protected void loadData() {
        redPacketPresenter.getRedPacketContent();
    }


    @Override
    protected void release() {
        super.release();
        if(redPacketPresenter!=null){
            redPacketPresenter.unregistViewCallback(this);
        }
    }

    @Override
    public int getResId() {
        return R.layout.fragment_redpacket;
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

    @Override
    public void onContentLoaded(RedPacketContent redPacketContent) {
        setUpState(State.SUCCESS);
        redpacketContentAdapter.setData(redPacketContent);
    }

    @Override
    public void onContentLoadMore(RedPacketContent redPacketContent) {
        setUpState(State.SUCCESS);

        //只要有loadMore都别忘了finish加载动画
        twinklingRefreshLayout.finishLoadmore();

        redpacketContentAdapter.setMoreData(redPacketContent);
        ToastUtils.showToast(getContext(),"加载了"+redPacketContent.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size()+"件宝贝");
    }

    @Override
    public void onLoadMoreError() {
        twinklingRefreshLayout.finishLoadmore();

        //loadMore加载不成功基本都是弹toast，在首页也是一样
        ToastUtils.showToast(getContext(),"网络异常，稍后再试");
    }

    @Override
    public void onLoadMoreEmpty() {
        twinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast(getContext(),"没有更多内容了");
    }

    @Override
    public void OnRedpacketItemListener(IBaseInfo dataDTO) {

        /**
         * 下面留着做个参考模板，虽然所有的跳转淘口令代码都是一样的，但区别在于传进来的dataDTO不一样，因此需要定义一个接口去抽象下面公共代码中的公共方法
         */

        /*String title = dataDTO.getTitle();

        //这个才是领券界面url
        String url = dataDTO.getCoupon_click_url();
        if(TextUtils.isEmpty(url)){
            //这个是详情界面的url
            url=dataDTO.getClick_url();
        }


        String cover_url = dataDTO.getPict_url();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover_url);

        Intent intent=new Intent(this.getContext(), TicketActivity.class);
        startActivity(intent);*/

        TicketUtils.toTicketPage(getContext(),dataDTO);
    }

}