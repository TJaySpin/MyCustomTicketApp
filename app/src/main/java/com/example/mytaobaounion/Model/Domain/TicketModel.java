package com.example.mytaobaounion.Model.Domain;

public class TicketModel {


    private boolean success;
    private int code;
    private String message;
    private DataDTOX data;

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

    public DataDTOX getData() {
        return data;
    }

    public void setData(DataDTOX data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TicketModel{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTOX {
        private TbkTpwdCreateResponseDTO tbk_tpwd_create_response;

        public TbkTpwdCreateResponseDTO getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseDTO tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }


        @Override
        public String toString() {
            return "DataDTOX{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }

        public static class TbkTpwdCreateResponseDTO {
            private DataDTO data;
            private String request_id;

            public DataDTO getData() {
                return data;
            }

            public void setData(DataDTO data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }


            @Override
            public String toString() {
                return "TbkTpwdCreateResponseDTO{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public static class DataDTO {
                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }


                @Override
                public String toString() {
                    return "DataDTO{" +
                            "model='" + model + '\'' +
                            '}';
                }
            }
        }
    }
}

/*public class TicketModel {


    *//**
     * success : true
     * code : 10000
     * message : 淘宝口令构建成功!
     * data : {"tbk_tpwd_create_response":{"data":{"model":"￥xhQoYC66sMX￥"},"request_id":"64jzpdn6m026"}}
     *//*

    private boolean success;
    private int code;
    private String message;
    private DataBeanX data;

    @Override
    public String toString() {
        return "TicketResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        @Override
        public String toString() {
            return "DataBeanX{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }

        *//**
         * tbk_tpwd_create_response : {"data":{"model":"￥xhQoYC66sMX￥"},"request_id":"64jzpdn6m026"}
         *//*

        private TbkTpwdCreateResponseBean tbk_tpwd_create_response;

        public TbkTpwdCreateResponseBean getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseBean tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseBean {
            @Override
            public String toString() {
                return "TbkTpwdCreateResponseBean{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            *//**
             * data : {"model":"￥xhQoYC66sMX￥"}
             * request_id : 64jzpdn6m026
             *//*


            private DataBean data;
            private String request_id;

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataBean {
                @Override
                public String toString() {
                    return "DataBean{" +
                            "model='" + model + '\'' +
                            '}';
                }

                *//**
                 * model : ￥xhQoYC66sMX￥
                 *//*

                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }
            }
        }
    }
}*/
