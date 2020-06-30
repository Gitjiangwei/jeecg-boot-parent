package org.kunze.diansh.controller.vo;

import lombok.Data;
import org.kunze.diansh.entity.Commodity;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

@Data
public class SalesTicketVo implements Serializable {
    private ArrayList<Commodity> commodityList;

    //配送信息
    private DistributionVo distributionVo;
    /**超市名称*/
    private String shopName;

    /**数量*/
    private String saleNum;

    /**总金额*/
    private String saleSum;

    /**实收*/
    private String practical;

    /**找零*/
   // private String changes;

    /**订单号*/
    private String orders;

    //商家地址
    private String shopAddress;

    //取货方式 1、自提 2、配送
    private String pickUp;

    /**买家留言*/
    private String buyerMessage;

    /**配送费*/
    private String postFree;


}
