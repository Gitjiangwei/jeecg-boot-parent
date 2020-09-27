package org.kunze.diansh.pojo.vo.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页轮播图展示实体
 *
 * @author 姜伟
 * @date 2020/7/17
 */
@Data
@ApiModel("首页展示轮播图片传输实体")
public class HomeWheelVO implements Serializable {
    /**
     * 首页Id
     */
    @ApiModelProperty("轮播图Id")
    private String wheelId;

    /**
     * 轮播图片
     */
    @ApiModelProperty("轮播图URL")
    private String wheelImages;

    /**
     * 播放顺序
     */
    @ApiModelProperty("轮播图播放顺序")
    private String wheelNo;

    /**
     * 图片跳转地址URL
     */
    @ApiModelProperty("图片跳转地址URL")
    private String wheelUrl;

    /**
     * 轮播图名称
     */
    @ApiModelProperty("轮播图名称")
    private String wheelName;

    /**
     * 图片类型 0：广告 1：新商品
     */
    @ApiModelProperty("图片类型 0：广告 1：新商品 ")
    private String wheelIsflag;

    /**
     * 超市名称
     */
    @ApiModelProperty("超市名称")
    private String shopName;
}
