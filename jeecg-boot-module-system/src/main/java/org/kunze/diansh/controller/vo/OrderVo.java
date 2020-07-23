package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderVo implements Serializable {

    /**订单编号*/
    private String orderId;

    /**实付金额*/
    private String payment;

    /**姓名+性别*/
    private String consigneeSex;

    /**联系方式*/
    private String telphone;

    /**下单时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**交易完成时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**状态*/
    //订单状态   1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
    private String status;

    //提货方式，1、自提，2、商家配送
    private String pickUp;

    /**备注*/
    private String buyerMessage;

    /**取货码*/
    private String pickNo;

    /**超市*/
    private String shopName;
}
