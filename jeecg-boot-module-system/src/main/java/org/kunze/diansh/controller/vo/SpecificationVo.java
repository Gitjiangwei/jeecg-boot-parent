package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpecificationVo implements Serializable {


    //规格模板所属商品分类id
    private String categoryId;


    //规格参数模板，json格式
    private String specifications;
}
