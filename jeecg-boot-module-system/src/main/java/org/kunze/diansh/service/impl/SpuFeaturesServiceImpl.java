package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
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
}
