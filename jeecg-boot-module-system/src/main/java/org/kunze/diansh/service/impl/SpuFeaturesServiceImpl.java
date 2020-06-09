package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.*;
import org.kunze.diansh.entity.SpuFeatures;
import org.kunze.diansh.entity.modelData.SpuFeaturesDetailModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesIdsModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesListModel;
import org.kunze.diansh.entity.modelData.SpuFeaturesModel;
import org.kunze.diansh.mapper.SpuFeaturesMapper;
import org.kunze.diansh.service.ISpuFeaturesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SpuFeaturesServiceImpl extends ServiceImpl<SpuFeaturesMapper, SpuFeatures> implements ISpuFeaturesService {

    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;

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
            if(spuFeaturesBo.getSkuId()!=null && !spuFeaturesBo.getSkuId().equals("")){
                List<String> stringList = new ArrayList<>();
                stringList.add(spuFeaturesBo.getSkuId());
                result = spuFeaturesMapper.updateSkuFeatures(stringList,"1");
                if(result > 0){
                    isFlag = true;
                }
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
            List<SpuFeaturesVo> spuFeaturesVos = spuFeaturesMapper.selectFeatures(shopId,more);
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
        skuFeaturesVo.setPrice(spuFeaturesIdsModel.getFeaturesPrice());
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
                BigDecimal ordPrice = new BigDecimal(item.getFeaturesPrice());
                BigDecimal newPrice = ordPrice.divide(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
                spuFeaturesListModel.setFeaturesPrice(newPrice.toString());
                String ownSpec = item.getOwnSpec().substring(1,item.getOwnSpec().length()-1);
                spuFeaturesListModel.setOwnSpec(ownSpec);
                spuFeaturesListModelList.add(spuFeaturesListModel);
            }
            PageInfo<SpuFeaturesListModel> pageInfo = new PageInfo<SpuFeaturesListModel>(spuFeaturesListModelList);
            pageInfo.setTotal(page.getTotal());
            return pageInfo;
        }
    }


}
