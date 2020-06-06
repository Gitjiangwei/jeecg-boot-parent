package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.*;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.modelData.SpuModel;

import java.util.List;

public interface ISpuService extends IService<Spu> {

    /**
     * 商品查询
     * @param spuVo
     * @return
     */
    PageInfo<SpuModel> qrySpuList(SpuVo spuVo,Integer pageNo,Integer pageSize);

    /**
     * 商品查询
     * @param spuVo
     * @return
     */
    PageInfo<SpuBo> qrySpuLists(SpuVo spuVo,Integer pageNo,Integer pageSize);

    /**
     * 通过商品分类Id查询相关商品的详细信息
     * @param cateId 商品分类Id
     * @return
     */
    PageInfo<SpuModel> querySpuById(String cateId,Integer pageNo,Integer pageSize,String shopId);

    /**
     * 查询品牌以作下拉框使用
     * @return
     */
    List<SpuBrandVo> qrySpuBrand();

    /**
     * 根据商品分类查询品牌
     * @param categoryId
     * @return
     */
    List<SpuBrandVo> qrySpuBrands(String categoryId);

    /**
     * 添加商品
     * @param spuBo
     * @return
     */
    Boolean saveSpu(SpuBo spuBo);

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    Boolean updateSpu(SpuBo spuBo);

    /**
     * 根据商品id删除商品信息
    * @param spuList
     * @return
     */
    Boolean deleteSpu(List spuList);

    /**
     * 商品详情页查看
     * @param spuId
     * @return
     */
    SpuDetailVo selectByPrimaryKey(String spuId);


    /**
     * 相似商品
     * @param cid3
     * @param spuId
     * @return
     */
    List<BeSimilarSpuVo> selectBySimilarSpu(String cid3,String spuId,String shopId);

    /**
     * 首页分类商品
     * @param cid3
     * @return
     */
    List<BeSimilarSpuVo> selectCategorySpu(String cid3,String shopId);

    /**
     * 更新商品上架状态 0下架，1上架
     * @param saleable 商品状态
     * @param spuList 商品id集合
     * @param shopId 店铺id
     * @return 修改是否成功
     */
    Boolean updateSpuSaleable(String saleable,List spuList,String shopId);
}
