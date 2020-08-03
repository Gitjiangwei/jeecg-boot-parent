package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.OrderDetail;
import java.util.List;
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     *
     * 根据订单Id查询订单详情
     * **/
    List<OrderDetail> queryOrderDetail(@Param(value = "orderId") String orderId);
}
