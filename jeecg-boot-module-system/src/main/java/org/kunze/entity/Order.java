package org.kunze.entity;

import java.io.Serializable;

public class Order implements Serializable {

    //订单id
    private String orderId;

    //总金额，单位为分
    private String totalPay;

    //实付金额。单位:分。如:20007，表示:200元7分
    private String actualPay;

    //支付类型id
    private String promotionIds;

    //支付类型，1、在线支付，2、货到付款
    private String paymentType;

    //邮费。单位:分。如:20007，表示:200元7分
    private String postFee;

    //订单创建时间
    private String createTime;

    //物流名称
    private String shippingName;

    //物流单号
    private String shippingCode;

    //用户id
    private String userId;

    //买家留言
    private String buyerMessage;

    //买家昵称
    private String buyerNick;

    //地址Id
    private String receiverId;

    //买家是否已经评价,0未评价，1已评价
    private String buyerRate;

    //发票类型(0无发票1普通发票，2电子发票，3增值税发票)
    private String invoiceType;

    //订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
    private String sourceType;
}
