package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class OrderDetailVo {

    /**订单Id*/
    private String orderId;

    /**下单时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    //配送信息
    private DistributionVo distributionVo;

    /**买家留言*/
    private String buyerMessage;

    /**数量*/
    private String saleNum;

    /**总金额*/
    private String saleSum;

    /**实收*/
    private String practical;

    /**配送费*/
    private String postFree;

    /**订单中商品信息*/
    private List<OrderSpuVo> orderSpuVos;
}
