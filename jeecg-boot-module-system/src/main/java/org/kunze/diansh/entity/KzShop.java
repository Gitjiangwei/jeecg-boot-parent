package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class KzShop implements Serializable {

    //超市ID
    private String id;

    //超市名称
    private String shopName;

    //创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //是否删除 0：未删除 1：已删除
    private String status;

    //省
    private String province;

    //市
    private String city;

    //区
    private String area;

    //超市地址
    private String shopAddress;

    //超市图片
    private String image;

    //开始营业时间
    private String startBusiness;

    //打烊时间
    private String endBusiness;

    //超市负责人
    private String personCharge;

    //联系方式
    private String telphone;

    //身份证号码
    private String idenitiy;

    /**修改时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //配送费
    private Integer postFree;

    /**超市上下架：0、下架 1、上架*/
    private String isFlag;

    /**售后联系方式*/
    private String afterSale;

    //起送价
    private Integer minPrice;

    /**配送方式*/
    private String distributionModel;

}
