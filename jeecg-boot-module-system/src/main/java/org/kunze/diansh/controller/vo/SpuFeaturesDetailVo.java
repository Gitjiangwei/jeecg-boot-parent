package org.kunze.diansh.controller.vo;

import lombok.Data;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.modelData.SpuFeaturesDetailModel;

import java.io.Serializable;
import java.util.List;

@Data
public class SpuFeaturesDetailVo implements Serializable {

    /**轮播图*/
    private List<String> images;

    private SpuFeaturesDetailModel spuFeaturesDetailModel;

    private SkuFeaturesVo skuFeaturesVo;
}
