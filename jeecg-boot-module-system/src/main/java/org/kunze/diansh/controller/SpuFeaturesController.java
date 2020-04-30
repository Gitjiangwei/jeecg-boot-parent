package org.kunze.diansh.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.SpuFeaturesBo;
import org.kunze.diansh.controller.vo.SpuFeaturesDetailVo;
import org.kunze.diansh.controller.vo.SpuFeaturesVo;
import org.kunze.diansh.service.ISpuFeaturesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品类型")
@RestController
@RequestMapping(value = "/kunze/features")
public class SpuFeaturesController  {

    @Autowired
    private ISpuFeaturesService spuFeaturesService;


    @AutoLog("添加类型商品")
    @ApiOperation("添加类型商品")
    @PostMapping(value = "/saveSpuFeatures")
    public Result<T> saveSpuFeatures(@RequestBody SpuFeaturesBo spuFeaturesBo){
        Result<T> result = new Result<T>();
        if(spuFeaturesBo.getSpuId()==null || spuFeaturesBo.getSpuId().equals("")){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = spuFeaturesService.saveSpuFeatures(spuFeaturesBo);
            if(resultOk){
                result.success("添加成功");
            }else {
                result.error500("添加失败");
            }
        }
        return result;
    }


    @ApiOperation("首页查询热卖商品")
    @AutoLog("首页查询热卖商品")
    @PostMapping(value = "/queryFeats")
    public Result<List<SpuFeaturesVo>> selectFeatures(@RequestParam(name = "shopId") String shopId,
                                                      @RequestParam(name = "more",defaultValue = "1") String more){
        Result<List<SpuFeaturesVo>> result = new Result<List<SpuFeaturesVo>>();
        if(shopId==null || shopId.equals("")){
            result.error500("参数丢失！");
        }else {
            List<SpuFeaturesVo> list = spuFeaturesService.selectFeatures(shopId,more);
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }


    public Result<SpuFeaturesDetailVo> selectFeaturesDetail(@RequestParam(name = "featuresId") String featuresId){
        Result<SpuFeaturesDetailVo> result = new Result<>();
        if(featuresId == null || featuresId.equals("")){
            result.error500("参数丢失！");
        }else {
            SpuFeaturesDetailVo spuFeaturesDetailVo = spuFeaturesService.selectFeaturesDetail(featuresId);
            result.setResult(spuFeaturesDetailVo);
            result.setSuccess(true);
        }
        return result;
    }
}
