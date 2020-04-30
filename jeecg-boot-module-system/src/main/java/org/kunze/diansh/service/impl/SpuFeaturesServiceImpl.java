package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.entity.SpuFeatures;
import org.kunze.diansh.mapper.SpuFeaturesMapper;
import org.kunze.diansh.service.ISpuFeaturesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            isFlag = true;
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

    /**
     * 监控每日特卖数据
     */
    @Override
    public void updateOverMonitor() {
        //1、查询热卖表中过期的SkuId
        List<String> skuIds = spuFeaturesMapper.selectFeaturesSkuId();
        if(skuIds != null){
            //2、将过期的热卖商品Sku进行取消
            spuFeaturesMapper.updateSkuFeatures(skuIds,"0");
            //3、删除过期的热卖商品
            spuFeaturesMapper.delFeatures();
        }
    }

    /**
     * 监控每日特卖数据
     */
    @Override
    public void updateMonitor() {
        //1、查询热卖表中过期的SkuId
        List<String> skuIds = spuFeaturesMapper.selectFeaturesSkuIds();
        if (skuIds != null){
            List<String> notSkuIds = spuFeaturesMapper.selectSkuNotState(skuIds);
            if(notSkuIds != null){
                spuFeaturesMapper.updateSkuFeatures(notSkuIds,"0");
            }
        }
    }
}
