package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.*;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.entity.modelData.SpuDetailModel;
import org.kunze.diansh.entity.modelData.SpuModel;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuDetailMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.ISpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements ISpuService {


    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 商品查询
     *
     * @param spuVo
     * @return
     */
    @Override
    public PageInfo<SpuModel> qrySpuList(SpuVo spuVo,Integer pageNo,Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuVo,spu);
        List<SpuModel> spuModelList  = spuMapper.qrySpuList(spu);
        return new PageInfo<SpuModel>(spuModelList);
    }

    /**
     * 商品查询
     *
     * @param spuVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<SpuBo> qrySpuLists(SpuVo spuVo, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuVo,spu);
        List<Spu> spuModelList  = spuMapper.qrySpuLists(spu);
        List<SpuBo> spuBos = new ArrayList<SpuBo>();
        for(Spu item : spuModelList){
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(item,spuBo);
            spuBos.add(spuBo);
        }
        return new PageInfo<SpuBo>(spuBos);
    }

    /**
     * 通过商品分类Id查询相关商品的详细信息
     * @param cateId 商品分类Id
     * @return
     */
    @Override
    public PageInfo<SpuModel> querySpuById(String cateId,Integer pageNo,Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<SpuModel> spuList = spuMapper.querySpuById(cateId);
        return new PageInfo<SpuModel>(spuList);
    }

    /**
     * 查询品牌以作下拉框使用
     *
     * @return
     */
    @Override
    public List<SpuBrandVo> qrySpuBrand() {
        return spuMapper.qrySpuBrand();
    }

    /**
     * 根据商品分类查询品牌
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<SpuBrandVo> qrySpuBrands(String categoryId) {
        return spuMapper.qrySpuBrands(categoryId);
    }

    /**
     * 添加商品
     *
     * @param spuBo
     * @return
     */
    @Override
    public Boolean saveSpu(SpuBo spuBo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Boolean flag = false;
        String userName = "";
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }
        //1、将SpuBo中的数据放进Spu实体中，添加Spu表
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);
        spu.setUpdateName(userName);
        int resultSpu = spuMapper.saveSpu(spu);
        if(resultSpu>0){
            //2、将SpuBo中的SpuDetail数据放进SpuDetail中，添加SpuDetail表
            SpuDetail spuDetail = spuBo.getSpuDetail();
            spuDetail.setSpuId(spu.getId());
            int resultSpuDetail = spuDetailMapper.saveSpuDetail(spuDetail);
            List<Sku> skuList = new ArrayList<Sku>();
            List<Stock> stockList = new ArrayList<Stock>();
            for(SkuVo item : spuBo.getSkuVos()){
                Sku sku = new Sku();
                Stock stock = new Stock();

                sku.setId(UUID.randomUUID().toString().replace("-",""));
                stock.setSkuId(sku.getId());
                stock.setStock(item.getStock());
                sku.setSpuId(spu.getId());
                sku.setTitle(spu.getTitle());
                sku.setEnable(item.getEnable());
                sku.setImages(item.getImages());
                BigDecimal ordPrice = new BigDecimal(item.getPrice());
                BigDecimal newPrice = ordPrice.multiply(new BigDecimal(100));
                sku.setPrice(newPrice.toString());
                sku.setIndexes(item.getIndexes());
                sku.setOwnSpec(item.getOwnSpec());
                sku.setEnable(item.getEnable());
                sku.setUpdateName(userName);
                skuList.add(sku);
                stockList.add(stock);

            }
            int resultSku = skuMapper.updateSku(skuList);
            int resultStock = stockMapper.updateStock(stockList);
            if(resultSpuDetail > 0 && resultSku > 0 && resultStock > 0){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @Override
    public Boolean updateSpu(SpuBo spuBo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isFlag = false;
        String userName = "";//操作人
        if(sysUser!=null){
            userName = sysUser.getRealname();
        }

        Spu spu = new Spu(); //商品
        BeanUtils.copyProperties(spuBo,spu);
        spu.setUpdateName(userName);
        int updateSupResult = spuMapper.updateSpu(spu);
        updateSupResult = 1;
        if(updateSupResult > 0){
            int resultSpuDetail = -1;
            if(null != spuBo.getSpuDetail()){
                SpuDetail spuDetail = spuBo.getSpuDetail();
                spuDetail.setSpuId(spu.getId());
                resultSpuDetail = spuDetailMapper.updateSpuDetail(spuDetail); //更新
            }
            if(null != spuBo.getSkuVos()){
                List<Sku> skuList = new ArrayList<Sku>();
                List<Stock> stockList = new ArrayList<Stock>();
                for(SkuVo skuVo : spuBo.getSkuVos()){
                    Sku sku = new Sku();
                    Stock stock = new Stock(); //库存

                    sku.setId(skuVo.getId());
                    stock.setSkuId(sku.getId());
                    stock.setStock(skuVo.getStock());
                    sku.setSpuId(spu.getId());
                    sku.setTitle(spu.getTitle());
                    sku.setEnable(skuVo.getEnable());
                    sku.setImages(skuVo.getImages());
                    BigDecimal ordPrice = new BigDecimal(skuVo.getPrice());
                    BigDecimal newPrice = ordPrice.multiply(new BigDecimal(100));
                    sku.setPrice(newPrice.toString());
                    sku.setIndexes(skuVo.getIndexes());
                    sku.setOwnSpec(skuVo.getOwnSpec());
                    sku.setEnable(skuVo.getEnable());
                    sku.setUpdateName(userName);
                    skuList.add(sku);
                    stockList.add(stock);
                }
                int resultSku = skuMapper.updateSku(skuList);
                int resultStock = stockMapper.updateStock(stockList);
                if(resultSpuDetail > 0 && resultSku > 0 && resultStock > 0){
                    isFlag = true;
                }
            }
        }
        return isFlag;
    }

    /**
     * 删除商品
     * @param spuBo
     * @return
     */
    @Override
    public Boolean deleteSpu(SpuBo spuBo) {
        boolean resultFlag = false;
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);
        int deleteSpuFlag = spuMapper.deleteSpu(spu);
        if(deleteSpuFlag > 0){
            resultFlag = true;
        }
        return resultFlag;
    }

    /**
     * 商品详情页查看
     *
     * @param spuId
     * @return
     */
    @Override
    public SpuDetailVo selectByPrimaryKey(String spuId) {
        SpuDetailVo spuDetailVo = new SpuDetailVo();
        if(spuId == null || spuId.equals("")){
            return null;
        }else {
            SpuDetailModel spuDetailModel = spuMapper.selectByPrimaryKey(spuId);
            List<Sku> skuList = skuMapper.querySkuBySpuId(spuId);
            spuDetailVo.setImages(Arrays.asList(spuDetailModel.getImages().split(",")));
            spuDetailVo.setSpuDetailModel(spuDetailModel);
            spuDetailVo.setSkus(skuList);
            return spuDetailVo;
        }

    }

    @Override
    public List<BeSimilarSpuVo> selectBySimilarSpu(String cid3, String spuId) {
        if(cid3 == null || cid3.equals("")){
            return null;
        }else if(spuId == null || spuId.equals("")){
            return null;
        }else {
            List<String> spuIds = querySpuId(cid3,spuId);
            if(spuIds==null){
                return null;
            }
            //查询相似商品
            List<BeSimilarSpuVo> similarSpuVos = spuMapper.selectSimilarSpu(spuIds);
            return similarSpuVos;
        }
    }

    /**
     * 首页分类商品
     *
     * @param cid3
     * @return
     */
    @Override
    public List<BeSimilarSpuVo> selectCategorySpu(String cid3) {
        if(cid3 == null || cid3.equals("")){
            return null;
        }else {
            List<String> spuIds = querySpuId(cid3,"");
            if(spuIds==null){
                return null;
            }
            List<BeSimilarSpuVo> similarSpuVos = spuMapper.selectSimilarSpu(spuIds);
            return similarSpuVos;
        }
    }

    private List<String> querySpuId(String cid3,String spuId){
            List<String> stringList = spuMapper.selectCid3SpuByIds(cid3, spuId);
            if(stringList.size()==0){
                return null;
            }
            List<String> a = new ArrayList<String>();
            int mun = 4;
            for (int i = 0; i < mun; i++) {
                if (a.size() == 4) {
                    break;
                }
                if ((i + 1) == mun && a.size() != 4) {
                    mun++;
                }
                //获取0至spuId集合总数之间的随机数
                int random = new Random().nextInt(stringList.size());
                Boolean flag = false;
                if (a.size() != 0) {
                    for (int j = 0; j < a.size(); j++) {
                        if (random == Integer.parseInt(a.get(j))) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        a.add(String.valueOf(random));
                    }
                } else {
                    a.add(String.valueOf(random));
                }
            }
        //获取集合中的spuId值
        List<String> spuIds = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++) {
            spuIds.add(stringList.get(Integer.parseInt(a.get(i))));
        }
        return spuIds;
    }
}
