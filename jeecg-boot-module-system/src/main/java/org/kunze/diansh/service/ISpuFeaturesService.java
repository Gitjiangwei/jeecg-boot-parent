package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.entity.SpuFeatures;

import java.util.List;

public interface ISpuFeaturesService extends IService<SpuFeatures> {

    /***
     * 插入商品类型
     * @param spuFeaturesBo
     * @return
     */
    Boolean saveSpuFeatures(SpuFeaturesBo spuFeaturesBo);


    /**
     * 首页查询热卖商品
     */
    List<SpuFeaturesVo> selectFeatures(String shopId,String more);


    /**
     * 监控每日过期特卖数据
     */
    void updateOverMonitor();

    /**
     * 监控每日特卖数据
     */
    void updateMonitor();
}
