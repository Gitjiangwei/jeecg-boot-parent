package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.entity.SpuFeatures;

public interface ISpuFeaturesService extends IService<SpuFeatures> {

    /***
     * 插入商品类型
     * @param spuFeaturesBo
     * @return
     */
    Boolean saveSpuFeatures(SpuFeaturesBo spuFeaturesBo);

}
