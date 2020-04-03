package org.kunze.diansh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品类目表
 */
@Data
@TableName("kz_category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;


    /**ID*/
    @TableId(type = IdType.UUID)
    private String id; //类目id
    private String name; //类目名称
    private String parentId; //父类目id,顶级类目填0
    private String isParent; //是否为父节点，0为否，1为是
    private Integer sort; //排序指数，越小越靠前
    private String index;
    private String isflag; //是否删除 0：未删除 1：已删除

}
