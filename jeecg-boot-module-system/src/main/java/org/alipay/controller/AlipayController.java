package org.alipay.controller;

import com.alipay.api.AlipayApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.alipay.bean.AlipayBean;
import org.alipay.service.IAlipayService;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "支付宝模块")
@RestController
@RequestMapping(value = "/kunze/alipay")
/**
 * 支付宝控制类
 * @Author Backlight
 */
public class AlipayController {

    @Autowired
    private IAlipayService alipayService;

    /**
     * 支付宝支付统一下单
     * @param out_trade_no 订单号
     * @param subject 订单名称
     * @param total_amount 付款金额（元）
     * @param body 商品描述
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/alipay_pay")
    @ApiOperation("支付宝支付统一下单")
    @AutoLog("支付宝支付统一下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "out_trade_no",value = "订单号"),
            @ApiImplicitParam(name = "subject",value = "订单名称"),
            @ApiImplicitParam(name = "total_amount",value = "付款金额（元）"),
            @ApiImplicitParam(name = "body",value = "商品描述"),
    })
    public String alipay(String out_trade_no, String subject, String total_amount, String body) throws AlipayApiException {

        return alipayService.aliPay(new AlipayBean()
                .setBody(body)
                .setOut_trade_no(out_trade_no)
                .setTotal_amount(new StringBuffer().append(total_amount))
                .setSubject(subject));
    }

}
