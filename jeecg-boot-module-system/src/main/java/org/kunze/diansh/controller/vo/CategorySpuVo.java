package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategorySpuVo implements Serializable {

    /**spuId*/
    private String spuId;

    /**商品名称*/
    private String spuTitle;

    /**缩略图*/
    private String image;

    /**商品现在价格*/
    private String spuNewPrice;

    /**商品原价格*/
    private String spuPrice;

    /**商品分类*/
    private String cid3;
}
