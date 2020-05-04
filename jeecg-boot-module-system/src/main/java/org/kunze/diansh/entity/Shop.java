package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Shop implements Serializable {

    //超市ID
    private String id;

    //超市名称
    private String shopName;

    //创建时间
    private String createTime;

    //是否删除 0：未删除 1：已删除
    private String status;

    //超市地址
    private String shopAddress;

    //超市图片
    private String image;

    //营业时间
    private String businessHours;
}
