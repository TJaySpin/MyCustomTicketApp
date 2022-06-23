package com.lcodecore.tkrefreshlayout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.lcodecore.tkrefreshlayout.utils.LogUtils;

//import com.example.mytaobaounion.Utils.LogUtils;

public class TbNestedScrollView extends NestedScrollView {

    private int LooperHeight;

    //注意，这个变量是滑动的距离，而不是某个布局的高度
    private int originScroll;

    private RecyclerView mRecyclerView;
    private boolean isBottom;

    public boolean isBottom() {
        return isBottom;
    }

    public void setIsBottom(boolean isBottom) {
        this.isBottom = isBottom;
    }



    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //这个距离就是轮播图和RecyclerView之间的高度，所以当滑动的距离等于这个高度（差）的时候就表示到顶了
    public void setLooperHeight(int height){
        LooperHeight =height;
    }



    /**
     * 之所以重写这个方法，顾名思义”预嵌套滑动“，在滑动之前就拿到滑动的差值，根据条件判断滑动事件是否分发给RecyclerView
     * 特别注意，这里重写的是NestedScrollingParent2中的onNestedPreScroll，否则不会被调用到。详情见https://blog.csdn.net/newcoderzz/article/details/99580527
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if(target instanceof RecyclerView){
            mRecyclerView = (RecyclerView) target;
        }


        /**
         * 若NestedScrollView还没滑到顶（轮播图上边缘），滑动事件就由自己消费掉，也就是NestedScrollView继续滑动;
         * 跳出这个if那么滑动事件就会分发给里面的RecyclerView，滑动的就是商品列表
         */
        if(originScroll<LooperHeight){
            //顾名思义，scrollBy代表滑动多少，并将滑动的距离记录在“消费了”数组中；此外，这里只有垂直方向的滑动，dx似乎可以不用管（不确定）
            scrollBy(dx,dy);
            consumed[0]=dx;
            consumed[1]=dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }


    //获得NestedScrollView滑动的距离，这些参数在源码中有注释
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }


    /**
     *  这里isInBottom方法实际上是用在tkRefreshLayout插件源码中的isViewToBottom中被调用，我们写了一个isTbNestedScrollView方法用来处理滑动到底的情况
     *  如果不做任何处理，由于tkRefreshLayout默认进行事件拦截，会导致RecyclerView还没有滑到底就触发loadMore，因此我们针对里面的ScrollView写了个方法来判断滑到底部
     */
    public boolean isInBottom() {
        if(mRecyclerView != null) {
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
            LogUtils.i(TbNestedScrollView.class,"状态："+isBottom);
            return isBottom;
        }
        return false;
    }
}
