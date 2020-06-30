package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InformationVo implements Serializable {

    /**月交易额度*/
    private String moneyMoney;

    /**配送费*/
    //private String moneyPostfree;

    /**总额*/
    private String totalMoney;

    /*月交易成功金额*/
    private String okPayment;

    /**手续费率*/
    private String charge;

    /*月手续费*/
    private String chargeTotal;

    /**月退款金额*/
    private String refundPayment;

    /**月交易成功订单数*/
    private String okTotal;

    /**月退款订单数*/
    private String refundTotal;

    /**当日交易额*/
    private String toDayMoney;

    /**当日成功交易*/
    private String toDayOkPayment;

    /**当日手续费*/
    private String toDayChargeTotal;

    /**当日退款金额*/
    private String toDayRefundPayment;

    /**当日交易成功订单数*/
    private String toDayokTotal;

    /**当日退款订单数*/
    private String toDayRefundTotal;

    /**当日盈利*/
    private String toDayTotalPrice;

    /**订单量*/
    private String orderNum;

    /**商品数量*/
    private String spuNum;
}
