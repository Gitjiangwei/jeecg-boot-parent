package org.kunze.diansh.pojo.request.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 首页轮播图展示数据
 *
 * @author 姜伟
 * @date 2020/9/17
 */
@Data
@ApiModel("首页查询轮播图")
public class HomeWheelRequest implements Serializable {
    /** 轮播图片所展示的端 */
    @ApiModelProperty(value = "展示端 1：小程序 2：Android 3：IOS", required = true, example = "1")
    @Pattern(regexp = "^[1-3]", message = "参数错误")
    @NotBlank(message = "轮播图展示端不能为空")
    private String wheelPort;

    /** 图片名称 */
    @ApiModelProperty("轮播图名称")
    private String wheelName;

    /** 超市Id */
    @ApiModelProperty(value = "超市Id", required = true, example = "1")
    @NotBlank(message = "超市Id不能为空")
    private String shopId;

    /** 显示图片数 */
    @ApiModelProperty(value = "显示图片数", example = "5")
    private Integer pageSize;
}