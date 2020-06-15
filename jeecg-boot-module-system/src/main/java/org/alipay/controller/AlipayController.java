package org.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.alipay.bean.AlipayBean;
import org.alipay.config.AlipayPropertiesConfig;
import org.alipay.paysdk.AliPayApi;
import org.alipay.service.IAlipayService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/alipay_pay")
    @ApiOperation("支付宝支付统一下单")
    @AutoLog("支付宝支付统一下单")
    public String alipay(@RequestBody @Valid AlipayBean alipayBean) throws AlipayApiException {

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setBody(alipayBean.getBody());
        model.setSubject(alipayBean.getSubject());
        model.setOutTradeNo(alipayBean.getOut_trade_no());
        model.setTimeoutExpress(AlipayBean.timeout_express);
        model.setTotalAmount(alipayBean.getTotal_amount().toString());
        model.setProductCode(AlipayBean.product_code);

        String resultStr = AliPayApi.appPayToResponse(model, AlipayPropertiesConfig.getKey("notify_url")).getBody();
        return resultStr;
    }


    /**
     *查询支付宝订单交易状态
     * @param outTradeNo
     * @return
     */
    @PostMapping(value = "/checkAlipay")
    @ApiOperation("查询支付宝订单交易状态")
    @AutoLog("查询支付宝订单交易状态")
    public Result checkAlipay(String outTradeNo){
        Result result = new Result();
        if(EmptyUtils.isEmpty(outTradeNo)){
            return result.error500("参数为空");
        }
        byte resultStatus = alipayService.checkAlipay(outTradeNo);
        result.setResult(resultStatus);
        result.setSuccess(true);
        return result;
    }

}
