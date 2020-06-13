package org.alipay.service;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.alipay.bean.AlipayBean;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
 * 支付宝业务层
 */
public interface IAlipayService {

    /**
     * 支付宝统一下单
     * @param alipayBean
     * @return
     * @throws AlipayApiException
     */
    String aliPay(AlipayBean alipayBean) throws AlipayApiException;

    /**
     * 支付宝异步回调通知
     * @param conversionParams
     * @return
     */
    String notify(Map<String, String> conversionParams);

    /**
     * 查询支付宝订单交易状态
     * @param outTradeNo 业务系统订单编号
     * @return
     */
    Byte checkAlipay(String outTradeNo);
}
