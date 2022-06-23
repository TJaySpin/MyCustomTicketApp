package com.example.mytaobaounion.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mytaobaounion.Adapter.HomePagerAdapter;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.Model.Domain.Catagories;
import com.example.mytaobaounion.Presenter.IHomePresenter;
import com.example.mytaobaounion.Presenter.Impl.HomePresenterImpl;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Activity.IMainActivity;
import com.example.mytaobaounion.UI.Activity.MainActivity;
import com.example.mytaobaounion.UI.Activity.ScanQrActivity;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.View.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.vondear.rxfeature.activity.ActivityScanerCode;
//import com.tamsiree.rxfeature.activity.ActivityScanerCode;
//import com.vondear.rxfeature.activity.ActivityScanerCode;



import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {
    private IHomePresenter mHomePresenter;
    private HomePagerAdapter homePagerAdapter;

    @BindView(R.id.home_indicator)
    protected TabLayout mTabLayout;

    @BindView(R.id.home_viewpager)
    protected ViewPager mHomePager;

    @BindView(R.id.home_search_et)
    public EditText homeEditText;

    @BindView(R.id.scan_icon)
    public ImageView scanIcon;



    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }



    @Override
    protected void initView( View rootView) {
        //保证刚打开时就能显示
        setUpState(State.SUCCESS);
        mTabLayout.setupWithViewPager(mHomePager);
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(homePagerAdapter);
    }




    /**
     * 处理首页特殊情况，当首页搜索栏被点击时，直接跳到搜索界面；
     * 但跳到搜索页面要通过MainActivity点击bottomNavigationView的搜索界面图标，因此这里需要Fragment和MainActivity之间通信
     */
    @Override
    protected void initListener() {
        homeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment与Activity之间的通信，这里是通过Fragment.getActivity（）直接拿到它依附的Activity，即MainActivity
                FragmentActivity activity = getActivity();
                if(activity instanceof IMainActivity){
                    ((IMainActivity) activity).switch2search();
                }
            }
        });

        scanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫码界面
                startActivity(new Intent(getContext(), ScanQrActivity.class));
            }
        });
    }




    @Override
    protected void initPresenter() {
        mHomePresenter= PresenterManager.getInstance().getmHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }



    @Override
    protected void loadData() {
        mHomePresenter.getCatagories();
    }


    @Override
    public int getResId() {
        return R.layout.fragment_home;
    }



    @Override
    protected void release() {
        if(mHomePresenter!=null){
            mHomePresenter.unregistViewCallback(this);
        }
    }


    @Override
    protected void onRetryClick() {
        //重新加载分类
        if(mHomePresenter!=null){
            loadData();
        }
    }




    //利用Adapter更新数据，项目中基本都是这个模式
    @Override
    public void onCatagoriesLoad(Catagories catagories) {
        //ex，这里没有判断catagories为空是因为，已经在逻辑层Impl中拿数据的时候判断了
        setUpState(State.SUCCESS);
        if(homePagerAdapter!=null){
            /*//控制一次性加载多少页
            mHomePager.setOffscreenPageLimit(catagories.getData().size());*/

            //在View层显示数据，这里是利用Adapter显示
            homePagerAdapter.setCategoriesData(catagories);
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
}
