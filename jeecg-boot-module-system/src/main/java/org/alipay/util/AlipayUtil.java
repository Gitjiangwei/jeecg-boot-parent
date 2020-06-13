package org.alipay.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.alipay.bean.AlipayBean;
import org.alipay.config.AlipayPropertiesConfig;

/**
 * 支付宝工具类
 */
public class AlipayUtil {

    public static String connect(AlipayBean alipayBean) throws AlipayApiException {
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = AlipayPropertiesConfig.getInstance();
        //2、设置请求参数
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest ();

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo(alipayBean.getOut_trade_no());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(alipayBean.getTotal_amount().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        //封装参数
        alipayRequest.setBizModel(model);

        //页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(AlipayPropertiesConfig.getKey("return_url"));
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(AlipayPropertiesConfig.getKey("notify_url"));

        //3、请求支付宝进行付款，并获取支付结果
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(alipayRequest);
        System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        //返回付款信息
        return  response.getBody();
    }


}
