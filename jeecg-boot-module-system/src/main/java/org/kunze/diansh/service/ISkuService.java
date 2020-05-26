package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Sku;

import java.util.List;

public interface ISkuService extends IService<Sku> {

    /**
     * 根据spuId查询相关联的sku
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuId(String spuId);
}
