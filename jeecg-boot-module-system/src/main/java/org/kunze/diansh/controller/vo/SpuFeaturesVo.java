package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeaturesVo implements Serializable {

    /**id*/
    private String featuresId;

    /**SpuId*/
    private String spuId;

    /**skuId*/
    private String skuId;

    /**热卖商品名称*/
    private String title;

    /**图片*/
    private String image;

    /**热卖商品价格*/
    private String featuresPrice;

    /**商品原价格*/
    private String price;

    /**热卖商品库存*/
    private String featuresStock;

    /**原始库存*/
    private String originalStock;
}
