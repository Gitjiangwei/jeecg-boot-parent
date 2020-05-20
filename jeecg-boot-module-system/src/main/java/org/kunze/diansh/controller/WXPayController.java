package org.kunze.diansh.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.OrderComsumer;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.WxPayAPI.WXPayUtil;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.properties.WeChatPayProperties;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.service.IOrderService;
import org.kunze.diansh.service.IStockService;
import org.kunze.diansh.service.IWXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static java.lang.System.out;

@RestController
@Slf4j
@Api(tags = "微信支付")
@RequestMapping(value = "/kunze/wechatpay")
public class WXPayController {

    @Autowired
    private IWXPayService iwxPayService;

    @Autowired
    private WeChatPayProperties weChatPayProperties;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IStockService iStockService;


    @ApiOperation("发起小程序支付")
    @AutoLog("发起小程序支付")
    @PostMapping(value = "/wxAppCreatePayOrder")
    public Result wxAppCreatePayOrder(HttpServletRequest request){
        String orderId = request.getParameter("orderId");
        String userId = request.getParameter("userId");
        String shopId = request.getParameter("shopId");
        String code = request.getParameter("code");
        String money = request.getParameter("money");

        Order order = iOrderService.selectOrderById(orderId,userId,shopId);
        if(null == order){
            return Result.error("发起支付时出现错误！");
        }
        if(!money.equals(order.getAmountPayment())){
            return Result.error("非法访问，请求已关闭！");
        }
        if(order.getStatus() != 1){
            return Result.ok("此订单已支付！");
        }



        //获取订单参数
        //处理订单参数是否正确
        //组装统一下单id
        //业务层请求微信服务
       // iwxPayService.wxAppCreatePayOrder(orderId,money,openId);
        Result result = new Result(){};
        Map<String,String> resultMap = iwxPayService.wxAppCreatePayOrder(orderId,money,code);
        result.setResult(resultMap);
        return result;
    }


    /**
     * 小程序支付回调Api
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/xcxNotify")
    public void xcxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        InputStream inputStream = null;
        BufferedReader in = null;
        BufferedOutputStream out = null;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();

            String s;
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }


            //解析xml成map
            Map<String, String> resultMap = new HashMap<>(16);
            resultMap = WXPayUtil.xmlToMap(sb.toString());

            //过滤空 设置 TreeMap
            SortedMap packageParams = new TreeMap<>();
            Set<String> keySet = resultMap.keySet();
            for (String key : keySet) {
                String value = resultMap.get(key);
                String v = "";
                if (StrUtil.isNotBlank(value)) {
                    v = value.trim();
                }
                packageParams.put(key, v);
            }

            //判断签名是否正确
            if (WXPayUtil.isSignatureValid(packageParams, weChatPayProperties.getApiKey())) {

                //处理业务开始
                Map requestMap = new HashMap(16);
                if ("SUCCESS".equals((String) packageParams.get("return_code"))) {

                    //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                    requestMap.put("return_code", "SUCCESS");
                    requestMap.put("return_msg", "OK");
                    System.out.println("通知签名验证成功");
                    /**
                     * 业务处理
                     */

                    String out_trade_no = (String) packageParams.get("out_trade_no");
                    //更新订单状态为【已支付】
                    iOrderService.updateOrderStatus("out_trade_no");
                    //从队列中删除订单
                    OrderComsumer.removeToOrderDelayQueue(out_trade_no);
                    //更新商品库存
                    iStockService.updateStockNum(out_trade_no);
                } else {

                    requestMap.put("return_code", "FAIL");
                    requestMap.put("return_msg", "报文为空");

                }
                String xmlParam = WXPayUtil.mapToXml(requestMap);
                out = new BufferedOutputStream(
                        response.getOutputStream());
                out.write(xmlParam.getBytes());
                out.close();
            } else {
                log.info("通知签名验证失败");
                System.out.println("通知签名验证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                in.close();
                inputStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @ApiOperation("查询微信订单状态")
    @AutoLog("查询微信订单状态")
    @PostMapping(value = "/qryWxOrderStatus")
    public Result qryWxOrderStatus(String outTradeNo){

        return iwxPayService.qryWxOrderStatus(outTradeNo);
    }




}