package org.alipay.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*支付实体对象*/
@Data
@Accessors(chain = true)
public class AlipayBean implements Serializable {
    /*商户订单号，必填*/
    @NotBlank(message = "订单号不能为空！")
    private String out_trade_no;
    /*订单名称，必填*/
    //@NotBlank(message = "订单名称不能为空！")
    private String subject;
    /*付款金额，必填*/
    @NotNull(message = "付款金额不能为空！")
    @DecimalMax("1000000000000")
    @DecimalMin("0")
    private Integer total_amount;
    /*商品描述，可空*/
    //@NotBlank(message = "商品描述不能为空！")
    private String body;
    //用户id
    @NotBlank(message = "用户id不能为空！")
    private String userId;
    //店铺id
    @NotBlank(message = "店铺id不能为空！")
    private String shopId;
    /**
     * 超时时间参数
     * 1m～15d。m-分钟，h-小时，d-天
     * 1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭
     */
    public static String timeout_express="10m";
    //销售产品码(可选)
    public static String product_code="QUICK_MSECURITY_PAY";

}
