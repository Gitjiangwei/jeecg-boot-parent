package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class ShopVo implements Serializable {

    /**超市ID*/
    private String id;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**超市名称*/
    private String shopName;

    //省
    private String province;

    //市
    private String city;

    //区
    private String area;

    /**超市地址*/
    private String shopAddress;

    /**超市图片*/
    private String image;

    /**营业时间*/
    private String businessHours;

    /**超市负责人*/
    private String personCharge;

     /**联系方式*/
    private String telphone;

     /**身份证号码*/
    private String idenitiy;

    //配送费
    private String postFree;

    /**修改时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**地址集*/
    private String addressTotal;
}
