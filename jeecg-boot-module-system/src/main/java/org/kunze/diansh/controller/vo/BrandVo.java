package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVo implements Serializable {

    private String id;

    //品牌名称
    private String name;

    //品牌Logo
    private String image;

    //品牌首字母
    private String letter;

    //分类Id
    private String kId;

}
