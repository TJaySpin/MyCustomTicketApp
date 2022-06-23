package com.example.mytaobaounion.Adapter;

import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mytaobaounion.Model.Domain.SelectedContent;
import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.Constant;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedContentAdapter extends RecyclerView.Adapter<SelectedContentAdapter.InnerHolder> {
    private List<SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mData=new ArrayList<>();
    private OnContentItemClickListener contentItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO dataDTO = mData.get(position);
        holder.setData(dataDTO);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentItemClickListener!=null){
                    contentItemClickListener.OnContentItemClick(dataDTO);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent contents) {
        if(contents.getCode()== Constant.SUCCESS_CODE){
            List<SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> dataDTOList = contents.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            mData.clear();
            mData.addAll(dataDTOList);
            notifyDataSetChanged();
        }
    }


    public class InnerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.selected_cover)
        public ImageView cover;
        @BindView(R.id.selected_title_tv)
        public TextView title;
        @BindView(R.id.selected_original_price)
        public TextView original_price;
        @BindView(R.id.seleced_off_price)
        public TextView off_price;
        @BindView(R.id.selected_buy_btn)
        public TextView buy_btn;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO data){
            title.setText(data.getTitle());

            String cover_url = UrlUtils.getCoverPath(data.getPict_url());
            Glide.with(itemView.getContext()).load(cover_url).into(cover);

            if(TextUtils.isEmpty(data.getCoupon_click_url())){
                original_price.setText("晚了，没有优惠券了");
                buy_btn.setVisibility(View.GONE);
            }
            else{
                original_price.setText("原价："+itemView.getContext().getResources().getString(R.string.text_selected_origin_price,data.getZk_final_price()));
                //别忘了设置回来
                buy_btn.setVisibility(View.VISIBLE);
            }

            if(TextUtils.isEmpty(data.getCoupon_info())){
                off_price.setVisibility(View.GONE);
            }else{
                off_price.setVisibility(View.VISIBLE);
                off_price.setText(data.getCoupon_info());
            }

        }
    }


    public interface OnContentItemClickListener{
        void OnContentItemClick(IBaseInfo data);
    }

    public void setContentItemClickListener(OnContentItemClickListener contentItemClickListener) {
        this.contentItemClickListener = contentItemClickListener;
    }
}
