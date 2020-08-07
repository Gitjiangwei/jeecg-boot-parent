package org.kunze.diansh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;

@Data
@TableName("kz_category_hotel")
public class CategoryHotel {

    private String id; //主键
    @NotBlank(message = "名称不能为空！")
    @TableField(value = "`name`")
    private String name; //名字
    private String image; //图片
    @NotBlank(message = "父节点id不能为空！")
    private String pid; //父节点id 顶级节点初始化0
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime; //创建时间
    @NotBlank(message = "店铺id不能为空！")
    @TableField(value = "shop_id")
    private String shopId; //店铺id
    @TableField(value = "del_flag")
    private String delFlag; //是否物理删除  0否 / 1是
    @NotBlank(message = "isParent not null!")
    @TableField(value = "is_parent")
    private String isParent; //是否为父节点 0否 / 1是
    //子集合
    @TableField(select = false)
    private List<CategoryHotel> childrenList;
    @TableField(value = "`show`")
    private Integer show; //是否在主页展示 0 否 / 1 是

}
