package com.example.mytaobaounion.UI.Fragment;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytaobaounion.Adapter.SelectedContentAdapter;
import com.example.mytaobaounion.Adapter.SelectedPagerLeftAdapter;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.Model.Domain.SelectedContent;
import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;
import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.Presenter.ISelectedPagePresenter;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.Utils.SizeUtils;
import com.example.mytaobaounion.Utils.TicketUtils;
import com.example.mytaobaounion.View.ISelectedPageCallback;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPagerLeftAdapter.OnLeftItemClickListener, SelectedContentAdapter.OnContentItemClickListener {
    private ISelectedPagePresenter selectedPagerPresenter;

    @BindView(R.id.left_catagory_list)
    public RecyclerView catagoryList;
    @BindView(R.id.content_list)
    public RecyclerView contentList;


    private SelectedPagerLeftAdapter selectedPagerLeftAdapter;
    private SelectedContentAdapter selectedContentAdapter;


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_selected_redpacket_fragment_layout,container,false);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        TextView fragmentTitle = rootView.findViewById(R.id.base_fragment_title);
        fragmentTitle.setText("精选宝贝");

        catagoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedPagerLeftAdapter = new SelectedPagerLeftAdapter();
        catagoryList.setAdapter(selectedPagerLeftAdapter);

        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedContentAdapter = new SelectedContentAdapter();
        contentList.setAdapter(selectedContentAdapter);
        contentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= SizeUtils.dip2px(getContext(),4);
                outRect.bottom=SizeUtils.dip2px(getContext(),4);
                outRect.left=SizeUtils.dip2px(getContext(),3);
                outRect.right=SizeUtils.dip2px(getContext(),3);
            }
        });
    }

    @Override
    protected void initListener() {
        selectedPagerLeftAdapter.setLeftItemClickListener(this);
        selectedContentAdapter.setContentItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        selectedPagerPresenter = PresenterManager.getInstance().getSelectedPagerPresenter();
        selectedPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        selectedPagerPresenter.getCatagories();
    }

    @Override
    public int getResId() {
        return R.layout.fragment_selected;
    }


    @Override
    protected void release() {
        super.release();
        if(selectedPagerPresenter!=null){
            selectedPagerPresenter.unregistViewCallback(this);
        }
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
    public void retry() {
        super.retry();
        if(selectedPagerPresenter!=null){
            selectedPagerPresenter.reload();
        }
    }

    @Override
    public void onCatagoriesLoad(SelectedPagerCatagories selectedPagerCatagories) {
        setUpState(State.SUCCESS);
        selectedPagerLeftAdapter.setData(selectedPagerCatagories);
    }


    @Override
    public void onContentLoaded(SelectedContent contents) {
        //每次点击左边的标签，右边界面都会从第一个item开始
        contentList.scrollToPosition(0);

        selectedContentAdapter.setData(contents);
    }

    @Override
    public void onLeftItemClick(SelectedPagerCatagories.DataDTO dataDTO) {
        selectedPagerPresenter.getContentByCategoryId(dataDTO);
    }

    @Override
    public void OnContentItemClick(IBaseInfo data) {
        /**
         * 下面留着做个参考模板，虽然所有的跳转淘口令代码都是一样的，但区别在于传进来的dataDTO不一样，因此需要定义一个接口去抽象下面公共代码中的公共方法
         */

        /*String title = data.getTitle();

        //这个才是领券界面url
        String url = data.getCoupon_click_url();
        if(TextUtils.isEmpty(url)){
            //这个是详情界面的url
            url=data.getClick_url();
        }


        String cover_url = data.getPict_url();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        ticketPresenter.getTicket(title,url,cover_url);

        Intent intent=new Intent(this.getContext(), TicketActivity.class);
        startActivity(intent);*/


        TicketUtils.toTicketPage(getContext(),data);
    }
}