package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.controller.bo.SpuBo;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.controller.vo.SpuBrandVo;
import org.kunze.diansh.controller.vo.SpuVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Spu;
import org.kunze.diansh.entity.SpuDetail;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.entity.modelData.SpuModel;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuDetailMapper;
import org.kunze.diansh.mapper.SpuMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.ISpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
     * 通过商品分类Id查询相关商品的详细信息
     * @param cateId 商品分类Id
     * @return
     */
    @Override
    public List<Spu> querySpuById(String cateId) {
        return spuMapper.querySpuById(cateId);
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
                resultSpuDetail = spuDetailMapper.updateSpuDetail(spuDetail); //更新商品详情
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


}
