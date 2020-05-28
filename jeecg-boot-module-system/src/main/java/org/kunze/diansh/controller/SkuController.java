package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.service.ISkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/kunze/sku")
public class SkuController {

    @Autowired
    private ISkuService skuService;

    @PostMapping(value = "/qrySkuBySpuId")
    public Result qrySkuBySpuId(@RequestParam(value = "spuId",required = false) String spuId){
        Result result = new Result();
        if (EmptyUtils.isEmpty(spuId)){
            return result.error500("参数丢失！");
        }
        List<Map<String,Object>> skuInfo = skuService.selectSkuInfoBySpuId(spuId);
        if (EmptyUtils.isNotEmpty(skuInfo)){
            result.setResult(skuInfo);
            return result;
        }
        return result;
    }
}
