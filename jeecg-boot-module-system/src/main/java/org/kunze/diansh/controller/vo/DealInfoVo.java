package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class DealInfoVo implements Serializable {

    private String id; //主键
    private String payment; //订单交易额
    private String okPaymentTotal; //交易完成的钱和订单数
    //private String okTotal; //交易完成的单量
    private String refundPaymentTotal; //退款金额和订单数
    //private String refundTotal; //退款单量
    private String totalPayment; //月总交易额
    private String serviceFee; //服务费/手续费
    private String createTime; //创建时间
    private String serviceChange; //手续费

}
