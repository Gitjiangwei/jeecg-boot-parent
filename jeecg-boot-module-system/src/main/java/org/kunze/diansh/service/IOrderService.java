package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.entity.Order;

import java.util.List;


public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车集合
     * @param shopId 店铺id
     * @param userID 用户id
     * @param pick_up 配送方式 1.自提 2.商家配送
     */
    Order createOrder(String aid, List cids, String shopId, String userID,String pick_up);

    /**
     * 根据订单状态查询订单数据
     * @param status 订单状态
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    List<Order> selectOrderByStatus(String status, String userID, String shopID);


    /**
     * 根据订单ID查询订单数据
     * @param orderId 订单id
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    Order selectOrderById(String orderId,String userID,String shopID);

    /***
     * 订单支付后修改状态
     * @param orderId
     * @return
     */
    String updateOrderStatus(String orderId);


    /**
     * 订单支付后修改状态
     * @param status 状态
     * @param orderId 订单id
     * @return
     */
    String updateOrderStatus(String status,String orderId);

}
