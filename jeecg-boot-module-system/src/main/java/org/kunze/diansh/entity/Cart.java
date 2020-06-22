package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

//购物车
@Data
public class Cart implements Serializable {


    //主键id
    private String id;

    //用户id
    private String userId;

    //商品id
    private String skuid;

    //标题
    private String titile;

    //图片
    private String image;

    //商品上架时的价格
    private String createPrice;

    //是否选中
    private boolean isSelect;

    //价格
    private String cartPrice;

    //购买数量
    private Integer cartNum;

    //商品的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
    private String ownSpec;

    //状态
    private String status;

    //店铺id
    private String shopId;

}
