package org.kunze.diansh.pojo.vo.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图展示数据
 *
 * @author 姜伟
 * @date 2020/9/17
 */
@ApiModel("轮播图管理传输实体")
@Data
public class WheelVO implements Serializable {
    /** 轮播图片id */
    @ApiModelProperty("轮播图id")
    private String wheelId;

    /** 轮播图片地址 */
    @ApiModelProperty("轮播图地址")
    private String wheelImages;

    /** 轮播顺序 */
    @ApiModelProperty("轮播图播放顺序")
    private String wheelNo;

    /** 轮播图片所展示的端 */
    @ApiModelProperty("轮播图所展示的端 1：小程序 2：Android 3：IOS")
    private String wheelPort;

    /** 轮播图片所跳转的地址 */
    @ApiModelProperty("轮播图跳转地址")
    private String wheelUrl;

    /** 图片是否启动 */
    @ApiModelProperty("是否启动 0：是 1：否")
    private String isFlag;

    /** 图片名称 */
    @ApiModelProperty("图片名称")
    private String wheelName;

    /** 超市Id */
    @ApiModelProperty("超市Id")
    private String shopId;

    /** 超市名称 */
    @ApiModelProperty("超市名称")
    private String shopName;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /** 操作人 */
    @ApiModelProperty("操作人")
    private String updateName;
}
