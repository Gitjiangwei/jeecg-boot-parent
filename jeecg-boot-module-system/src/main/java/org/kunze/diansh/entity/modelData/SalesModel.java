package org.kunze.diansh.entity.modelData;

import lombok.Data;

import java.io.Serializable;

/***
 * 销售额
 */
@Data
public class SalesModel implements Serializable {

    /**总销售额*/
    private String total;

    /**上周销售额*/
    private String lastWeek;

    /**本周销售额*/
    private String week;

    /**今天额度*/
    private String today;

    /**昨天额度*/
    private String yestToday;
}
