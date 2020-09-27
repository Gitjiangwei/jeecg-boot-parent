package org.kunze.diansh.pojo.request.wheel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 删除轮播图实体
 *
 * @author 姜伟
 * @date 2020/7/18
 */
@Data
@ApiModel("删除轮播图传入实体")
public class DeleteWheelRequest implements Serializable {
    /**
     * 轮播图id
     */
    @ApiModelProperty(value = "轮播图id，多个id以逗号分隔", required = true, example = "1,2,3")
    @NotBlank(message = "轮播图Id不能为空")
    private String ids;
}
