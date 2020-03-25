package org.kunze.entity;

import java.io.Serializable;

/**
 * 购物车商品中间表
 */
public class CartGood implements Serializable {

    //商品id
    private String goodId;

    //购物车Id
    private String cartId;

    //商品价格
    private String goodPrice;

    //商品数量
    private String goodNum;
}
