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

public interface SpuMapper extends BaseMapper<Spu> {

    List<SpuModel> qrySpuList(Spu spu);

    List<Spu> qrySpuLists(Spu spu);

    List<SpuBrandVo> qrySpuBrand();

    int saveSpu(Spu spu);

    List<SpuBrandVo> qrySpuBrands(@Param("categoryId") String categoryId);

    int updateSpu(Spu spu);

    int deleteSpu(@Param("spu") Spu spu);

    List<SpuModel> querySpuById(@Param("cateId") String cateId);

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
    List<String> selectCid3SpuByIds(@Param("ci3") String cid3,@Param("spuId") String spuId);


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
}
