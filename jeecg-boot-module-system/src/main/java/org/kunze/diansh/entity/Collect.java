package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

//收藏表实体类
@Data
public class Collect implements Serializable {

    private String id;//主键id
    @NotBlank(message = "用户id不能为空！")
    private String userId;//用户id
    @NotBlank(message = "商品id不能为空！")
    private String goodId; //商品id(spu_id)

    private Integer status;//数据状态 0可用 1不可用 默认为0

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间

    @NotBlank(message = "店铺id不能为空！")
    private String shopId; //店铺id
}
