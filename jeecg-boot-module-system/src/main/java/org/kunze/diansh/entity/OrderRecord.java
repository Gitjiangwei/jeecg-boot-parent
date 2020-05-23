package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderRecord implements Serializable {

    /**Id*/
    private String id;

    /**订单编号*/
    private String orderId;

    /**生成时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**发生事件*/
    private String sendEvent;


    public OrderRecord(String id,String orderId,String sendEvent){
        this.id = id;
        this.orderId = orderId;
        this.sendEvent = sendEvent;
    }

}
