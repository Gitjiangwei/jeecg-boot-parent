package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图表
 */
@Data
public class Wheel implements Serializable {

    /**轮播图ID*/
    private String wheelId;

    /**轮播图片地址*/
    private String wheelImages;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**创建人*/
    private String updateName;

    /**是否启动：0：启动 1：未启动*/
    private String isFlag;

    /**轮播顺序*/
    private String wheelNo;

    /**轮播端口  1、H5端 2、安卓端 3、IOS端*/
    private String wheelPort;

    /**图片跳转地址URL*/
    private String wheelUrl;

    /**图片名称*/
    private String wheelName;
}
