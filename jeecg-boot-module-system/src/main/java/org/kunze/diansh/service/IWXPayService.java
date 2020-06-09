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

    /**
     * 微信小程序退款
     * @param orderNo 商户订单id
     * @param amount  金额
     * @return 返回map（已做过签名验证），具体数据参见微信退款API
     */
    Result doRefund(String orderNo, Integer amount);
}
