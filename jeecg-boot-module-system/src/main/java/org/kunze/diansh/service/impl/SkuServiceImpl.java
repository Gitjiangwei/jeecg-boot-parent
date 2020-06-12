package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.CommonUtil;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.service.ISkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<Map<String,Object>> skuMap = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> map = skuMapper.selectSkuInfoBySpuId(spuId);
        for(Map<String,Object> item:map){
            Map<String,Object> map1 = item;
            map1.put("PRICE",new BigDecimal(item.get("PRICE").toString()).divide(new BigDecimal("100")));
            map1.put("NEW_PRICE",new BigDecimal(item.get("NEW_PRICE").toString()).divide(new BigDecimal("100")));
            skuMap.add(map1);
        }
        return CommonUtil.toCamel(skuMap);
    }


    /***
     * 查询不是特卖商品的规格
     * @param spuId
     * @return
     */
    @Override
    public PageInfo<Sku> queryNotFeatSku(String spuId,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Sku> skuList = new ArrayList<Sku>();
        List<Sku> oldSkuList = skuMapper.queryNotFeatSku(spuId);
        if(oldSkuList.size()==0){
            return null;
        }
        for(Sku item:oldSkuList){
            Sku sku = new Sku();
            BeanUtils.copyProperties(item,sku);
            /*String ownSpec = item.getOwnSpec().substring(2,item.getOwnSpec().length()-2);*/
            String ownSpec = item.getOwnSpec().replace("\"","");
            ownSpec = ownSpec.replace("{","");
            ownSpec = ownSpec.replace("}","");
            ownSpec = ownSpec.replace("\\","");
            sku.setOwnSpec(ownSpec);
            skuList.add(sku);
        }
        PageInfo<Sku> pageInfo = new PageInfo<Sku>(skuList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }


}
