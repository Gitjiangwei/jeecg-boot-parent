package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.SpuDetailVo;
import org.kunze.diansh.controller.vo.SpuFeaturesDetailVo;
import org.kunze.diansh.controller.vo.SpuFeaturesListVo;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.entity.SpuDetail;
import org.kunze.diansh.entity.SpuFeatures;
import org.kunze.diansh.entity.modelData.SpuFeaturesListModel;

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
    List<SpuFeaturesVo> selectFeatures(String shopId,String more, String shopType);


    /***
     * 每日特卖商品详情
     * @param featuresId
     * @return
     */
    SpuFeaturesDetailVo selectFeaturesDetail(String featuresId);


    /***
     * 后台查询每日特卖商品
     * @param spuFeaturesVo
     * @return
     */
    PageInfo<SpuFeaturesListModel> selectFeatList(SpuFeaturesListVo spuFeaturesVo, Integer pageNo, Integer pageSize);


    /***
     * 删除特卖商品
     * @param ids
     * @return
     */
    Boolean delFeatures(String ids);

    /**
     * 检索同一天是否有相同的特卖商品 是返回true 否返回 false
     * @param skuId
     * @param featuresTime
     * @return
     */
    Boolean querySkuIdentical(String skuId,String featuresTime);


    /**
     * 修改特卖商品
     * @param spuFeaturesBo
     * @return
     */
    Boolean updateSpuFeatures(SpuFeaturesBo spuFeaturesBo);
}
