package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RiderFlow implements Serializable {

    private String ridersId;//骑手ID

    private String orderId;//订单号

    private String remark;//备注

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;//创建时间

    private Integer riderType;//骑手类型：1：超市 2：餐饮

    private Integer singleNum;//接单数

    private BigDecimal singleMoney;//单价

    private Integer paramType;//参数类型



    private BigDecimal sumMoney;//总金额

    private BigDecimal sumSingle;//总单数

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;//开始时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;//结束时间

}
