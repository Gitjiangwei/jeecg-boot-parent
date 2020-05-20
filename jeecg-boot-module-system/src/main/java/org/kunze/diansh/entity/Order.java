package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Data
//订单实体
public class Order implements Serializable,Delayed {

    //订单id
    private String orderId;

    //店铺id
    private String shopId;

    //收获地址id
    private String addressId;

    //应付金额
    private String amountPayment;

    //实付金额。单位:分。如:20007，表示:200元7分
    private String payment;

    //提货方式，1、自提，2、商家配送
    private String pickUp;

    //邮费。单位:分。如:20007，表示:200元7分
    private String postFee;

    //订单状态   1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
    private Integer status;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //订单创建时间
    private Date createTime;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        Calendar cal = Calendar.getInstance();
        cal.setTime(createTime);
        cal.add(Calendar.MINUTE,2);
        this.cancelTime = cal.getTime(); //设置取消时间为15分钟后
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //订单更新时间
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //付款时间
    private Date paymentTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //发货时间
    private Date consignTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //交易完成时间
    private Date endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //交易关闭时间
    private Date closeTime;

    //物流名称
    private String shippingName;

    //物流单号
    private String shippingCode;

    //用户单号
    private String userID;

    //买家留言
    private String buyerMessage;

    //买家昵称
    private String buyerNick;

    //买家是否已经评价,0未评价，1已评价
    private int buyerRate;

    //购物车集合
    private List<OrderDetail> odList;

    //取消时间
    private Date cancelTime;



    @Override
    public long getDelay(TimeUnit unit) {
        //取消时间 - 当前时间
        long l = unit.convert(cancelTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return l;
    }

    @Override
    public int compareTo(Delayed o) {
        //这里根据取消时间来比较，如果取消时间小的，就会优先被队列提取出来
        return this.getCancelTime().compareTo(((Order) o).getCancelTime());
    }
}
