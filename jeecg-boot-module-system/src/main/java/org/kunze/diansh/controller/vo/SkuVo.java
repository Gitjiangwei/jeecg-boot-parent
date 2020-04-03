package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuVo implements Serializable {

    //sku Id
    private String id;

    //spu Id
    private String spuId;

    //商品的图片，多个图片以‘,’分割
    private String images;

    //销售价格，单位为分
    private String price;

    //特有规格属性在spu属性模板中的对应下标组合
    private String indexes;

    //sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
    private String ownSpec;

    //是否有效，0无效，1有效
    private String enable;

    //库存
    private String stock;
}
