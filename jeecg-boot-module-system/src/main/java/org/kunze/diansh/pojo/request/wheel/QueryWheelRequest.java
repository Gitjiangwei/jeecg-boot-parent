package org.kunze.diansh.pojo.request.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 轮播图管理传入参数
 *
 * @author 姜伟
 * @date 2020/7/17
 */
@ApiModel("轮播图管理后台传入参数")
@Data
public class QueryWheelRequest implements Serializable {
    /**
     * 图片名称
     */
    @ApiModelProperty(value = "轮播图片名称")
    private String wheelName;

    /**
     * 使用端
     */
    @ApiModelProperty(value = "使用端")
    @Pattern(regexp = "^[1-3]", message = "使用端参数错误")
    private String wheelPort;

    /**
     * 轮播图类型
     */
    @ApiModelProperty(value = "轮播图类型")
    @Pattern(regexp = "^[0-1]", message = "图片类型参数错误")
    private String wheelIsflag;

    /**
     * 超市名称
     */
    @ApiModelProperty(value = "超市名称")
    private String shopName;

    @ApiModelProperty(value = "当前页", required = true, example = "1")
    @NotNull(message = "当前页不能为空")
    private Integer pageNo;

    @ApiModelProperty(value = "每页展示数据行数", required = true, example = "10")
    @NotNull(message = "每页展示数据行数不能为空")
    private Integer pageSize;
}
