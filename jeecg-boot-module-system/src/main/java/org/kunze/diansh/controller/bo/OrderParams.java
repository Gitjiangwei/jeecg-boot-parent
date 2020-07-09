package org.kunze.diansh.controller.bo;


import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.util.EmptyUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 创建订单参数对象
 */
@Data
@Accessors(chain = true)
public class OrderParams {

    @NotBlank(message = "用户参数不能为空！")
    private String userID; //用户id
    @NotBlank(message = "店铺参数不能为空！")
    private String shopId; //店铺id

    private String aid; //地址id
    @NotBlank(message = "配送方式不能为空！")
    private String pick_up; //配送方式 1自提 2商家配送
    @NotBlank(message = "配送费不能为空！")
    private String postFree; //配送费
    @NotEmpty(message = "cids不能为空！")
    private List<Map<String,Object>> cids; //购物车商品的集合 包含skuid 和商品数量

    private Integer payType; //付款类型 0.微信 1.支付宝
    private String buyerMessage; //备注
}
