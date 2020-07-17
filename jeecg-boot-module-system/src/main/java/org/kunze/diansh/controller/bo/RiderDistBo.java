package org.kunze.diansh.controller.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RiderDistBo implements Serializable {


    /**订单编号*/
    private String orderId;

    /**骑手名称*/
    private String riderName;

    /**超市ID*/
    private String shopId;

    /**是否结算*/
    private String settlement;
}
