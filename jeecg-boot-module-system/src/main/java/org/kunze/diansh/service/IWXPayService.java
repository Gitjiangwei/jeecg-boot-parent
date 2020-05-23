package org.kunze.diansh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.entity.Order;

import java.util.Map;

public interface IWXPayService  extends IService<Order> {

    //小程序统一下单
    Map<String, String> wxAppCreatePayOrder(String orderId, String money, String code);

    //查询微信订单状态
    Result qryWxOrderStatus(String outTradeNo);

    //获取openId
    Map<String, Object> getOpenId(String js_code);
}
