package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品规格参数模板，json格式。
 */
@Data
public class Specification implements Serializable {

    //规格模板所属商品分类id
    private String categoryId;


    //规格参数模板，json格式
    private String specifications;


    //是否删除 0：未删除 1：已删除
    private String isFlag;

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


}
