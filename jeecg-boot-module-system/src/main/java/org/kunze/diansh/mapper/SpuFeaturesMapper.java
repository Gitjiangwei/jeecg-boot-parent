package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.entity.SpuFeatures;

import java.util.List;

public interface SpuFeaturesMapper extends BaseMapper<SpuFeatures> {

    int saveSpuFeatures(@Param("list") List<SpuFeatures> spuFeatures);

    List<SpuFeaturesVo> selectFeatures(@Param("shopId") String shopId,@Param("more") String more);

    int updateSkuFeatures(@Param("list") List<String> skuIds,@Param("isFeatures") String isFeatures);

    /**
     * 查询热卖表中的过期的skuId
     * @return
     */
    List<String> selectFeaturesSkuId();

    /***
     * 删除过期商品的ID
     * @return
     */
    int delFeatures();

    /**
     * 查询热卖表中的skuId
     * @return
     */
    List<String> selectFeaturesSkuIds();


    /***
     * 查询没有修改状态的SkuId
     * @param skuIds
     * @return
     */
    List<String> selectSkuNotState(@Param("list") List<String> skuIds);
}
