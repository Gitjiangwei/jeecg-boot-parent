package org.kunze.diansh.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 记录商铺汇总信息实体类
 */
@Data
@Builder
@TableName("kz_dealinfo")
public class DealInfo implements Serializable {

    private String id; //主键
    private String payment; //订单交易额
    private String postFree; //配送费
    private String okPayment; //交易完成的钱
    private String okTotal; //交易完成的单量
    private String refundPayment; //退款金额
    private String refundTotal; //退款单量
    private String totalPayment; //月总交易额
    private String serviceFee; //服务费/手续费
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间
    private String shopId; //店铺id
    private String serviceChange; //手续费
    private Integer dateFlag;//汇总信息类型 0月/1日
    private String occurrenceTime;//发生时间
}
