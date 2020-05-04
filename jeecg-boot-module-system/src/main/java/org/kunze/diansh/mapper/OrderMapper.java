package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.OrderDetail;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 插入订单数据
     * @param order 订单数据
     * @return 受影响的行数
     */
    Integer insertOrder(Order order);

    /**
     * 插入订单详情数据
     * @param orderDetail 订单详情数据
     * @return 受影响的行数
     */
    Integer insertOrderDetail(OrderDetail orderDetail);

    /**
     * 查询店铺当天的订单
     * @param shopId 店铺id
     * @return 订单数
     */
    Integer selectShopOrderNum(@Param("shopId") String shopId);

    /**
     * 查询订单数据 根据订单状态 无状态时查询当前用户的所有订单
     * @param status 订单状态
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    List<Order> selectOrderByStatus(@Param("status") String status,@Param("userID") String userID,@Param("shopID") String shopID);

    /**
     * 根据订单id查询订单详细数据
     * @param orderId 订单id
     * @return
     */
    List<OrderDetail> selectOrderDetailById(@Param("orderId") String orderId);


    /***
     * 修改订单状态
     * @param orderId 订单id
     * @param orderStatus 订单状态
     * @return
     */
    int updateOrderStatus(@Param("orderStatus") String orderStatus, @Param("orderId") String orderId);


    /***
     * 根据订单ID查询订单信息
     * @param orderId
     * @return
     */
    Order selectById(@Param("orderId") String orderId);
}
