package org.alipay.bean;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/*支付实体对象*/
@Data
@Accessors(chain = true)
public class AlipayBean implements Serializable {
    /*商户订单号，必填*/
    @NonNull
    private String out_trade_no;
    /*订单名称，必填*/
    @NonNull
    private String subject;
    /*付款金额，必填*/
    @NonNull
    private StringBuffer total_amount;
    /*商品描述，可空*/
    private String body;
    /**
     * 超时时间参数
     * 1m～15d。m-分钟，h-小时，d-天
     * 1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭
     */
    private String timeout_express="10m";
    //销售产品码(可选)
    private String product_code="QUICK_MSECURITY_PAY";

    public AlipayBean() {

    }
}
