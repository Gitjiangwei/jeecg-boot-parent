package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.kunze.diansh.entity.OrderDetail;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RiderSendVo implements Serializable {

    /**
     * 主键id
     */
    private String id;

    /**
     * 骑手id
     */
    private String riderId;

    /**
     * 骑手名字
     */
    private String riderName;

    /**
     * 商家名字
     */
    private String businessName;

    /**
     * 商家地址
     */
    private String businessAddress;

    /**
     * 买家名字
     */
    private String buyerName;

    /**
     * 买家地址
     */
    private String buyerAddress;

    /**
     * 买家电话
     */
    private String buyerPhone;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 下单时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    /**
     * 配送费
     */
    private String sendPrice;

    /**
     * 预计收入
     */
    private String income;

    /**
     * 订单价格
     */
    private String orderPrice;

    /**
     * 骑手状态:1.接单 2已取件 3送达
     */
    private Integer riderState;

    /**
     * 订单状态:0.待抢单1.待取送2.待完成3.已完成
     */
    private Integer orderState;

    /**
     * 送达时间
     */
    private String sendTime;

    /**
     * 超时时间
     */
    private String outTime;

    /**
     * 是否转派 0否 1是
     */
    private Integer isTurn;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //订单创建时间
    private Date createTime;
    /**
     * 更多订单:1.历史订单 2异常单
     */
    private Integer orderHistory;

    /**
     * 距离米
     */
    private String distance;

    /**
     * 取单号
     */
    private String pickNo;

    /**
     * 订单ID
     */
    private String oderId;
    /**
     * 更新时间
     *
     * **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 订单详情
     *
     * **/
    private List<OrderDetail>  orderDetail;

}
