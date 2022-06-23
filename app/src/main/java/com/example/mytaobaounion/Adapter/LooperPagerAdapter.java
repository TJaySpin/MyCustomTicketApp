package com.example.mytaobaounion.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
    List<HomePagerContent.DataDTO> mData=new ArrayList<>();
    private OnLooperClickListener looperClickListener=null;

    @Override
    public int getCount() {
        //将轮播图条目设置为最大值，实现伪无线轮转（实际上到达最大值就不能继续滑动了）
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        //直接比较二者地址来判断
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    //初始化图片：用Gilde将图片加载到Imageview，然后加入ViewGroup中
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();

        //由于前面设置了条目数为Integer.MAX_VALUE，因此数据一定会大于mData.size()，这里通过取余实现轮播图在mData中的轮转；此外，这里为了防止mData.size()为0，在View层的initListener里加了判断
        int realPositon=position%mData.size();
        HomePagerContent.DataDTO dataDTO = mData.get(realPositon);
        ImageView img=new ImageView(context);

        //调整加载的图片大小；layoutParams是MATCH_PARENT，不能直接拿到size，所以只能拿parent的size，也就是参数中的container
        int width = container.getMeasuredWidth();
        int height = container.getMeasuredHeight();
        int defineSize=(width>height?width:height)/2;
        String coverUrl=UrlUtils.getCoverPath(dataDTO.getPict_url());
        Glide.with(context).load(coverUrl).into(img);


        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        img.setLayoutParams(layoutParams);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        container.addView(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(looperClickListener!=null){
                    looperClickListener.OnLooperItemClick(dataDTO);
                }
            }
        });

        return img;
    }

    public interface OnLooperClickListener{                                                         //也是用来跳转淘口令界面传数据的，和HomePagerAdapter里的差不多
        public void OnLooperItemClick(HomePagerContent.DataDTO LooperData);
    }

    public void setLooperClickListener(OnLooperClickListener listener){
        looperClickListener=listener;
    }


    public void setData(List<HomePagerContent.DataDTO> contents) {
        if(mData!=null){
            mData.clear();
        }
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize(){
        return mData.size();
    }
}
