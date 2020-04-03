package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品品牌和分类中间表
 */
@Data
public class CategoryBrand implements Serializable {

    //商品分类id
    private String categoryId;

    //商品品牌id
    private String brandId;
}
