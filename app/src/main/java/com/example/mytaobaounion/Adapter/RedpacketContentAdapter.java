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
import com.example.mytaobaounion.Model.Domain.RedPacketContent;
import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedpacketContentAdapter extends RecyclerView.Adapter<RedpacketContentAdapter.InnerHolder> {
    private List<RedPacketContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mData=new ArrayList<>();

    private OnRedpacketItemClickListener redpacketItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redpacket_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        RedPacketContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO dataDTO = mData.get(position);
        holder.setData(dataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(redpacketItemClickListener!=null){
                    redpacketItemClickListener.OnRedpacketItemListener(dataDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(RedPacketContent redPacketContent) {
        List<RedPacketContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> data = redPacketContent.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setMoreData(RedPacketContent redPacketContent) {
        int oldsize=mData.size();
        List<RedPacketContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> data = redPacketContent.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.addAll(data);
        notifyItemRangeChanged(oldsize-1, data.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.redpacket_cover)
        public ImageView cover;
        @BindView(R.id.redpacket_title_tv)
        public TextView title;
        @BindView(R.id.redpacket_original_prise)
        public TextView originalPrice;
        @BindView(R.id.redpacket_off_price)
        public TextView offPrice;


        public InnerHolder(@NonNull View itemView)  {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        public void setData(RedPacketContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO dataDTO) {
            title.setText(dataDTO.getTitle());

            String coverUrl = UrlUtils.getCoverPath(dataDTO.getPict_url());
            Glide.with(cover.getContext()).load(coverUrl).into(cover);

            String zk_final_price = dataDTO.getZk_final_price();
            originalPrice.setText("¥"+zk_final_price);
            originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            float finalPrice = Float.parseFloat(zk_final_price)-dataDTO.getCoupon_amount();
            offPrice.setText("券后价："+String.format("%.2f",finalPrice)+"元");
        }
    }


    public interface OnRedpacketItemClickListener{
        void OnRedpacketItemListener(IBaseInfo dataDTO);
    }

    public void setRedpacketItemClickListener(OnRedpacketItemClickListener redpacketItemClickListener) {
        this.redpacketItemClickListener = redpacketItemClickListener;
    }
}
