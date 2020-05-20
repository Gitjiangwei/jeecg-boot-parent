package org.kunze.diansh.entity.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.Serializable;

@Data
// 该实体配置了微信支付的
// 基础参数
@Configuration
@PropertySource(value = "classpath:application-dev.yml")
public class WeChatPayProperties implements Serializable {

    @Value("${WeChatPay.wxapp.appid}")
    private String wxAppAppId;

    @Value("${WeChatPay.wxapp.mch_id}")
    private String mchId;

    @Value("${WeChatPay.wxapp.apiKey}")
    private String apiKey;

    @Value("${WeChatPay.wxapp.appSecret}")
    private String appSecret;

    //微信支付类型
    //NATIVE--原生支付
    //JSAPI--公众号支付-小程序支付
    //MWEB--H5支付
    //APP -- app支付
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_MWEB = "MWEB";
    public static final String TRADE_TYPE_APP = "APP";



    //小程序发起申请接口
    public static final String WX_PAY_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //获取openId
    public final String GET_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    //查询订单支付状态地址
    public final String QUERY_WX_PAY_STATUS = "https://api.mch.weixin.qq.com/pay/orderquery";

    //小程序支付回调地址
    public final String WX_PAY_NOTIFY_URL = "http://image.hohodj.com/jeecg-boot/kunze/wechatpay/xcxNotify";

}
