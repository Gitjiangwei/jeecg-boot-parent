package org.kunze.diansh.controller.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 创建订单参数实体
 *
 * @author 姜伟
 * @date 2020/9/17
 */
@ApiModel("创建订单参数对象")
@Data
@Accessors(chain = true)
public class OrderParams {


    /**
     * 用户参数Id
     */
    @ApiModelProperty(value = "用户Id", required = true)
    @NotBlank(message = "用户参数不能为空！")
    private String userID;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", required = true, example = "1001")
    @NotBlank(message = "店铺参数不能为空！")
    private String shopId;

    /**
     * 地址Id
     */
    @ApiModelProperty(value = "地址Id", required = true)
    @NotBlank(message = "地址id不能为空")
    private String aid;

    /**
     * 配送方式 1自提 2商家配送
     */
    @ApiModelProperty(value = "配送方式", required = true, example = "1")
    @NotBlank(message = "配送方式不能为空！")
    @Pattern(regexp = "^[1-2]]", message = "配送参数错误")
    private String pick_up;

    /**
     * 配送费
     */
    @ApiModelProperty(value = "配送费", required = true)
    @NotBlank(message = "配送费不能为空！")
    private String postFree;

    /**
     * 购物车商品的集合 包含skuid 和商品数量
     */
    @ApiModelProperty(value = "购物车商品集合", required = true)
    @NotEmpty(message = "cids不能为空！")
    private List<Map<String, Object>> cids;

    /**
     * 买家昵称
     */
    @ApiModelProperty(value = "买家昵称")
    private String buyerNick;

    /**
     * 买家预留手机号
     */
    @ApiModelProperty(value = "买家预留手机号")
    private Long buyerPhone;

    /**
     * 付款类型 0.微信 1.支付宝
     */
    @ApiModelProperty(value = "付款类型", example = "0")
    private Integer payType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String buyerMessage;

    /**
     * 1超市 2飯店
     */
    @ApiModelProperty(value = "店铺类型 1.超市 2.饭店", required = true, example = "1")
    @NotBlank(message = "店铺类型不能为空")
    @Pattern(regexp = "^[1-2]", message = "店铺类型参数错误")
    private Integer shopType;
}
