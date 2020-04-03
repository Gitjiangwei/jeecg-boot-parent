package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Sku;

import java.util.List;

public interface SkuMapper extends BaseMapper<Sku> {

    int saveSku(@Param("skuList")List<Sku> skus);
}
