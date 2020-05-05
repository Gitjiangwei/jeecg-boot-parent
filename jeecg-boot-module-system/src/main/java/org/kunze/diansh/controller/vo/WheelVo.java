package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WheelVo implements Serializable {

    /**轮播图片id*/
    private String wheelId;

    /**轮播图片地址*/
    private String wheelImages;

    /**轮播顺序*/
    private String wheelNo;

    /**轮播图片所展示的端*/
    private String wheelPort;

    /**轮播图片所跳转的地址*/
    private String wheelUtl;

    /**图片是否启动*/
    private String isFlag;

    /**图片名称*/
    private String wheelName;

    /**超市Id*/
    private String shopId;

    /**超市名称*/
    private String shopName;
}
