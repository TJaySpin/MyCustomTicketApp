package com.example.mytaobaounion.UI.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mytaobaounion.R;


//继承自ImageView，注意Attached和Detached两个生命周期方法，前者开始旋转（加载），后者停止加载
public class LoadingView extends AppCompatImageView {
    private float mDegree=0;

    //标志位，标志onDetachedFromWindow是否被触发，“是否需要转”
    private boolean mNeedRotate;

    public LoadingView(@NonNull Context context) {
        this(context,null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //如果是继承自ImageView，可以直接这样设置图片
        setImageResource(R.mipmap.loading);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        startRotate();
    }

    private void startRotate() {
        //和轮播图那里类似，通过post一个runnable实现伪动画效果；也可以把runnable单独提出来，然后post(task)
        post(new Runnable() {
            @Override
            public void run() {
                mDegree+=10;

                //由于设置成了float，不能用%360计算
                if(mDegree>=360){
                    mDegree=0;
                }

                //通知刷新UI
                invalidate();

                //当loadingView不可见（即加载完成），或者切出当前页面（即onDetachedFromWindow被触发），就移除runnable停止旋转
                if(getVisibility()!=VISIBLE&&mNeedRotate==false){
                    removeCallbacks(this);
                }
                else{
                    postDelayed(this,10);
                }
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    //仅仅将标志位设置成false即可，代表onDetachedFromWindow被触发，不需要旋转了
    private void stopRotate() {
        mNeedRotate=false;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        //起始角度，坐标为旋转中心
        canvas.rotate(mDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
