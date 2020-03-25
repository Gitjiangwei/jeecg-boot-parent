package org.kunze.entity;

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

    //先生/女士
    private String consigneeSex;

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

    //状态 0:可用 1：删除
    private String status;


}
