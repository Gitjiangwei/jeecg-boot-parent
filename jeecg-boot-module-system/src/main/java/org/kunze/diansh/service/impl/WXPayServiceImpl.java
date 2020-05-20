package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.HttpRequest;
import org.jeecg.common.util.MD5Util;
import org.kunze.diansh.WxPayAPI.WXPay;
import org.kunze.diansh.WxPayAPI.WXPayConstants;
import org.kunze.diansh.WxPayAPI.WXPayUtil;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.service.IWXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

@Service
public class WXPayServiceImpl extends ServiceImpl<OrderMapper,Order> implements IWXPayService {

    @Autowired
    private WeChatPayProperties weChatPayProperties;

    @Autowired
    private OrderMapper orderMapper;


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
        //添加或更新支付记录(参数跟进自己业务需求添加)
        //int flag = this.addOrUpdatePaymentRecord(orderNum, payAmount,.....);
        int flag = 1;
        if(flag < 0){
            resultMap.put("returnCode", "FAIL");
            resultMap.put("returnMsg", "此订单已支付！");
            out.println("【小程序支付】 此订单已支付！");
        }else if(flag == 0){
            resultMap.put("returnCode", "FAIL");
            resultMap.put("returnMsg", "支付记录生成或更新失败！");
            out.println("【小程序支付】 支付记录生成或更新失败！");
        }else{
            Map<String,String> resMap = null;
            try {
                resMap = this.xcxUnifieldOrder(orderId, weChatPayProperties.TRADE_TYPE_JSAPI, payAmount,openId);
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
        }
        return resultMap;
    }



    /**
     * 小程序支付统一下单
     */
    private Map<String,String> xcxUnifieldOrder(String orderNum,String tradeType, double payAmount,String openid) throws Exception{
        //封装参数
        SortedMap<String,String> paramMap = new TreeMap<String,String>();
        paramMap.put("appid", weChatPayProperties.getWxAppAppId());
        paramMap.put("mch_id", weChatPayProperties.getMchId());
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", "TEST ORDER");
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", "1");
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
                if (map == null) {
                    return Result.error("支付错误");
                } else if ("FAIL".equals(map.get("trade_state"))) {
                    return Result.error((String) map.get("return_msg"));
                }
                if ("SUCCESS".equals(map.get("trade_state"))) {

                    return Result.ok("支付成功");
                } else if ("CLOSED".equals(map.get("trade_state"))) {
                    return Result.error("订单已关闭");
                } else if ("PAYERROR".equals(map.get("trade_state"))) {

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
                    return Result.error("二维码超时");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("支付错误");
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




    // 获取openID
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
