package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * sku表
 */
@Data
public class Sku implements Serializable {

    //sku Id
    private String id;

    //spu Id
    private String spuId;

    //商品标题
    private String title;

    //商品的图片，多个图片以‘,’分割
    private String images;

    //当前价格
    private String newPrice;

    //销售价格，单位为分
    private String price;

    //特有规格属性在spu属性模板中的对应下标组合
    private String indexes;

    //sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
    private String ownSpec;

    //是否有效，0无效，1有效
    private String enable;

    //添加时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    //修改时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastUpdateTime;

    //操作人
    private String updateName;

    //是否为特卖 0：否 1：是
    private String isFeatures;

    //购买时的商品数量 只用于业务逻辑处理（计算订单价格）
    private Integer num;

    private String shopId;

    private String stock;

    //是否上架 0下架/1上架
    private String saleable;
}
