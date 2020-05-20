package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SalesVo implements Serializable {

    /**总金额*/
    private String total;

    /**周同比*/
    private String onWeek;

    /**日同比*/
    private String onDay;

    /**当天销售额*/
    private String toDays;
}
