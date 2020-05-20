package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InformationVo implements Serializable {

    /**月交易额度*/
    private String moneyMoney;

    /**订单量*/
    private String orderNum;

    /**商品数量*/
    private String spuNum;
}
