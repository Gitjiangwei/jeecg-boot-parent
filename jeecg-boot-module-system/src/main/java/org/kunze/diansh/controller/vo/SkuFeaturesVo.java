package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuFeaturesVo implements Serializable {

    //skuId
    private String skuId;

    //标题
    private String title;

    /**
     * sku图片
     */
    private String skuImages;

    //特卖价格
    private String featuresPrice;

    //指导价格
    private String price;

    //规格参数
    private String ownSpec;

    //库存
    private String featuresStock;
}
