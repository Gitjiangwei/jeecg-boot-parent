package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.OrderComsumer;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.HttpRequest;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.RedisUtil;
import org.kunze.diansh.WxPayAPI.MiniprogramConfig;
import org.kunze.diansh.WxPayAPI.WXPay;
import org.kunze.diansh.WxPayAPI.WXPayConstants;
import org.kunze.diansh.WxPayAPI.WXPayUtil;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.service.IOrderService;
import org.kunze.diansh.service.IStockService;
import org.kunze.diansh.service.IWXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static java.lang.System.out;

@Service
public class WXPayServiceImpl extends ServiceImpl<OrderMapper,Order> implements IWXPayService {

    @Autowired
    private WeChatPayProperties weChatPayProperties;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IStockService iStockService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisUtil redisUtil;

    private MiniprogramConfig config;
    private WXPay wxpay;

    @Autowired
    public WXPayServiceImpl(){
        try {
            config = MiniprogramConfig.getInstance();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信配置初始化错误", e);
        }
    }


    /**
     * 发起小程序支付
     * @param orderId
     * @param money
     * @param code
     * @return
     */
    @Override
    public Map<String, String> wxAppCreatePayOrder(String orderId, String money, String code) {
        out.println("【小程序支付】 统一下单开始, 订单编号="+orderId);

        SortedMap<String, String> resultMap = new TreeMap<String, String>();

        // 向微信请求获取openId
        Map<String, Object> openIdMap = getOpenId(code);
        if (openIdMap == null) {
            resultMap.put("returnCode", "FAIL");
            resultMap.put("returnMsg", "向微信请求数据时出现错误！");
            return resultMap;
        }
        String openId = (String) openIdMap.get("openid");
        if (openId == null) {
            resultMap.put("returnCode", "FAIL");
            resultMap.put("returnMsg", "获取openId失败！");
            return resultMap;
        }

        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = 0.01;
        Map<String,String> resMap = null;
        try {
            resMap = this.xcxUnifieldOrder(orderId, weChatPayProperties.TRADE_TYPE_JSAPI, money,openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(WXPayConstants.SUCCESS.equals(resMap.get("return_code")) && WXPayConstants.SUCCESS.equals(resMap.get("result_code"))){
            resultMap.put("appId", weChatPayProperties.getWxAppAppId());
            resultMap.put("timeStamp", Long.toString(WXPayUtil.getCurrentTimestamp()));
            resultMap.put("nonceStr", WXPayUtil.generateNonceStr());
            resultMap.put("package", "prepay_id="+resMap.get("prepay_id"));
            resultMap.put("signType", "MD5");
            resultMap.put("sign", createSign(resultMap,weChatPayProperties.getApiKey()));
            resultMap.put("returnCode", "SUCCESS");
            resultMap.put("returnMsg", "OK");
            out.println("【小程序支付】统一下单成功，返回参数:"+resultMap);
        }else{
            resultMap.put("returnCode", resMap.get("return_code"));
            resultMap.put("returnMsg", resMap.get("return_msg"));
            out.println("【小程序支付】统一下单失败，失败原因:"+resMap.get("return_msg"));
        }
        return resultMap;
    }


    /**
     * 小程序支付统一下单
     * @param orderId 订单号
     * @param tradeType 交易类型
     * @param money 金额
     * @param openid
     * @return
     * @throws Exception
     */
    private Map<String,String> xcxUnifieldOrder(String orderId,String tradeType, String money,String openid) throws Exception{
        Instant now = Instant.now();
        Date nowDate = Date.from(now);
        // 支付有限时间为14分钟30秒
        now = now.plusSeconds((60*14)+30);
        Date endDate = Date.from(now);
        //封装参数
        SortedMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("appid", weChatPayProperties.getWxAppAppId());
        paramMap.put("mch_id", weChatPayProperties.getMchId());
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("time_start", DateUtils.date2Str(nowDate,new SimpleDateFormat("yyyyMMddHHmmss")));
        paramMap.put("time_expire", DateUtils.date2Str(endDate,new SimpleDateFormat("yyyyMMddHHmmss")));
        paramMap.put("body", "小程序快捷支付");
        paramMap.put("out_trade_no", orderId);
        paramMap.put("total_fee", money);
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", weChatPayProperties.WX_PAY_NOTIFY_URL);
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid",openid);
        paramMap.put("sign", WXPayUtil.generateSignature(paramMap,weChatPayProperties.getApiKey()));
        //转换为xml
        String xmlData = WXPayUtil.mapToXml(paramMap);
        //请求微信后台，获取预支付ID
        String resXml = HttpRequest.sendPost(WeChatPayProperties.WX_PAY_UNIFIED_ORDER, xmlData);
        return WXPayUtil.xmlToMap(resXml);
    }



    /**
     * 查询支付状态
     *
     * @param outTradeNo 订单号
     * @return
     */
    public Result qryWxOrderStatus(String outTradeNo) {
        //订单号合法性校验
        //组装请求参数
        Map wxPayRequestMap = this.createWxAppParam(outTradeNo);

        //将要发送的参数，转换为xml

        String xmlParam = null;
        try {
            xmlParam = WXPayUtil.generateSignedXml(wxPayRequestMap, weChatPayProperties.getApiKey());

            int i = 0;
            //轮询
            while (true) {
                //查询支付状态
                String resultXml = HttpRequest.sendPost(weChatPayProperties.QUERY_WX_PAY_STATUS,xmlParam);
                Map map = WXPayUtil.xmlToMap(resultXml);
                redisUtil.set(outTradeNo+"__"+DateUtils.formatTime(new Date()),map,15,true,null);
                if (map == null) {
                    return Result.error("支付错误");
                } else if ("FAIL".equals(map.get("trade_state"))) {
                    return Result.error((String) map.get("return_msg"));
                }
                if ("SUCCESS".equals(map.get("trade_state"))) {

//                    //支付成功 开始业务处理
//                    Order order = orderMapper.selectById(outTradeNo);
//                    //订单状态为未支付才开始业务处理
//                    if("1".equals(order.getStatus().toString())){
//                        //更新订单状态为【已支付】
//                        orderService.updateOrderStatus(outTradeNo,order.getAmountPayment());
//                        //从队列中删除订单
//                        OrderComsumer.removeToOrderDelayQueue(outTradeNo);
//                        //更新商品库存
//                        iStockService.updateStockNum(outTradeNo);
//                    }

                    return Result.ok("支付成功");
                } else if ("CLOSED".equals(map.get("trade_state"))) {
                    out.println("============================================================================================订单已关闭");
                    return Result.error("订单已关闭");
                } else if ("PAYERROR".equals(map.get("trade_state"))) {
                    out.println("============================================================================================支付失败");
                    return Result.error("支付失败");
                }

                //间隔三秒
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                i++;
                //防止无限的轮询下去，增加一个计数器
                if (i >= 20) {
                    out.println("============================================================================================支付失败");
                    return Result.error("二维码超时");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("支付错误");
        }
    }


    /**
     * 生成微信小程序支付查询参数
     * @param wxOrderId 微信订单号
     * @return 参数
     */
    private Map createWxAppParam(String wxOrderId){
        Map<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("appid", weChatPayProperties.getWxAppAppId());
        paramMap.put("mch_id", weChatPayProperties.getMchId());
        paramMap.put("out_trade_no",wxOrderId);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            paramMap.put("sign", WXPayUtil.generateSignature(paramMap,weChatPayProperties.getApiKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramMap;
    }



    /**
     * 微信小程序退款
     * @param orderNo 商户订单id
     * @param amount  金额
     * @param orderStatus 订单状态
     * @return 返回map（已做过签名验证），具体数据参见微信退款API
     */
    public Result doRefund(String orderNo, Integer amount,String orderStatus) {
        Result result = new Result(){};
        HashMap<String, String> data = new HashMap<>();
        data.put("appid", weChatPayProperties.getWxAppAppId());
        data.put("mch_id", weChatPayProperties.getMchId());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        data.put("out_trade_no", orderNo);
        data.put("out_refund_no", orderNo);
        data.put("total_fee", amount.toString());
        data.put("refund_fee", amount.toString());
        data.put("notify_url",weChatPayProperties.WX_PAY_REFUND_URL);
        try{
            data.put("sign", WXPayUtil.generateSignature(data,weChatPayProperties.getApiKey()));
        }catch (Exception e){
            e.printStackTrace();
            log.error("调用退款接口失败,订单号:"+orderNo);
        }
        try {
            Map<String, String> resultMap = wxpay.refund(data);
            if(resultMap.get("return_code").equals(WXPayConstants.SUCCESS)&& resultMap.get("result_code").equals(WXPayConstants.SUCCESS)){
                //退款成功时，在此处更新退款状态
                //更新订单状态为已退款
                orderService.updateOrderStatu(orderStatus,resultMap.get("out_trade_no"));
                result.setSuccess(true);
                result.setMessage("退款成功！");
            }else{
                //退款失败时，记录退款失败信息
                result.setSuccess(false);
                result.setMessage("退款异常！错误原因："+resultMap.get("err_code_des"));
            }
            System.out.println(resultMap);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 获取openID
    @Override
    public Map<String, Object> getOpenId(String code) {
        Map<String, Object> map = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", weChatPayProperties.getWxAppAppId());
        params.put("secret", weChatPayProperties.getAppSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String retStr = HttpRequest.sendPost(weChatPayProperties.GET_OPEN_ID_URL, this.createLinkString(params));
        if (retStr == null) {
            return null;
        }
        map = stringToHashMap(retStr);
        if (map == null || map.get("openid") == null || map.get("session_key") == null) {
            return null;
        }
        return map;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }


    /**
     * 字符串转换map集合
     * @param retStr
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> stringToHashMap(String retStr) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = null;
        try {
            map = mapper.readValue(retStr, HashMap.class);
        } catch (JsonParseException e) {
            return null;
        } catch (JsonMappingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return map;
    }

    /**
     * 创建签名Sign
     *
     * @param key
     * @param parameters
     * @return
     */
    public String createSign(SortedMap<String,String> parameters,String key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator<?> it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            if(entry.getValue() != null || !"".equals(entry.getValue())) {
                String v = String.valueOf(entry.getValue());
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }






}
