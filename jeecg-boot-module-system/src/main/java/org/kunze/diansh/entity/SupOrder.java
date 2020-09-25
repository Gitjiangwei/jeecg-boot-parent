package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class SupOrder implements Serializable {

    /**
     * 商家地址
     * **/
   private String shopAddress;
    /**
     * 商家名字
     * **/
    private String shopName;
    /**
     *商家电话
     * **/
     private String shopTelphone;

    /**
     * 买家地址
     * **/
    private String buyerAddress;
    /**
     *买家留言
     * **/
    private String buyerMessage;
    /**
     * 买家电话
     * **/
    private  String buyerTelphone;

    /**
     * 买家名字
     * **/
    private String buyerName;


    /**
     * 区域
     * **/
    private String area;

   /**
    *
    *取货号
    */
   private Integer pickNo;

}
