package com.example.mytaobaounion.Model.Domain;

import android.text.TextUtils;

import com.example.mytaobaounion.Presenter.IBaseInfo;
import com.example.mytaobaounion.Presenter.IItemInfo;

import java.util.List;

public class HomePagerContent {

    private Boolean success;
    private Integer code;
    private String message;
    private List<DataDTO> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HomePagerContent{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTO implements IItemInfo {
        private Integer category_id;
        private Object category_name;
        private String click_url;
        private String commission_rate;
        private Integer coupon_amount;
        private String coupon_click_url;
        private String coupon_end_time;
        private Object coupon_info;
        private Integer coupon_remain_count;
        private String coupon_share_url;
        private String coupon_start_fee;
        private String coupon_start_time;
        private Integer coupon_total_count;
        private String item_description;
        private Long item_id;
        private Integer level_one_category_id;
        private String level_one_category_name;
        private String nick;
        private String pict_url;
        private long seller_id;                                                                     //这个id可能会过长，在api文档里有范例，因此不能是int


        private String shop_title;
        private SmallImagesDTO small_images;
        private String title;
        private Integer user_type;
        private long volume;
        private String zk_final_price;

        public Integer getCategory_id() {
            return category_id;
        }

        public void setCategory_id(Integer category_id) {
            this.category_id = category_id;
        }

        public Object getCategory_name() {
            return category_name;
        }

        public void setCategory_name(Object category_name) {
            this.category_name = category_name;
        }

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public Integer getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(Integer coupon_amount) {
            this.coupon_amount = coupon_amount;
        }

        public String getCoupon_click_url() {
            return coupon_click_url;
        }

        public void setCoupon_click_url(String coupon_click_url) {
            this.coupon_click_url = coupon_click_url;
        }

        public String getCoupon_end_time() {
            return coupon_end_time;
        }

        public void setCoupon_end_time(String coupon_end_time) {
            this.coupon_end_time = coupon_end_time;
        }

        public Object getCoupon_info() {
            return coupon_info;
        }

        public void setCoupon_info(Object coupon_info) {
            this.coupon_info = coupon_info;
        }

        public Integer getCoupon_remain_count() {
            return coupon_remain_count;
        }

        public void setCoupon_remain_count(Integer coupon_remain_count) {
            this.coupon_remain_count = coupon_remain_count;
        }

        public String getCoupon_share_url() {
            return coupon_share_url;
        }

        public void setCoupon_share_url(String coupon_share_url) {
            this.coupon_share_url = coupon_share_url;
        }

        public String getCoupon_start_fee() {
            return coupon_start_fee;
        }

        public void setCoupon_start_fee(String coupon_start_fee) {
            this.coupon_start_fee = coupon_start_fee;
        }

        public String getCoupon_start_time() {
            return coupon_start_time;
        }

        public void setCoupon_start_time(String coupon_start_time) {
            this.coupon_start_time = coupon_start_time;
        }

        public Integer getCoupon_total_count() {
            return coupon_total_count;
        }

        public void setCoupon_total_count(Integer coupon_total_count) {
            this.coupon_total_count = coupon_total_count;
        }

        public String getItem_description() {
            return item_description;
        }

        public void setItem_description(String item_description) {
            this.item_description = item_description;
        }

        public Long getItem_id() {
            return item_id;
        }

        public void setItem_id(Long item_id) {
            this.item_id = item_id;
        }

        public Integer getLevel_one_category_id() {
            return level_one_category_id;
        }

        public void setLevel_one_category_id(Integer level_one_category_id) {
            this.level_one_category_id = level_one_category_id;
        }

        public String getLevel_one_category_name() {
            return level_one_category_name;
        }

        public void setLevel_one_category_name(String level_one_category_name) {
            this.level_one_category_name = level_one_category_name;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPict_url() {
            return pict_url;
        }

        public void setPict_url(String pict_url) {
            this.pict_url = pict_url;
        }

        public long getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(long seller_id) {
            this.seller_id = seller_id;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public SmallImagesDTO getSmall_images() {
            return small_images;
        }

        public void setSmall_images(SmallImagesDTO small_images) {
            this.small_images = small_images;
        }

        @Override
        public String getCover() {
            return getPict_url();
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String getUrl() {
            return TextUtils.isEmpty(getCoupon_click_url()) ? getClick_url() : getCoupon_click_url();
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getUser_type() {
            return user_type;
        }

        public void setUser_type(Integer user_type) {
            this.user_type = user_type;
        }

        @Override
        public long getCouponAmount() {
            return getCoupon_amount();
        }

        @Override
        public String getFinalPrice() {
            return getZk_final_price();
        }

        @Override
        public long getVolume() {
            return volume;
        }


        public void setVolume(long volume) {
            this.volume = volume;
        }

        public String getZk_final_price() {
            return zk_final_price;
        }

        public void setZk_final_price(String zk_final_price) {
            this.zk_final_price = zk_final_price;
        }

        public static class SmallImagesDTO {
            private List<String> string;

            public List<String> getString() {
                return string;
            }

            public void setString(List<String> string) {
                this.string = string;
            }
        }
    }
}