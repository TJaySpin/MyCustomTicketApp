package com.example.mytaobaounion.Model.Domain;

import android.text.TextUtils;

import com.example.mytaobaounion.Presenter.IBaseInfo;

import java.util.List;

public class SelectedContent {
    private Boolean success;
    private Integer code;
    private String message;
    private DataDTO data;

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

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SelectedContent{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTO {
        private TbkDgOptimusMaterialResponseDTO tbk_dg_optimus_material_response;

        public TbkDgOptimusMaterialResponseDTO getTbk_dg_optimus_material_response() {
            return tbk_dg_optimus_material_response;
        }

        public void setTbk_dg_optimus_material_response(TbkDgOptimusMaterialResponseDTO tbk_dg_optimus_material_response) {
            this.tbk_dg_optimus_material_response = tbk_dg_optimus_material_response;
        }


        @Override
        public String toString() {
            return "DataDTO{" +
                    "tbk_dg_optimus_material_response=" + tbk_dg_optimus_material_response +
                    '}';
        }

        public static class TbkDgOptimusMaterialResponseDTO {
            private String is_default;
            private ResultListDTO result_list;
            private Integer total_results;
            private String request_id;

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public ResultListDTO getResult_list() {
                return result_list;
            }

            public void setResult_list(ResultListDTO result_list) {
                this.result_list = result_list;
            }

            public Integer getTotal_results() {
                return total_results;
            }

            public void setTotal_results(Integer total_results) {
                this.total_results = total_results;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }


            @Override
            public String toString() {
                return "TbkDgOptimusMaterialResponseDTO{" +
                        "is_default='" + is_default + '\'' +
                        ", result_list=" + result_list +
                        ", total_results=" + total_results +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public static class ResultListDTO {
                private List<MapDataDTO> map_data;

                public List<MapDataDTO> getMap_data() {
                    return map_data;
                }

                public void setMap_data(List<MapDataDTO> map_data) {
                    this.map_data = map_data;
                }


                @Override
                public String toString() {
                    return "ResultListDTO{" +
                            "map_data=" + map_data +
                            '}';
                }

                public static class MapDataDTO implements IBaseInfo {
                    private Integer category_id;
                    private String click_url;
                    private String coupon_click_url;
                    private String coupon_end_time;
                    private String coupon_info;
                    private Integer coupon_remain_count;
                    private String coupon_start_time;
                    private Integer coupon_total_count;
                    private String event_end_time;
                    private String event_start_time;
                    private String item_url;
                    private Long num_iid;
                    private String pict_url;
                    private String reserve_price;
                    private Integer status;
                    private String title;
                    private String tk_rate;
                    private Integer type;
                    private Integer user_type;
                    private Integer volume;
                    private String zk_final_price;
                    private String zk_final_price_wap;

                    public Integer getCategory_id() {
                        return category_id;
                    }

                    public void setCategory_id(Integer category_id) {
                        this.category_id = category_id;
                    }

                    public String getClick_url() {
                        return click_url;
                    }

                    public void setClick_url(String click_url) {
                        this.click_url = click_url;
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

                    public String getCoupon_info() {
                        return coupon_info;
                    }

                    public void setCoupon_info(String coupon_info) {
                        this.coupon_info = coupon_info;
                    }

                    public Integer getCoupon_remain_count() {
                        return coupon_remain_count;
                    }

                    public void setCoupon_remain_count(Integer coupon_remain_count) {
                        this.coupon_remain_count = coupon_remain_count;
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

                    public String getEvent_end_time() {
                        return event_end_time;
                    }

                    public void setEvent_end_time(String event_end_time) {
                        this.event_end_time = event_end_time;
                    }

                    public String getEvent_start_time() {
                        return event_start_time;
                    }

                    public void setEvent_start_time(String event_start_time) {
                        this.event_start_time = event_start_time;
                    }

                    public String getItem_url() {
                        return item_url;
                    }

                    public void setItem_url(String item_url) {
                        this.item_url = item_url;
                    }

                    public Long getNum_iid() {
                        return num_iid;
                    }

                    public void setNum_iid(Long num_iid) {
                        this.num_iid = num_iid;
                    }

                    public String getPict_url() {
                        return pict_url;
                    }

                    public void setPict_url(String pict_url) {
                        this.pict_url = pict_url;
                    }

                    public String getReserve_price() {
                        return reserve_price;
                    }

                    public void setReserve_price(String reserve_price) {
                        this.reserve_price = reserve_price;
                    }

                    public Integer getStatus() {
                        return status;
                    }

                    public void setStatus(Integer status) {
                        this.status = status;
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

                    public String getTk_rate() {
                        return tk_rate;
                    }

                    public void setTk_rate(String tk_rate) {
                        this.tk_rate = tk_rate;
                    }

                    public Integer getType() {
                        return type;
                    }

                    public void setType(Integer type) {
                        this.type = type;
                    }

                    public Integer getUser_type() {
                        return user_type;
                    }

                    public void setUser_type(Integer user_type) {
                        this.user_type = user_type;
                    }

                    public Integer getVolume() {
                        return volume;
                    }

                    public void setVolume(Integer volume) {
                        this.volume = volume;
                    }

                    public String getZk_final_price() {
                        return zk_final_price;
                    }

                    public void setZk_final_price(String zk_final_price) {
                        this.zk_final_price = zk_final_price;
                    }

                    public String getZk_final_price_wap() {
                        return zk_final_price_wap;
                    }

                    public void setZk_final_price_wap(String zk_final_price_wap) {
                        this.zk_final_price_wap = zk_final_price_wap;
                    }


                    @Override
                    public String toString() {
                        return "MapDataDTO{" +
                                "category_id=" + category_id +
                                ", click_url='" + click_url + '\'' +
                                ", coupon_click_url='" + coupon_click_url + '\'' +
                                ", coupon_end_time='" + coupon_end_time + '\'' +
                                ", coupon_info='" + coupon_info + '\'' +
                                ", coupon_remain_count=" + coupon_remain_count +
                                ", coupon_start_time='" + coupon_start_time + '\'' +
                                ", coupon_total_count=" + coupon_total_count +
                                ", event_end_time='" + event_end_time + '\'' +
                                ", event_start_time='" + event_start_time + '\'' +
                                ", item_url='" + item_url + '\'' +
                                ", num_iid=" + num_iid +
                                ", pict_url='" + pict_url + '\'' +
                                ", reserve_price='" + reserve_price + '\'' +
                                ", status=" + status +
                                ", title='" + title + '\'' +
                                ", tk_rate='" + tk_rate + '\'' +
                                ", type=" + type +
                                ", user_type=" + user_type +
                                ", volume=" + volume +
                                ", zk_final_price='" + zk_final_price + '\'' +
                                ", zk_final_price_wap='" + zk_final_price_wap + '\'' +
                                '}';
                    }
                }
            }
        }
    }



}
