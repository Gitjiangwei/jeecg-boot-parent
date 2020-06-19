package org.alipay.controller;

import cn.hutool.core.util.NumberUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.alipay.bean.AlipayBean;
import org.alipay.config.AlipayPropertiesConfig;
import org.alipay.paysdk.AliPayApi;
import org.alipay.service.IAlipayService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.NumberUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.RoundingMode;

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

    @Autowired
    private IOrderService orderService;

    /**
     * 支付宝App支付统一下单
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/alipay_pay")
    @ApiOperation("支付宝App支付统一下单")
    @AutoLog("支付宝App支付统一下单")
    public Result alipay(@RequestBody @Valid AlipayBean alipayBean, BindingResult bindingResult) throws AlipayApiException {
        Result result = new Result();
        //参数校验
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }

        Order order = orderService.selectOrderById(alipayBean.getOut_trade_no(),alipayBean.getUserId(),alipayBean.getShopId());

        if(null == order){
            return Result.error("发起支付时出现错误！");
        }
        String totalPrice = NumberUtil.add(order.getAmountPayment(),order.getPostFree()).toString();
        if(!alipayBean.getTotal_amount().equals(totalPrice)){
            return Result.error("非法访问，请求已关闭！");
        }

        //除以100 保留两位小数 四舍五入模式
        String totalAmount = NumberUtil.div(totalPrice,"100",2, RoundingMode.HALF_UP).toString();

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setBody(alipayBean.getBody());
        model.setSubject(alipayBean.getSubject());
        model.setOutTradeNo(alipayBean.getOut_trade_no());
        model.setTimeoutExpress(AlipayBean.timeout_express);
        model.setTotalAmount(totalAmount);
        model.setProductCode(AlipayBean.product_code);

        String resultStr = AliPayApi.appPayToResponse(model, AlipayPropertiesConfig.getKey("notify_url")).getBody();
        result.setSuccess(true);
        result.setResult(resultStr);
        return result;
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

    /**
     * 支付宝退款
     * @param alipayBean
     * @return
     */
    @PostMapping(value = "/refundAlipay")
    @ApiOperation("支付宝退款")
    @AutoLog("支付宝退款")
    public Result refundAlipay(@RequestBody @Valid AlipayBean alipayBean, BindingResult bindingResult){
        Result result = new Result();
        //参数校验
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();//支付宝退款退款对象
        model.setOutTradeNo(alipayBean.getOut_trade_no());
        model.setRefundAmount(alipayBean.getTotal_amount()); //退款(元)
        try {
            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(model);
            if(response.isSuccess()){
                //调用成功！
                //更新订单状态为已退款
                orderService.updateOrderStatu("7",response.getOutTradeNo());
                result.success("退款成功！");
            }else {
                return result.error500("调用支付宝失败，请重试！");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return result.error500("调用支付宝时出现异常，请重试！");
        }
        return result;
    }



}
