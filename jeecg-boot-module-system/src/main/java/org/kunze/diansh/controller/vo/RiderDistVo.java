package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class RiderDistVo implements Serializable {

    //主键id
    private String id;

    /**订单编号*/
    private String orderId;

    /**配送地址*/
    private String address;

    /**配送费*/
    private String deliveryFee;

    /**骑手名称*/
    private String riderName;

    /**超市*/
    private String shopName;


    /**发布时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**是否结算 0:未结算 1：已结算*/
    private String settlement;

    /**结算时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settlementTime;


    /**取货码*/
    private String pickNo;

}
