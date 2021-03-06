package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.BeSimilarSpuVo;
import org.kunze.diansh.controller.vo.CategorySpuVo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.modelData.SpuDetailModel;
import org.kunze.diansh.entity.modelData.SpuModel;

import java.util.List;
import java.util.Map;

public interface SpuMapper extends BaseMapper<Spu> {

    List<SpuModel> qrySpuList(Spu spu);

    List<Spu> qrySpuLists(Spu spu);

    List<SpuBrandVo> qrySpuBrand();

    int saveSpu(Spu spu);

    List<SpuBrandVo> qrySpuBrands(@Param("categoryId") String categoryId);

    int updateSpu(Spu spu);

    int deleteSpu(String userName,@Param("spuList")List spuList);

    List<SpuModel> querySpuById(@Param("cateId") String cateId,@Param("shopId") String shopId,@Param("isFlag") String isFlag);

    Spu querySpuByIds(@Param("spuId") String spuId);

    /**
     * 商品详情页查看
     * @param spuId
     * @return
     */
    SpuDetailModel selectByPrimaryKey(@Param("spuId") String spuId);

    /**
     * 根据三级分类查询Spu的ID
     * @param cid3
     * @return
     */
    List<String> selectCid3SpuByIds(@Param("ci3") String cid3,@Param("spuId") String spuId,@Param("shopId") String shopId);


    /**
     * 查询相似商品
     * @param spus
     * @return
     */
    List<BeSimilarSpuVo> selectSimilarSpu(@Param("spuList") List<String> spus);


    /**
     * 首页分类商品
     * @param spus
     * @return
     */
    List<CategorySpuVo> selectCategorySpu(@Param("spuList") List<String> spus);


    /***
     * 首页全文检索
     * @param key
     * @param shopId
     * @return
     */
    List<BeSimilarSpuVo> selectSpuTitleLike(@Param("key") String key,@Param("shopId") String shopId);

    /**
     * 更新商品上架状态 0下架，1上架
     * @param saleable 商品状态
     * @param spuList 商品id
     * @param shopId 店铺id的集合
     * @return
     */
    Integer updateSpuSaleable(@Param("saleable")String saleable,@Param("spuList")List spuList,@Param("shopId")String shopId);

    /**
     * 通过条形码检索sku
     * @param barCode
     * @return
     */
    List<Map<String,Object>> getSkusByBarCode(@Param("barCode")String barCode);
}
