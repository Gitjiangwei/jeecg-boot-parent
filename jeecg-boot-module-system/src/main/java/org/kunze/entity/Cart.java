package org.kunze.entity;

import java.io.Serializable;

//购物车
public class Cart implements Serializable {


    //主键id
    private String id;

    //用户id
    private String userId;

    //总价格
    private String cartPrice;

    //总数量
    private String cartNum;

    //状态
    private String status;
}
