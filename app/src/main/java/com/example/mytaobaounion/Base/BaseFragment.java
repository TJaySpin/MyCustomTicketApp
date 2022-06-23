package com.example.mytaobaounion.Base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private FrameLayout mContainer;
    private View mSuccessView;
    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;
    private State currentState=State.NONE;

    protected enum State {
        NONE,EMPTY,LOADING,SUCCESS,ERROR
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //顾名思义，加载根布局rootView，即每个Fragment的根布局，4个Fragment根布局各不相同
        View rootView = loadRootView(inflater,container);

        //虽然每个Fragment的根布局都不同，但里面都有一个FrameLayout坑用来填充具体内容，并且id都是相同的R.id.base_container，拿到这个container，通过addView将具体的Fragment的内容填充进去
        mContainer = rootView.findViewById(R.id.base_container);

        //所有子Fragment，自动按这个顺序实现下列方法
        //loadStateView方法就是将具体的Fragment布局填充到container中，包括4个内容Fragment，以及success，loading，empty，error四种状态的Fragment
        loadStateView(inflater,container);

        //ButterKnife一定是绑定当前Fragment或者Activity，才能使用里面的的控件；注意！必须在loadStateView加载完界面之后绑定
        unbinder = ButterKnife.bind(this,rootView);

        initView(rootView);
        initListener();
        initPresenter();

        //这个方法的内容基本上都是利用逻辑层Impl去获取数据
        loadData();

        return rootView;
    }



    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container,false);
    }

    //默认初始化时必须调用，一次性添加四种状态的Fragment，但是根据状态来设置是否显示，在子类中重写setUpState判断状态
    private void loadStateView(LayoutInflater inflater,ViewGroup container){
        //成功状态，内部根据id来添加4种内容Fragment
        mSuccessView = loadSuccessView(inflater, container);
        mContainer.addView(mSuccessView);

        //空白状态
        mEmptyView = loadEmptyView(inflater, container);
        mContainer.addView(mEmptyView);

        //加载状态
        mLoadingView = loadLoadingView(inflater, container);
        mContainer.addView(mLoadingView);

        //错误状态
        mErrorView = loadErrorView(inflater, container);
        mContainer.addView(mErrorView);

        setUpState(State.NONE);
    }

    //第三个参数一定要是false，因为为true的话，addview方法会调用两次，程序直接闪退
    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resId=getResId();
        return inflater.inflate(resId, container, false);
    }

    protected View loadErrorView(LayoutInflater inflater,ViewGroup container){
        return inflater.inflate(R.layout.fragment_error,container,false);
    }

    protected View loadLoadingView(LayoutInflater inflater,ViewGroup container){
        return inflater.inflate(R.layout.fragment_loading,container,false);
    }

    protected View loadEmptyView(LayoutInflater inflater,ViewGroup container){
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }

    protected void setUpState(State currentState) {
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
    }

    //根据id，添加4种内容Fragment（在loadSuccessView中使用），由于这一系列方法一定会实现，所以设置成abstract，强制要求所有子Fragment重写
    public abstract int getResId();




    protected void initView(View rootView) {
    }

    protected void initListener() {
    }

    //项目中都是在Fragment（即View层）直接new出Presenter对象
    protected void initPresenter() {
    }

    protected void loadData() {}





    //网络失效后，点击重试连接
    @OnClick({R.id.network_error_tips})
    public void retry() {
        LogUtils.d(BaseFragment.class,"网炸了");
        onRetryClick();
    }

    //因为不同的内容Fragment加载的内容不同，所以具体实现要交给各个内容Fragment重写;不过后面基本没怎么用，都是在逻辑层里面写reload，view层重写retry，在里面调用reload
    protected void onRetryClick() {
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }
        release();
    }

    //在Destroy里调用，基本是注销callback
    protected void release() {
    }



}
