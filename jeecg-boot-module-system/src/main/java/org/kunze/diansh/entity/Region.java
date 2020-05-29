package org.kunze.diansh.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * 省市区
 */
@Data
@TableName("kz_region")
public class Region {

    //主键
    private Integer id;
    //名称
    private String name;
    //父节点
    private Integer pid;
    //地名简称
    @TableField(value = "sname")
    private String sName;
    //区域等级
    private Integer Level;
    //区域编码
    private String cityCode;
    //邮政编码
    private String yzCode;
    //组合名称
    private String merName;
    //经纬度
    private Float lat;
    //拼音
    private String pinYin;
    //子集合
    @TableField(select = false)
    private List children;
}
