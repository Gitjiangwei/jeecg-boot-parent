package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.modules.system.entity.SysUser;
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
import org.kunze.diansh.service.IStockService;
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

    @Autowired
    private IStockService stockService;

    /**
     * 商品查询
     *
     * @param spuVo
     * @return
     */
    @Override
    public PageInfo<SpuModel> qrySpuList(SpuVo spuVo,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuVo,spu);
        List<SpuModel> spuModelList  = spuMapper.qrySpuList(spu);
        PageInfo pageInfo = new PageInfo<SpuModel>(spuModelList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
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
        Page page = PageHelper.startPage(pageNo,pageSize);
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuVo,spu);
        List<Spu> spuModelList  = spuMapper.qrySpuLists(spu);
        List<SpuBo> spuBos = new ArrayList<SpuBo>();
        for(Spu item : spuModelList){
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(item,spuBo);
            spuBos.add(spuBo);
        }
        PageInfo pageInfo = new PageInfo<SpuBo>(spuBos);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 通过商品分类Id查询相关商品的详细信息
     * @param cateId 商品分类Id
     * @return
     */
    @Override
    public PageInfo<SpuModel> querySpuById(String cateId,Integer pageNo,Integer pageSize,String shopId,String isFlag) {
        PageHelper.startPage(pageNo,pageSize);
        List<SpuModel> spuList = spuMapper.querySpuById(cateId,shopId,isFlag);
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
                if(item.getNewPrice()!=null&&!item.getNewPrice().equals("")){
                    sku.setNewPrice(new BigDecimal(item.getNewPrice()).multiply(new BigDecimal("100")).toString());
                }else {
                    sku.setNewPrice("0");
                }
                if(item.getPrice()!=null && !item.getPrice().equals("")) {
                    BigDecimal ordPrice = new BigDecimal(item.getPrice());
                    BigDecimal newPrice = ordPrice.multiply(new BigDecimal(100));
                    sku.setPrice(newPrice.toString());
                }else {
                    sku.setPrice("0");
                }
                sku.setIndexes(item.getIndexes());
                sku.setOwnSpec(item.getOwnSpec());
                sku.setEnable(item.getEnable());
                sku.setUpdateName(userName);
                skuList.add(sku);
                stockList.add(stock);

            }
            int resultSku = skuMapper.saveSku(skuList);
            int resultStock = stockMapper.saveStock(stockList);
            //stockService.addStock(stockList); //同步添加redis中的库存
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
        if(updateSupResult>0){
            isFlag = true;
        }
        updateSupResult = 1;
        if(updateSupResult > 0 && (spuBo.getSkuVos()!=null || spuBo.getSpuDetail() != null)){
            if(null != spuBo.getSpuDetail()){
                SpuDetail sdObj = spuDetailMapper.qreySpuDetail(spu.getId());
                SpuDetail spuDetail = spuBo.getSpuDetail();
                spuDetail.setSpuId(spu.getId());
                if(EmptyUtils.isEmpty(sdObj)){
                    spuDetail.setSpecTemplate("");
                    spuDetail.setIsFlag("0");
                    spuDetailMapper.saveSpuDetail(spuDetail);
                }else {
                    spuDetailMapper.updateSpuDetail(spuDetail); //更新
                }
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
                    if(skuVo.getNewPrice()!=null && !skuVo.getNewPrice().equals("")){
                        sku.setNewPrice(new BigDecimal(skuVo.getNewPrice()).multiply(new BigDecimal("100")).toString());
                    }else {
                        sku.setNewPrice("0");
                    }
                    if(skuVo.getPrice()!=null && !skuVo.equals("")) {
                        BigDecimal ordPrice = new BigDecimal(skuVo.getPrice());
                        BigDecimal newPrice = ordPrice.multiply(new BigDecimal(100));
                        sku.setPrice(newPrice.toString());
                    }else {
                        sku.setPrice("0");
                    }
                    sku.setIndexes(skuVo.getIndexes());
                    sku.setOwnSpec(skuVo.getOwnSpec());
                    sku.setEnable(skuVo.getEnable());
                    sku.setUpdateName(userName);
                    skuList.add(sku);
                    stockList.add(stock);
                }
                int resultSku = skuMapper.updateSku(skuList);
                int stockNot = stockMapper.queryStockNot(stockList.get(0).getSkuId());
                int resultStock = 0;
                if(stockNot>0) {
                    resultStock = stockMapper.updateStock(stockList);
                }else {
                    resultStock = stockMapper.saveStock(stockList);
                }
                //stockService.addStock(stockList); //同步修改redis中的库存
                if(resultSku > 0 && resultStock > 0){
                    isFlag = true;
                }
            }
        }
        return isFlag;
    }

    /**
     * 删除商品
     * @param spuList
     * @return
     */
    @Override
    public Boolean deleteSpu(List spuList) {
        boolean resultFlag = false;
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        String userName = sysUser.getUsername()==null?"":sysUser.getUsername();
        Integer updateNum = spuMapper.deleteSpu("",spuList);
        if(updateNum>0){
            spuDetailMapper.delSpuDetail(spuList);
            int result = skuMapper.delSku(spuList);
            if(result>0) {
                resultFlag = true;
            }
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
            if(spuDetailModel == null){
                return null;
            }
            List<Sku> skuList = skuMapper.querySkuBySpuId(spuId);
            spuDetailVo.setImages(Arrays.asList(spuDetailModel.getImages().split(",")));
            spuDetailVo.setSpuDetailModel(spuDetailModel);
            spuDetailVo.setSkus(skuList);
            return spuDetailVo;
        }

    }

    @Override
    public List<BeSimilarSpuVo> selectBySimilarSpu(String cid3, String spuId,String shopId) {
        if(cid3 == null || cid3.equals("")){
            return null;
        }else if(spuId == null || spuId.equals("")){
            return null;
        }else {
            List<String> spuIds = querySpuId(cid3,spuId,shopId);
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
    public List<BeSimilarSpuVo> selectCategorySpu(String cid3,String shopId) {
        if(cid3 == null || cid3.equals("")){
            return null;
        }else {
            List<String> spuIds = querySpuId(cid3,"",shopId);
            if(spuIds==null){
                return null;
            }
            List<BeSimilarSpuVo> similarSpuVos = spuMapper.selectSimilarSpu(spuIds);
            return similarSpuVos;
        }
    }


    private List<String> querySpuId(String cid3,String spuId,String shopId){
            int num = 8; //相似商品查4个 首页分类查8个
            if(!StringUtils.isEmpty(spuId)){
                num = 4;
            }
            List<String> stringList = spuMapper.selectCid3SpuByIds(cid3, spuId,shopId);
            if(stringList.size()==0){
                return null;
            }
            if (stringList.size() < num){
                return stringList;
            }
            List<String> a = new ArrayList<String>();
            int mun = num;
            for (int i = 0; i < mun; i++) {
                //如果a集合的大小等于初始变量则不继续添加
                if (a.size() == num) {
                    break;
                }
                //如果循环次数等于初始变量，但是a集合不等于初始变量，则循环次数+1
                if ((i + 1) == mun && a.size() != num) {
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

    /**
     * 更新商品上架状态 0下架，1上架
     * @param saleable 商品状态
     * @param spuList 商品id集合
     * @param shopId 店铺id
     * @return 修改是否成功
     */
    @Override
    public Boolean updateSpuSaleable(String saleable,List spuList,String shopId){
        Integer resultNum = spuMapper.updateSpuSaleable(saleable,spuList,shopId);
        if(resultNum>0){
            return true;
        }
        return false;
    }

}
