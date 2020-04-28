package org.kunze.diansh.entity.modelData;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeaturesModel implements Serializable {

    //类别id
    private String featuresId;

    //商品ID
    private String spuId;

    //商品类别 1、时令水果，2、新鲜蔬菜
    private String featuresFlag;

    //权重 数值越低，排序越靠前
    private String featuresWeight;

}
