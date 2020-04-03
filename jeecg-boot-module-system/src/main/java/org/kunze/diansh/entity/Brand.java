package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌表
 */
@Data
public class Brand implements Serializable {

    //主键id
    private String id;

    //品牌名称
    private String name;

    //品牌图片
    private String image;

    //品牌首字母
    private String letter;

    //创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //操作人
    private String updateName;

    //是否删除 0：未删除 1：已删除
    private String isFlag;

}
