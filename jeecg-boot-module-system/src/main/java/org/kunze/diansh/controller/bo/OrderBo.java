package org.kunze.diansh.controller.bo;

import lombok.Data;
import org.kunze.diansh.entity.Address;
import org.kunze.diansh.entity.Cart;
import org.kunze.diansh.entity.Order;

import java.util.List;

@Data
//订单操作类
public class OrderBo {

    //地址
    private Address address;

    //购物车集合
    private List<Cart> cartList;

    //订单
    private Order order;
}
