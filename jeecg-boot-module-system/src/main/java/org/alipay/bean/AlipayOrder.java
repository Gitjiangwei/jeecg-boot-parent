package org.alipay.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
//支付宝交易记录表
public class AlipayOrder implements Serializable {

    //订单id
    private String orderId;
    //支付宝订单号
    private String tradeNo;
    //商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
    private String outBizNo;
    //买家支付宝账号
    private String buyerLogonId;
    //卖家支付宝用户号
    private String sellerId;
    //卖家支付宝账号
    private String sellerEmail;

    //订单金额:本次交易支付的订单金额，单位为人民币（元）
    private Double totalAmount;
    //实收金额:商家在交易中实际收到的款项，单位为元
    private Double receiptAmount;
    //开票金额:用户在交易中支付的可开发票的金额
    private Double invoiceAmount;
    //付款金额:用户在交易中支付的金额
    private Double buyerPayAmount;

    //通知时间
    private Date notifyTime;
    //交易创建时间
    private Date gmtCreate;
    //交易付款时间
    private Date gmtPayment;
    //交易退款时间
    private Date gmtRefund;
    //交易结束时间
    private Date gmtClose;

    /**
     * 交易状态
     * 0 : 交易创建并等待买家付款 (WAIT_BUYER_PAY)
     * 1 : 未付款交易超时关闭或支付完成后全额退款 (TRADE_CLOSED)
     * 2 : 交易支付成功 (TRADE_SUCCESS)
     * 3 : 交易结束并不可退款 (TRADE_FINISHED)
     * 除交易创建不会触发异步通知以外所有状态均会触发异步通知
     */
    private Integer tradeStatus;
}
