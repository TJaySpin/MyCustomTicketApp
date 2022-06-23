package com.example.mytaobaounion.UI.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.mytaobaounion.R;

public class AutoLoopViewPager extends ViewPager {
    //标志位，用来标记轮播图的开始和停止
    private boolean isloop=false;

    //默认轮播间隔，这个属性在自定义attrs里有设置
    private static final int DEFAULT_DELAY=2000;
    private int duration=DEFAULT_DELAY;


    /**
     * 通过post(callback)调用；
     * 单独提出来而没有放在startLoop()里是因为，后者如果在轮播图轮询过程中切回桌面，调用onPause生命周期方法后，还会进行一次轮询(可以打log检查)；
     * 猜测可能是因为postDelayed消息已经发出但因为时间间隔还没执行，但不知道为什么提出来就能解决
     */
    private Runnable task=new Runnable() {
        @Override
        public void run() {
            //ViewPager里的方法
            int currentItem=getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);

            if(isloop){
                postDelayed(this,duration);
            }
        }
    };


    public AutoLoopViewPager(@NonNull Context context) {
        this(context,null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化的时候去拿到关键属性（轮播间隔）
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLooperStyle);
        duration = typedArray.getInteger(R.styleable.AutoLooperStyle_duration, DEFAULT_DELAY);
        typedArray.recycle();
    }




    public void startLoop(){
        isloop=true;
        //每个View都有post方法
        post(task);
    }

    public void stopLoop(){
        removeCallbacks(task);
        isloop=false;
    }


    //在View层里轮播前（onResume）可以提前设置间隔时间
    public void setDuration(int duration){
        this.duration=duration;
    }

}
