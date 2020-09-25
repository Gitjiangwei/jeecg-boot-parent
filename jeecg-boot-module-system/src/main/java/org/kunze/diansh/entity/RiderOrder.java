package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class RiderOrder implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 骑手ID
     * **/
    private String RiderId;
    /**
     *姓名
     * **/
    private String RiderName;
    /**
     *订单ID
     * **/
    private String orderId;
   /**
    * 配送费
    * **/
    private String sendPrice;
    /**
     *
     * 距离
     */
    private String distance;
    /**
     *
     *取货号
     */
    private Integer pickNo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;// 创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;// 修改时间


    /**
     * 骑手状态:1.接单 2已取件 3送达
     */
    private Integer riderState;

    /**
     * 订单状态:0.待抢单1.待取送2.待完成3.已完成
     */
    private Integer orderState;

    /**
     *
     * 状态
     * **/
    private String status;
}
