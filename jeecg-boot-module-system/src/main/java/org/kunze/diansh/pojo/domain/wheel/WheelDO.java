package org.kunze.diansh.pojo.domain.wheel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图列表展示实体
 *
 * @author 姜伟
 * @date 2020/7/17
 */
@Data
public class WheelDO implements Serializable {
    /** 轮播图ID */
    private String wheelId;

    /** 轮播图片地址 */
    private String wheelImages;

    /** 创建时间 */
    private Date createTime;

    /** 创建时间 */
    private Date updateTime;

    /** 创建人 */
    private String updateName;

    /** 是否启动：0：启动 1：未启动 */
    private String isFlag;

    /** 轮播顺序 */
    private String wheelNo;

    /** 轮播端口 1、H5端 2、安卓端 3、IOS端 */
    private String wheelPort;

    /** 图片跳转地址URL */
    private String wheelUrl;

    /** 图片名称 */
    private String wheelName;

    /** 图片类型 0：广告 1：新商品 */
    private String wheelIsflag;

    /** 超市id */
    private String shopId;

    /** 超市名称 */
    private String shopName;
}
