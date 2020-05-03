package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuFeaturesVo implements Serializable {

    //skuId
    private String skuId;

    //标题
    private String title;

    //价格
    private String price;

    //规格参数
    private String ownSpec;

    //库存
    private String featuresStock;
}
