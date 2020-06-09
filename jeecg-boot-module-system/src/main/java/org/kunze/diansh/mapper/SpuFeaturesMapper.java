package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SkuFeaturesVo;
import org.kunze.diansh.controller.vo.SpuFeaturesListVo;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.SpuFeatures;
import org.kunze.diansh.entity.modelData.SpuFeaturesDetailModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesIdsModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesListModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesModel;

import java.util.List;

public interface SpuFeaturesMapper extends BaseMapper<SpuFeatures> {

    int saveSpuFeatures(@Param("list") List<SpuFeatures> spuFeatures);

    List<SpuFeaturesVo> selectFeatures(@Param("shopId") String shopId,@Param("more") String more);

    int updateSkuFeatures(@Param("list") List<String> skuIds,@Param("isFeatures") String isFeatures);

    /***
     * 修改特卖商品状态
     * @param status
     * @return
     */
    int updateFeatures(@Param("list") List<String> list, @Param("status") String status);

    /**
     * 查询热卖表中的过期的skuId
     * @return
     */
    List<SpuFeatures> selectFeaturesSkuId();

    /***
     * 删除过期商品的ID
     * @return
     */
    int delFeatures();

    /**
     * 查询热卖表中的skuId
     * @return
     */
    List<SpuFeatures> selectFeaturesSkuIds();


    /***
     * 查询没有修改状态的SkuId
     * @param skuIds
     * @return
     */
    List<String> selectSkuNotState(@Param("list") List<String> skuIds);


    SpuFeaturesIdsModel selectByKey(@Param("id") String id);


    /***
     * 查询每日特卖商品详情基本信息
     * @param spuId
     * @return
     */
    SpuFeaturesDetailModel selectFeaturesDetail(@Param("spuId") String spuId);


    /***
     * 查询特卖商品的SKU
     * @param skuId
     * @return
     */
    SkuFeaturesVo selectFeaturesSku(@Param("skuId") String skuId);


    /***
     * 后台查询特卖商品列表
     * @param spuFeaturesVo
     * @return
     */
    List<SpuFeaturesListModel> queryFeatList(SpuFeaturesListVo spuFeaturesVo);
}
