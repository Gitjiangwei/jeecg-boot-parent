package org.kunze.diansh.pojo.request.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 操作轮播图数据传入实体参数
 *
 * @author 姜伟
 * @date 2020/7/17
 */
@Data
@ApiModel("添加操作轮播图数据传入实体参数")
public class SaveWheelRequest implements Serializable {
    /** 轮播图片地址 */
    @ApiModelProperty(value = "轮播图地址", required = true)
    @NotBlank(message = "轮播图地址不能为空")
    private String wheelImages;

    /** 轮播顺序 */
    @ApiModelProperty(value = "轮播图播放顺序", required = true, example = "1")
    @Pattern(regexp = "^\\d+$|^\\d+[.]?\\d+$", message = "轮播图顺序只能输入数字")
    @NotBlank(message = "轮播图播放顺序不能为空")
    private String wheelNo;

    /** 轮播图片所展示的端 */
    @ApiModelProperty(value = "轮播图展示的端", required = true, example = "1")
    @Pattern(regexp = "^[1-3]", message = "轮播图展示端参数错误")
    @NotBlank(message = "轮播图展示端不能为空")
    private String wheelPort;

    /** 轮播图片所跳转的地址 */
    @ApiModelProperty(value = "轮播图跳转地址", required = true, example = "https:www.hohodaojia.com")
    @NotBlank(message = "轮播图跳转地址不能为空")
    private String wheelUrl;

    /** 图片是否启动 */
    @ApiModelProperty(value = "图片是否启动 0：启动 1：未启动", required = true, example = "1")
    @NotBlank(message = "图片是否启动状态不能为空")
    @Pattern(regexp = "^[0-1]", message = "图片是否启动参数错误")
    private String isFlag;

    /** 图片名称 */
    @ApiModelProperty(value = "图片名称", required = true, example = "图片名")
    @NotBlank(message = "图片名称不能为空")
    private String wheelName;

    /** 超市Id */
    @ApiModelProperty(value = "超市Id", required = true, example = "1")
    @NotBlank(message = "超市Id")
    private String shopId;

    /** 轮播图类型 */
    @ApiModelProperty(value = "轮播图类型", required = true, example = "1")
    @Pattern(regexp = "^[0-1]", message = "轮播图类型参数错误")
    @NotBlank(message = "轮播图类型参数不能为空")
    private String wheelIsflag;
}
