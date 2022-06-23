package com.example.mytaobaounion.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytaobaounion.Model.Domain.SelectedPagerCatagories;
import com.example.mytaobaounion.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedPagerLeftAdapter extends RecyclerView.Adapter<SelectedPagerLeftAdapter.InnerHolder> {
    private List<SelectedPagerCatagories.DataDTO> mData=new ArrayList<>();

    //设置选中的效果
    private int curSelectedPosition=0;

    private OnLeftItemClickListener leftItemClickListener=null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedPagerCatagories.DataDTO dataDTO = mData.get(position);
        TextView tv = holder.itemView.findViewById(R.id.left_catagory_tv);
        int tempPos=position;

        if(curSelectedPosition==tempPos){
            tv.setBackgroundColor(tv.getResources().getColor(R.color.colorPageBg));
        }
        else{
            tv.setBackgroundColor(tv.getResources().getColor(R.color.colorTabSelected));
        }

        tv.setText(dataDTO.getFavorites_title());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftItemClickListener!=null&&curSelectedPosition!=tempPos){
                    //每次点击左边的分类之后都别忘了改成选中效果
                    curSelectedPosition=tempPos;

                    leftItemClickListener.onLeftItemClick(dataDTO);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPagerCatagories selectedPagerCatagories) {
        List<SelectedPagerCatagories.DataDTO> data = selectedPagerCatagories.getData();
        if(data!=null){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();

            //设置刚进去还没点击任何页面的时候，第一次默认加载的页面
            if(mData.size()>0){
                leftItemClickListener.onLeftItemClick(mData.get(curSelectedPosition));
            }
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



    public interface OnLeftItemClickListener{
        void onLeftItemClick(SelectedPagerCatagories.DataDTO dataDTO);
    }

    public void setLeftItemClickListener(OnLeftItemClickListener leftItemClickListener) {
        this.leftItemClickListener = leftItemClickListener;
    }
}
