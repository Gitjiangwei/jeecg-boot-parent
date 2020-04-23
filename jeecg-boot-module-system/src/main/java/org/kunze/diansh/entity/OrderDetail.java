package org.kunze.diansh.entity;

import lombok.Data;

@Data
//订单详情实体
public class OrderDetail {

    //订单详情主键
    private String id;

    //订单id
    private String orderId;

    //商品详情id
    private String skuId;

    //购买数量
    private Integer num;

    //商品标题
    private String title;

    //商品动态属性键值对
    private String ownSpec;

    //价格
    private Integer price;

    //商品图片
    private String image;
}
