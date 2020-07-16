package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class Distribution implements Serializable {

    /**主键id*/
    private String id;

    /**订单编号*/
    private String orderId;

    /**收货地址*/
    private String address;

    /**配送费*/
    private String deliveryFee;

    /**骑手ID*/
    private String riderId;

    /**超市ID*/
    private String shopId;

    /**发布时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**是否结算 0：未结算，1：已结算*/
    private String settlement;

    /**结算日期*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String settlementTime;

    /**取货号*/
    private String pickNo;
}
