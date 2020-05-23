package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSpuVo implements Serializable {

    /**图片*/
    private String image;

    /**商品名称*/
    private String spuName;

    /**商品规格*/
    private String owenSpan;

    /**单价*/
    private String unitPrice;

    /**商品数量*/
    private String spuNum;

    /**商品总价*/
    private String unitPriceTotle;
}
