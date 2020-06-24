package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InformationVo implements Serializable {

    /**月交易额度*/
    private String moneyMoney;

    /**配送费*/
    private String moneyPostfree;

    /**总额*/
    private String totalMoney;

    /**当日交易额*/
    private String toDayMoney;

    /**日配送费*/
    private String toDayPostFree;

    /**当日盈利*/
    private String toDayTotalPrice;

    /**订单量*/
    private String orderNum;

    /**商品数量*/
    private String spuNum;
}
