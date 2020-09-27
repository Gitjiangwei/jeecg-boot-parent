package org.kunze.diansh.pojo.request.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 修改首页轮播图展示打开或关闭传入参数
 *
 * @author 姜伟
 * @date 2020/7/18
 */
@Data
@ApiModel("修改首页轮播图展示打开或关闭传入参数")
public class UpdateWheelStatusRequest implements Serializable {
    /**
     * 首页轮播图是否展示
     */
    @ApiModelProperty(value = "轮播图展示状态。0：启动，1：未启动", required = true, example = "0")
    @Pattern(regexp = "^[0-1]", message = "轮播图展示状态参数错误")
    @NotBlank(message = "轮播图展示状态不能为空")
    private String isFlag;

    /**
     * 轮播图id
     */
    @ApiModelProperty(value = "轮播图id，多个id以逗号分隔", required = true, example = "1,2")
    @NotBlank(message = "轮播图id不能为空")
    private String ids;
}
