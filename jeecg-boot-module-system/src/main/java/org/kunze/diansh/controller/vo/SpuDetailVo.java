package org.kunze.diansh.controller.vo;

import lombok.Data;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;
import org.kunze.diansh.entity.modelData.SpuDetailModel;

import java.io.Serializable;
import java.util.List;

@Data
public class SpuDetailVo implements Serializable {

    private List<String> images;

    //商品详情信息
    private SpuDetailModel spuDetailModel;

    private List<Sku> skus; //sku集合



}
