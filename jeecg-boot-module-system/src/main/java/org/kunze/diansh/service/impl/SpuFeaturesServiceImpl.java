package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.modules.system.entity.SysUser;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.*;
import org.kunze.diansh.entity.SpuFeatures;
import org.kunze.diansh.entity.modelData.SpuFeaturesDetailModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesIdsModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesListModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesModel;
import org.kunze.diansh.mapper.SpuFeaturesMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.ISpuFeaturesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SpuFeaturesServiceImpl extends ServiceImpl<SpuFeaturesMapper, SpuFeatures> implements ISpuFeaturesService {

    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;

    @Autowired
    private StockMapper stockMapper;

    /***
     * 插入商品类型
     * @param spuFeaturesBo
     * @return
     */
    @Override
    public Boolean saveSpuFeatures(SpuFeaturesBo spuFeaturesBo) {
        Boolean isFlag = false;
        spuFeaturesBo.setFeaturesPrice(new BigDecimal(spuFeaturesBo.getFeaturesPrice()).multiply(new BigDecimal("100")).toString());
        SpuFeatures spuFeatures = new SpuFeatures();
        BeanUtils.copyProperties(spuFeaturesBo,spuFeatures);
        spuFeatures.setFeaturesId(UUID.randomUUID().toString().replace("-",""));
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            spuFeatures.setCreateName(sysUser.getRealname());
        }else {
            spuFeatures.setCreateName("");
        }
        List<SpuFeatures> spuFeaturesList = new ArrayList<SpuFeatures>();
        spuFeaturesList.add(spuFeatures);
       int result = spuFeaturesMapper.saveSpuFeatures(spuFeaturesList);
        if(result>0){
            //减去原始库存
            //stockMapper.updateStockNum(Integer.valueOf(spuFeatures.getFeaturesStock()),spuFeatures.getSkuId());
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String featTime = format.format(spuFeatures.getSpecialstartTime());
            String newTime = format.format(date);
            if(featTime.equals(newTime)){
                List<String> featList = new ArrayList<String>();
                List<String> notSkuIds = new ArrayList<String>();
                featList.add(spuFeatures.getFeaturesId());
                notSkuIds.add(spuFeatures.getSkuId());
                spuFeaturesMapper.updateFeatures(featList,"1");
                spuFeaturesMapper.updateSkuFeatures(notSkuIds,"1");
                isFlag = true;
            }else {
                isFlag = true;
            }
        }
        return isFlag;
    }

    /**
     * 首页查询热卖商品
     *
     * @param shopId
     * @param more
     */
    @Override
    public List<SpuFeaturesVo> selectFeatures(String shopId, String more) {
        if(shopId != null && !shopId.equals("")){
            List<SpuFeaturesVo> spuFeaturesVos = spuFeaturesMapper.selectFeatures(shopId,Integer.valueOf(more));
            return spuFeaturesVos;
        }else {
            return null;
        }
    }


    /***
     * 每日特卖商品详情
     * @param featuresId
     * @return
     */
    @Override
    public SpuFeaturesDetailVo selectFeaturesDetail(String featuresId) {
        if(featuresId == null || featuresId.equals("")){
            return null;
        }

        SpuFeaturesIdsModel spuFeaturesIdsModel = spuFeaturesMapper.selectByKey(featuresId);
        if(spuFeaturesIdsModel.getSpuId() == null || spuFeaturesIdsModel.getSpuId().equals("")){
            return null;
        }
        SpuFeaturesDetailVo spuFeaturesDetailVo = new SpuFeaturesDetailVo();
        SpuFeaturesDetailModel spuFeaturesDetailModel = spuFeaturesMapper.selectFeaturesDetail(spuFeaturesIdsModel.getSpuId());
        spuFeaturesDetailVo.setSpuFeaturesDetailModel(spuFeaturesDetailModel);
        List<String> strings = new ArrayList<>();
        if(spuFeaturesDetailModel.getImages().contains(",")){
            strings = new ArrayList<String>(Arrays.asList(spuFeaturesDetailModel.getImages().split(",")));
        }else {
            strings.add(spuFeaturesDetailModel.getImages());
        }
        spuFeaturesDetailVo.setImages(strings);
        SkuFeaturesVo skuFeaturesVo =  spuFeaturesMapper.selectFeaturesSku(spuFeaturesIdsModel.getSkuId());
        skuFeaturesVo.setFeaturesPrice(spuFeaturesIdsModel.getFeaturesPrice());
        skuFeaturesVo.setFeaturesStock(spuFeaturesIdsModel.getFeaturesStock());
        spuFeaturesDetailVo.setSkuFeaturesVo(skuFeaturesVo);
        return spuFeaturesDetailVo;
    }

    /***
     * 后台查询每日特卖商品
     * @param spuFeaturesVo
     * @return
     */
    @Override
    public PageInfo<SpuFeaturesListModel> selectFeatList(SpuFeaturesListVo spuFeaturesVo, Integer pageNo, Integer pageSize) {
        if(spuFeaturesVo.getShopId()==null || spuFeaturesVo.getShopId().equals("")){
            return null;
        }else {
            Page page = PageHelper.startPage(pageNo, pageSize);
            List<SpuFeaturesListModel> spuFeaturesVoList = spuFeaturesMapper.queryFeatList(spuFeaturesVo);
            List<SpuFeaturesListModel> spuFeaturesListModelList = new ArrayList<SpuFeaturesListModel>();
            for(SpuFeaturesListModel item:spuFeaturesVoList){
                SpuFeaturesListModel spuFeaturesListModel = new SpuFeaturesListModel();
                BeanUtils.copyProperties(item,spuFeaturesListModel);
                spuFeaturesListModel.setFeaturesPrice(CalculationUtil.FractionalConversion(item.getFeaturesPrice()));
                String ownSpec = item.getOwnSpec().substring(1,item.getOwnSpec().length()-1);
                spuFeaturesListModel.setOwnSpec(ownSpec.replace("\"",""));
                spuFeaturesListModelList.add(spuFeaturesListModel);
            }
            PageInfo<SpuFeaturesListModel> pageInfo = new PageInfo<SpuFeaturesListModel>(spuFeaturesListModelList);
            pageInfo.setTotal(page.getTotal());
            return pageInfo;
        }
    }

    /***
     * 删除特卖商品
     * @param ids
     * @return
     */
    @Override
    public Boolean delFeatures(String ids) {
        Boolean isflag = false;
        if(ids != null && !ids.equals("")){
            char a = ids.charAt(ids.length()-1);
            if(a == ','){
                ids = ids.substring(0,ids.length()-1);
            }
            if(ids == null || ids.equals("")){
                return false;
            }
            List<String> stringList = new ArrayList<String>();
            if(ids.contains(",")){
                stringList = new ArrayList<String>(Arrays.asList(ids.split(",")));
            }else {
                stringList.add(ids);
            }
            int result = spuFeaturesMapper.delFeatures(stringList);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }

    /**
     * 检索同一天是否有相同的特卖商品 是返回true 否返回 false
     *
     * @param skuId
     * @param featuresTime
     * @return
     */
    @Override
    public Boolean querySkuIdentical(String skuId, String featuresTime) {
        Boolean isFlag = false;
        int result = spuFeaturesMapper.querySkuIdentical(skuId,featuresTime);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 修改特卖商品
     *
     * @param spuFeaturesBo
     * @return
     */
    @Override
    public Boolean updateSpuFeatures(SpuFeaturesBo spuFeaturesBo) {
        Boolean isFlag = false;
        spuFeaturesBo.setFeaturesPrice(new BigDecimal(spuFeaturesBo.getFeaturesPrice()).multiply(new BigDecimal("100")).toString());
        SpuFeatures spuFeatures = new SpuFeatures();
        BeanUtils.copyProperties(spuFeaturesBo,spuFeatures);
        int result = spuFeaturesMapper.updateSpuFeatures(spuFeatures);
        if (result>0){
            isFlag = true;
        }
        return isFlag;
    }


}
