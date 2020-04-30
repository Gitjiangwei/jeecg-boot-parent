package org.kunze.diansh.entity.modelData;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeaturesIdsModel implements Serializable {

    private String spuId;

    private String skuId;

    private String featuresPrice;
}
