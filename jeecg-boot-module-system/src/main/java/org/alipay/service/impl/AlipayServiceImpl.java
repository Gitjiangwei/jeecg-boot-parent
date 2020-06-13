package org.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.alipay.bean.AlipayBean;
import org.alipay.bean.AlipayOrder;
import org.alipay.config.AlipayPropertiesConfig;
import org.alipay.mapper.AlipayMapper;
import org.alipay.service.IAlipayService;
import org.alipay.util.AlipayUtil;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.OrderComsumer;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.service.IOrderService;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
@Service
public class AlipayServiceImpl implements IAlipayService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AlipayMapper alipayMapper;

    @Autowired
    private IStockService iStockService;

    @Autowired
    private IOrderService orderService;


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
                    AlipayPropertiesConfig.getKey("alipay_public_key"), //支付宝公钥
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

            //获取当前交易的订单
            Order order = orderMapper.selectById(outTradeNo);

            //对比交易金额 和 商户id
            if(order!=null && totalAmount.equals(order.getAmountPayment()) && AlipayPropertiesConfig.getKey("app_id").equals(appId)){

                AlipayOrder alipayOrder = new AlipayOrder();

                //修改数据库支付宝订单表(因为要保存每次支付宝返回的信息到数据库里，以便以后查证)
                alipayOrder.setOrderId(outTradeNo);
                alipayOrder.setNotifyTime(DateUtils.str2Date(notifyTime,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));  //通知时间
                alipayOrder.setGmtCreate(DateUtils.str2Date(gmtCreate,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));  //交易创建时间
                alipayOrder.setGmtPayment(DateUtils.str2Date(gmtPayment,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));  //交易付款时间
                alipayOrder.setGmtRefund(DateUtils.str2Date(gmtRefund,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));  //交易退款时间
                alipayOrder.setGmtClose(DateUtils.str2Date(gmtClose,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));  //交易结束时间

                alipayOrder.setTradeNo(tradeNo);  //支付宝订单号
                alipayOrder.setOutBizNo(outBizNo); //商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
                alipayOrder.setBuyerLogonId(buyerLogonId); //买家支付宝账号
                alipayOrder.setSellerId(sellerId); //卖家支付宝用户号
                alipayOrder.setSellerEmail(sellerEmail); //卖家支付宝账号

                alipayOrder.setTotalAmount(Double.parseDouble(totalAmount)); //订单金额:本次交易支付的订单金额，单位为人民币（元）
                alipayOrder.setReceiptAmount(Double.parseDouble(receiptAmount)); //实收金额:商家在交易中实际收到的款项，单位为元
                alipayOrder.setInvoiceAmount(Double.parseDouble(invoiceAmount)); //开票金额:用户在交易中支付的可开发票的金额
                alipayOrder.setBuyerPayAmount(Double.parseDouble(buyerPayAmount)); //付款金额:用户在交易中支付的金额

                //除交易创建不会触发异步通知以外所有状态均会触发异步通知
                switch (tradeStatus) // 判断交易结果
                {
                    case "TRADE_FINISHED": // 交易结束并不可退款
                        alipayOrder.setTradeStatus(3);
                        break;
                    case "TRADE_SUCCESS": // 交易支付成功
                        alipayOrder.setTradeStatus(2);
                        break;
                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
                        alipayOrder.setTradeStatus(1);
                        break;
                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
                        alipayOrder.setTradeStatus(0);
                        break;
                    default:
                        break;
                }
                //记录支付宝交易记录
                this.addOrUpdAlipayInfo(alipayOrder);

                if(tradeStatus.equals("TRADE_SUCCESS")){
                    //支付成功 开始业务处理

                    //订单状态为未支付才开始业务处理
                    if("1".equals(order.getStatus().toString())){
                        //更新订单状态为【已支付】
                        orderService.updateOrderStatus(outTradeNo,buyerPayAmount);
                        //从队列中删除订单
                        OrderComsumer.removeToOrderDelayQueue(outTradeNo);
                        //更新商品库存
                        iStockService.updateStockNum(outTradeNo);
                    }
                    return "success";
                }else {
                    return "fail";
                }

            }else {
                //对比金额不通过
                return "fail";
            }

        } else {  //验签不通过
            System.out.println("==================验签不通过 ！");
            return "fail";
        }
    }


    /**
     * 向支付宝发起订单查询请求
     */
    @Override
    public Byte checkAlipay(String outTradeNo) {
        log.info("==================向支付宝发起查询，查询商户订单号为："+outTradeNo);

        try {
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
            AlipayClient alipayClient = AlipayPropertiesConfig.getInstance();
            AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
            alipayTradeQueryRequest.setBizContent("{" +
                    "\"out_trade_no\":\""+outTradeNo+"\"" +
                    "}");
            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
            if(alipayTradeQueryResponse.isSuccess()){
                byte orderFlag = 0;
                switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
                {
                    case "TRADE_FINISHED": // 交易结束并不可退款
                        orderFlag = 3;
                        break;
                    case "TRADE_SUCCESS": // 交易支付成功
                        orderFlag = 2;
                        break;
                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
                        orderFlag = 1;
                        break;
                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
                        orderFlag = 1;
                        break;
                    default:
                        break;
                }
                return orderFlag;
            } else {
                log.info("==================调用支付宝查询接口失败！");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return 0;
    }



    /**
     * 添加或更新支付宝记录
     * @param aOrder 支付包交易对象
     * @return
     */
    private void addOrUpdAlipayInfo(AlipayOrder aOrder){
        AlipayOrder alipayOrder = alipayMapper.selectAlipayById(aOrder);
        if(EmptyUtils.isEmpty(alipayOrder)){
            //没有记录时新增
            Integer result = alipayMapper.insertAlipay(aOrder);
            if(result == 0)
                System.out.println("=================================== 新增支付宝交易记录失败！ =====================================");
        }else {
            //有记录时更新记录
            alipayMapper.updateAlipay(aOrder);
        }
    }
}
