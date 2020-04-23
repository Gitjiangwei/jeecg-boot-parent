package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品信息类
 */
@Data
public class Commodity implements Serializable {


    /**商品名称*/
    private String spuName;

    /**单价*/
    private String unitPrice;

    /**商品数量*/
    private String spuNum;

    /**商品总价*/
    private String unitPriceTotle;


    public Commodity(String spuName, String unitPrice, String spuNum, String unitPriceTotle) {
        super();
        this.spuName = spuName;
        this.unitPrice = unitPrice;
        this.spuNum = spuNum;
        this.unitPriceTotle = unitPriceTotle;
    }

}
