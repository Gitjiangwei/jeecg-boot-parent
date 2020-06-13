package org.alipay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.alipay.bean.AlipayOrder;
import org.apache.ibatis.annotations.Param;

public interface AlipayMapper extends BaseMapper<AlipayOrder> {

    /**
     * 添加支付宝交易记录
     * @param alipayOrder
     * @return
     */
    Integer insertAlipay(AlipayOrder alipayOrder);

    /**
     * 更新支付宝交易记录
     * @param alipayOrder
     * @return
     */
    Integer updateAlipay(AlipayOrder alipayOrder);

    /**
     * 根据id查询支付宝交易记录
     * @param alipayOrder
     * @return
     */
    AlipayOrder selectAlipayById(@Param("alipay") AlipayOrder alipayOrder);
}
