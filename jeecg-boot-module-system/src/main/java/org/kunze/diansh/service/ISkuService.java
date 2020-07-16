package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.Sku;

import java.util.List;
import java.util.Map;

public interface ISkuService extends IService<Sku> {

    /**
     * 根据spuId查询相关联的sku
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuId(String spuId);

    /**
     * 查询商品展示的详细信息 通过spuId
     * @param spuId
     * @return
     */
    List<Map<String,Object>> selectSkuInfoBySpuId(String spuId);

    /***
     * 查询不是特卖商品的规格
     * @param spuId
     * @return
     */
    PageInfo<Sku> queryNotFeatSku(@Param("spuId") String spuId,Integer pageNo,Integer pageSize);

    /**
     * 添加规格
     * @param skuVo
     * @return
     */
    Boolean saveSku(SkuVo skuVo);
}
