package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class RiderAndroidVo implements Serializable {


    /**订单编号*/
    private String orderId;

    /**配送地址*/
    private String address;

    /**取货超市*/
    private String shopName;

    /**骑手名称*/
    private String riderName;

    /**发布时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**订单状态 1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭 7.已退款 8.拒绝接单*/
    private String status;

    /**配送费*/
    private String deliveryFee;
}
