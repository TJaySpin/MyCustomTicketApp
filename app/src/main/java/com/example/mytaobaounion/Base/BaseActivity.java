package com.example.mytaobaounion.Base;

import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytaobaounion.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceId());
        unbinder = ButterKnife.bind(this);

        /*
        //将屏幕显示的内容全部变灰
        View decorView = getWindow().getDecorView();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //不能直接setPaint，和layerType有关，可以看看源码
        decorView.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
        */


        //所有子Activity（其实基本就MainActivity一个，还有一个额外的淘口令TicketActivity），自动按这个顺序实现下列方法
        initView();
        initEvent();
        try {
            initPresenter();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    //只用来initFragment，因为MainActivity只用来显示Fragment
    protected void initView() {
    }

    //和initListener没有区别
    protected void initEvent() {
    }

    //在MainActivity中没用，在TicketActivity中使用
    protected abstract void initPresenter() throws PackageManager.NameNotFoundException;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
        }
        release();
    }

    protected void release(){
    }

    protected abstract int getResourceId();

}
