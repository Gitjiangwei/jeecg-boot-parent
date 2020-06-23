package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.OrderRecord;

import java.util.List;
import java.util.Map;

public interface OrderRecordMapper extends BaseMapper<OrderRecord> {

    /***
     * 添加记录
     * @param orderRecord
     * @return
     */
    int addOrderRecord(OrderRecord orderRecord);


    /**
     * 查询订单记录
     * @param orderId
     * @return
     */
    List<Map<String,String>> queryOrderRecord(@Param("orderId") String orderId);


    /***
     * 查询所有消息记录
     * @return
     */
    List<OrderRecord> queryOrderRecordTotal(@Param("shopId") String shopId);
}
