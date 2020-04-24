package org.kunze.diansh.controller.bo;

import lombok.Data;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;

import java.io.Serializable;
import java.util.List;

/**
 * 添加和修改商品时使用的入参
 */
@Data
public class SpuBo implements Serializable {

    private String id;

    private String brandId;

    private String cid1;// 1级类目

    private String cid2;// 2级类目

    private String cid3;// 3级类目

    private String title;// 标题

    private String subTitle;// 子标题

    private String image;//商品单图片

    private String images;//商品多图片

    private SpuDetail spuDetail;

    private List<SkuVo> skuVos;
}
