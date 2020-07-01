package org.kunze.diansh.entity.modelData;

import lombok.Data;

import java.io.Serializable;

@Data
public class DealInfoModel implements Serializable {


    private String id; //主键
    private String payment; //订单交易额
    private String okPayment; //交易完成的钱
    private String okTotal; //交易完成的单量
    private String refundPayment; //退款金额
    private String refundTotal; //退款单量
    private String totalPayment; //月总交易额
    private String serviceFee; //服务费/手续费
    private String occurrenceTime; //创建时间
    private String serviceChange; //手续费
}
