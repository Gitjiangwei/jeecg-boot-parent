package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.CommonUtil;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.service.ISkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Autowired
    private SkuMapper skuMapper;


    @Override
    public List<Sku> querySkuBySpuId(String spuId) {
        return skuMapper.querySkuBySpuId(spuId);
    }

    /**
     * 查询商品展示的详细信息 通过spuId
     * @param spuId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectSkuInfoBySpuId(String spuId) {
        return CommonUtil.toCamel(skuMapper.selectSkuInfoBySpuId(spuId));
    }
}
