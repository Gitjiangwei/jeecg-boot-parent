package org.kunze.diansh.controller.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DealInfoBo implements Serializable {

    private String shopId; //超市Id

    private String dateFlag;//汇总信息类型 0月/1日

    private String startTime; //开始时间

    private String endTime; //结束时间
}
