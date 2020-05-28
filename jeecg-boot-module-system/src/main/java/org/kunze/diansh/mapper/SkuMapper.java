package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Sku;

import java.util.List;
import java.util.Map;

public interface SkuMapper extends BaseMapper<Sku> {

    int saveSku(@Param("skuList")List<Sku> skus);

    int updateSku(@Param("skuList")List<Sku> skuList);

    Sku querySkuById(@Param("id") String skuId);

    //查询商品的基本信息 通过skuid
    Sku querySkuInfoById(@Param("id") String skuId);

    /**
     * 根据SpuId查询SKU
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuId(@Param("spuId") String spuId);

    /**
     * 查询商品展示的详细信息 通过spuId
     * @param spuId
     * @return
     */
    List<Map<String,Object>> selectSkuInfoBySpuId(@Param("spuId") String spuId);
}
