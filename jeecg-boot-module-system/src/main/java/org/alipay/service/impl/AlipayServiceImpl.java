package org.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.alipay.bean.AlipayBean;
import org.alipay.config.AlipayPropertiesConfig;
import org.alipay.service.IAlipayService;
import org.alipay.util.AlipayUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlipayServiceImpl implements IAlipayService {

    /**
     * 支付宝统一下单
     * @param alipayBean
     * @return
     * @throws AlipayApiException
     */
    @Override
    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        return AlipayUtil.connect(alipayBean);
    }

    /**
     * 支付宝异步回调通知
     *
     * @param conversionParams
     * @return
     */
    @Override
    public String notify(Map<String, String> conversionParams) {
        System.out.println("==================支付宝异步请求逻辑处理");
        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;

        try {
            //调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(conversionParams,
                    AlipayPropertiesConfig.getKey("alipay_public_key"),
                    AlipayPropertiesConfig.getKey("charset"),
                    AlipayPropertiesConfig.getKey("sign_type"));

        } catch (AlipayApiException e) {
            System.out.println("==================验签失败 ！");
            e.printStackTrace();
        }

        //对验签进行处理
        if (signVerified) {
            //验签通过
            //获取需要保存的数据
            String appId = conversionParams.get("app_id");//支付宝分配给开发者的应用Id
            String notifyTime = conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
            String gmtCreate = conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
            String gmtPayment = conversionParams.get("gmt_payment");//交易付款时间
            String gmtRefund = conversionParams.get("gmt_refund");//交易退款时间
            String gmtClose = conversionParams.get("gmt_close");//交易结束时间
            String tradeNo = conversionParams.get("trade_no");//支付宝的交易号
            String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
            String outBizNo = conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
            String buyerLogonId = conversionParams.get("buyer_logon_id");//买家支付宝账号
            String sellerId = conversionParams.get("seller_id");//卖家支付宝用户号
            String sellerEmail = conversionParams.get("seller_email");//卖家支付宝账号
            String totalAmount = conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
            String receiptAmount = conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
            String invoiceAmount = conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
            String buyerPayAmount = conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
            String tradeStatus = conversionParams.get("trade_status");// 获取交易状态


        } else {  //验签不通过
            System.out.println("==================验签不通过 ！");
            return "fail";
        }
        return "";
    }
}
