package com.example.mytaobaounion.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytaobaounion.Model.Domain.HomePagerContent;
import com.example.mytaobaounion.Presenter.IItemInfo;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Fragment.HomePagerFragment;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {
    private List<IItemInfo> mData = new ArrayList<>();
    private OnListItemClickListener listItemClickListener=null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        IItemInfo dataDTO = mData.get(position);
        holder.setData(dataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listItemClickListener!=null){
                    listItemClickListener.OnItemClick(dataDTO);
                }
            }
        });
    }

    public interface OnListItemClickListener{
        public void OnItemClick(IItemInfo ItemData);
    }

    public void setListItemClickListener(OnListItemClickListener listener){
        listItemClickListener=listener;
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }



    /**
     * 真正的setData是在BindView里面，这里是把获得的数据传给mData;
     * 此外，因为首页和搜索页面中RecyclerView复用同一个Adapter，而在setData的时候传进来的数据却不一样，因此定义一个IItemInfo接口，让他们两个的数据类去实现，达到真正的复用目的；
     * 因为之前为了实现跳转淘口令界面的复用，让所有页面的数据类都实现了IBaseInfo接口，因此IItemInfo接口必须继承IBaseInfo；
     * 注意，传入List<>中的类型不能是接口，因此用类型通配符替代，下面setLoadMoreData同理
     */
    public void setData(List<? extends IItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    //和普通的setData的区别在于，不是从头开始显示数据，而是接着老数据
    public void setLoadMoreData(List<? extends IItemInfo> contents) {
        int oldsize=mData.size();
        mData.addAll(contents);

        //添加新数据到尾部（局部刷新）
        notifyItemRangeChanged(oldsize-1,contents.size());
    }


    public class InnerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_price)
        public TextView offPrice;

        @BindView(R.id.goods_after_off_price)
        public TextView afteroffPrice;

        @BindView(R.id.goods_original_price)
        public TextView originalPrice;

        @BindView(R.id.sells_count)
        public TextView sellsCount;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        //刷新多少数据是通过Adapter里的setData，而刷新的数据怎么显示是通过InnerHolder里的setData
        public void setData(IItemInfo data) {
            title.setText(data.getTitle());

            /*ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            int width=layoutParams.width;
            int height=layoutParams.height;
            int defineSize=(width>height?width:height)/2;

            //图片大小必须是10的整数，这里按照正常获取是149.因此必须转化成10的倍数，比如140
            if(defineSize%10!=0){
                defineSize-=(defineSize%10);
            }

            String coverUrl=UrlUtils.getCoverPath(data.getCover(),defineSize);
            Glide.with(itemView.getContext()).load(coverUrl).into(cover);*/
            String coverUrl=UrlUtils.getCoverPath(data.getCover());
            Glide.with(itemView.getContext()).load(coverUrl).into(cover);

            int couponAmount = (int) data.getCouponAmount();
            //这里其实是原价，这个名字有误导性
            String finalPrice = data.getFinalPrice();

            offPrice.setText(String.format(myFormat(R.string.text_goods_off_price),couponAmount));

            //afteroffPrice的float形式
            float gapPrice=Float.valueOf(finalPrice)-couponAmount;
            afteroffPrice.setText(String.format("%.2f",gapPrice));

            //在原价上加个横杠，有时候不明显，试着移动看看
            originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrice.setText(String.format(myFormat(R.string.text_goods_origin_price),finalPrice));

            sellsCount.setText(String.format(myFormat(R.string.text_sells_count),data.getVolume()));

        }

        //将上面方法里使用String.format的部分简单封装
        private String myFormat(int StringId){
            return itemView.getContext().getResources().getString(StringId);
        }


    }
}
