package org.kunze.diansh.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.CommonUtil;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.HotelSku;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.mapper.HotelSkuMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.ISkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private HotelSkuMapper hotelSkuMapper;

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
            map1.put("PRICE",CalculationUtil.FractionalConversion(item.get("PRICE").toString()));
            map1.put("NEW_PRICE", CalculationUtil.FractionalConversion(item.get("NEW_PRICE").toString()));
            map1.put("OWN_SPEC",item.get("OWN_SPEC").toString().replace("\"",""));
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

    /**
     * 添加规格
     *
     * @param skuVo
     * @return
     */
    @Override
    public Boolean saveSku(SkuVo skuVo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userName = "";
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }
        Boolean isFlag  = false;
        if(!StringUtils.isEmpty(skuVo.getSpuId())){
            Sku sku = new Sku();
            Stock stock = new Stock();
            BeanUtils.copyProperties(skuVo,sku);
            sku.setId(UUID.randomUUID().toString().replace("-",""));
            sku.setUpdateName(userName);
            sku.setPrice(CalculationUtil.MetaconversionScore(sku.getPrice()));
            sku.setNewPrice(CalculationUtil.MetaconversionScore(sku.getNewPrice()));
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            List<Sku> skuList = new ArrayList<Sku>();
            skuList.add(sku);
            List<Stock> stockList = new ArrayList<Stock>();
            stockList.add(stock);
            int result = skuMapper.saveSku(skuList);
            int resultStock = stockMapper.saveStock(stockList);
            if(result>0&&resultStock>0){
                isFlag = true;
            }
        }
        return isFlag;
    }


    /**
     * 添加sku 类型为餐饮
     * @param hotelSku
     * @return
     */
    @Override
    public Boolean addHotelSku(HotelSku hotelSku){
        hotelSku.setPrice(NumberUtil.mul(hotelSku.getPrice(),new BigDecimal(100)));
        if(hotelSku.getNewPrice()==null)
        {
            hotelSku.setNewPrice(new BigDecimal("0"));
        }else
        {
            hotelSku.setNewPrice(NumberUtil.mul(hotelSku.getNewPrice(),new BigDecimal(100)));
        }
        int row = hotelSkuMapper.addHotelSku(hotelSku);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 修改sku 类型为餐饮
     * @param hotelSku
     * @return
     */
    @Override
    public Boolean updateHotelSku(HotelSku hotelSku){
        if(EmptyUtils.isNotEmpty(hotelSku.getPrice())){
            hotelSku.setPrice(NumberUtil.mul(hotelSku.getPrice(),new BigDecimal(100)));
        }
        if(hotelSku.getNewPrice()==null)
        {
            hotelSku.setNewPrice(new BigDecimal("0"));
        }else
        {
            hotelSku.setNewPrice(NumberUtil.mul(hotelSku.getNewPrice(),new BigDecimal(100)));
        }
        int row = hotelSkuMapper.updateHotelSku(hotelSku);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 删除sku 通过id 类型为餐饮
     * @param id
     * @return
     */
    @Override
    public Boolean delHotelSkuById(String id){
        int row = hotelSkuMapper.delHotelSkuById(id);
        if(row > 0){
            return true;
        }
        return false;
    }

    /**
     * 查询sku 通过店铺id 类型为餐饮
     * @param shopId
     * @return
     */
    @Override
    public PageInfo<Map<String,Object>> queryHotelSku(String shopId,Integer pageNo,Integer pageSize){
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Map<String,Object>> hotelSkus = hotelSkuMapper.queryHotelSku(shopId,null,null);
        List<Map<String,Object>> resultSKus=CommonUtil.toCamel(hotelSkus);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(resultSKus);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 查询sku 通过分类id 类型为餐饮
     * @param shopId
     * @param cid
     * @param saleable
     * @return
     */
    @Override
    public List<Map<String,Object>> queryHotelSkuByCid(String shopId,String cid,String saleable){
        List<Map<String,Object>> hotelSkus = hotelSkuMapper.queryHotelSku(shopId,cid,saleable);
        List<Map<String,Object>> resultSKus=CommonUtil.toCamel(hotelSkus);
        return resultSKus;
    }

    /**
     * 检索hotelSku 类型为餐饮
     * @param shopId
     * @param title
     * @return
     */
    @Override
    public PageInfo<Map<String,Object>> searchHotelSku(String shopId,String title,Integer pageNo,Integer pageSize){
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Map<String,Object>> hotelSkus = hotelSkuMapper.searchHotelSku(shopId,title);
        List<Map<String,Object>> resultSKus=CommonUtil.toCamel(hotelSkus);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>(resultSKus);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 通过id查询hotelSku 类型为餐饮
     * @param id
     * @return
     */
    public HotelSku queryHotelById(String id){
        return hotelSkuMapper.queryHotelById(id);
    }
}
