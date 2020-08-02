package org.kunze.diansh.controller.vo;

import lombok.Data;
import org.kunze.diansh.salesTicket.SalesTicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * 相似商品
 */
@Data
public class BeSimilarSpuVo implements Serializable {

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

    /**SkuId*/
    private String skuId;

    //sku规格
    private String skuSpec;

}
