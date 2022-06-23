package com.example.mytaobaounion.UI.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mytaobaounion.Base.BaseActivity;
import com.example.mytaobaounion.Model.Domain.TicketModel;
import com.example.mytaobaounion.Presenter.ITicketPresenter;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Custom.LoadingView;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.Utils.ToastUtils;
import com.example.mytaobaounion.Utils.UrlUtils;
import com.example.mytaobaounion.View.ITicketCallBack;

import butterknife.BindView;

/**
 * 当点击item的时候会跳到淘口令界面，item的点击事件在对应的Adapter中的onBindViewHolder里给itemView设置；
 * Adapter里采用接口回调将具体实现丢给View层的fragment，fragment重写接口中对应的方法（例如onItemClick），并调用对应Adapter的set把接口设置回去，这才能在onBindViewHolder里生效；
 * 由于不同的内容fragment拿到的数据不同，因此又创建了一个淘口令工具类，抽取公共部分，根据传来的数据类型跳转到淘口令界面；
 * 每个商品的淘口令数据都不同，因此同样需要M-V-P
 */
public class TicketActivity extends BaseActivity implements ITicketCallBack {

    private ITicketPresenter ticketPresenter;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;
    @BindView(R.id.ticket_code)
    public EditText mCode;
    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mTvCopyOrOpen;
    @BindView(R.id.ticket_cover_loading)
    public LoadingView loadingView;
    @BindView(R.id.ticket_load_retry)
    public TextView retryText;
    private boolean hasTaoBaoApp;


    @Override
    protected void initView() {

    }



    @Override
    protected void initEvent() {
        mTvCopyOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taoCode = mCode.getText().toString().trim();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //第一个参数仅为标签，可自定义
                ClipData clipDataCode = ClipData.newPlainText("sob_taobao_ticket_code", taoCode);
                clipboardManager.setPrimaryClip(clipDataCode);
                LogUtils.i(TicketActivity.class,"wdnmd:"+taoCode);
                if(hasTaoBaoApp){
                    Intent taobaoIntent=new Intent();

                    /*taobaoIntent.setAction("android.intent.action.MAIN");
                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");*/

                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }
                else{
                    ToastUtils.showToast(TicketActivity.this,"已经复制，粘贴分享，或打开淘宝");
                }
            }
        });
    }

    @Override
    protected void initPresenter(){
        ticketPresenter = PresenterManager.getInstance().getmTicketPresenter();
        if(ticketPresenter!=null){
            ticketPresenter.registerViewCallback(this);
        }

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            if(packageInfo!=null){
                hasTaoBaoApp = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            hasTaoBaoApp=false;
            e.printStackTrace();
        }

        mTvCopyOrOpen.setText(hasTaoBaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_ticket;
    }


    @Override
    protected void release() {
        super.release();
        if(ticketPresenter!=null){
            ticketPresenter.unregistViewCallback(this);
        }
    }

    @Override
    public void onLoading() {
        if(loadingView!=null){
            loadingView.setVisibility(View.VISIBLE);
        }
        if(retryText!=null){
            retryText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetworkError() {
        if(loadingView!=null){
            loadingView.setVisibility(View.GONE);
        }
        if(retryText!=null){
            retryText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnTicketCodeLoaded(String cover, TicketModel ticketModel) {
        if(loadingView!=null){
            loadingView.setVisibility(View.GONE);
        }

        if(mCover!=null&&!TextUtils.isEmpty(cover)){
            Glide.with(TicketActivity.this).load(UrlUtils.getCoverPath(cover)).into(mCover);
        }

        //扫码进入淘口令界面，防止没有图片，随便给张图片
        if(TextUtils.isEmpty(cover)){
            mCover.setImageResource(R.mipmap.baipiao);
        }


        if(ticketModel!=null&&ticketModel.getData().getTbk_tpwd_create_response()!=null){
            //获取淘口令
            String ticketCode=ticketModel.getData().getTbk_tpwd_create_response().getData().getModel();
            mCode.setText(ticketCode);
        }
    }
}