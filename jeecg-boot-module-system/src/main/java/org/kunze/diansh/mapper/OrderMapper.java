package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.OrderDetail;

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
     * 查询店铺订单
     * @param shopId 店铺id
     * @return 订单数
     */
    Integer selectShopOrderNum(@Param("shopId") String shopId);
}
