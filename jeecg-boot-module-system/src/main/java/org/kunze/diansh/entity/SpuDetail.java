package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * SPU详情表
 */
@Data
public class SpuDetail implements Serializable {

    private String spuId;// 对应的SPU的id

    private String description;// 商品描述

    private String specTemplate;// 商品特殊规格的名称及可选值模板

    private String specifications;// 商品的全局规格属性

    private String packingList;// 包装清单

    private String afterService;// 售后服务

    //是否删除
    private String isFlag;
}
