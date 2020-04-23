package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Order;


public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车集合
     * @param shopId 店铺id
     * @param userID 用户id
     */
    Order createOrder(String aid,String[] cids,String shopId,String userID);
}
