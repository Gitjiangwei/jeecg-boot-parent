package org.kunze.diansh.entity.modelData;

import cn.hutool.core.text.replacer.StrReplacer;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpuFeaturesDetailModel implements Serializable {

    //spuId
    private String spuId;

    //三级分类Id
    private String cid3;

    //商品轮播图
    private String images;

    //商品图片
    private String image;

    //基本参数规格
    private String specifications;

    private String packingList;

    private String afterService;
}
