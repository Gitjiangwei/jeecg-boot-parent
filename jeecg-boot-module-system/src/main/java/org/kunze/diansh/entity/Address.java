package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址表
 */
@Data
public class Address implements Serializable {

    //主键id
    private String id;

    //用户id
    private String userId;

    //收货人
    private String consignee;

    //1:先生 0:女士
    private int consigneeSex;

    //省
    private String province;

    //市
    private String city;

    //区/县
    private String county;

    //街道
    private String street;

    //联系方式
    private String telphone;

    //邮编
    private String postcode;

    //地址类型
    private String addType;

    //默认地址
    private String isDefault;

    //状态 0:可用 1：删除
    private int status;


}
