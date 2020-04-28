package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeatures implements Serializable {

    /**商品类别id*/
    private String featuresId;

    /**商品ID*/
    private String spuId;

    /**商品类别 1、时令水果，2、新鲜蔬菜*/
    private String featuresFlag;

    /**创建时间*/
    private String createTime;

    /**创建人*/
    private String createName;

    /**是否删除 0、未删除 1、已删除*/
    private String isFlag;

    /**权重 数值越低，排序越靠前*/
    private String featuresWeight;

    /**备注*/
    private String remarks;

}
