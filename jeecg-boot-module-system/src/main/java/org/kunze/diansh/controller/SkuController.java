package org.kunze.diansh.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.service.ISkuService;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "SKU")
@RestController
@RequestMapping(value = "/kunze/sku")
public class SkuController {

    @Autowired
    private ISkuService skuService;

    @Autowired
    private IStockService stockService;

    @PostMapping(value = "/qrySkuBySpuId")
    public Result<List<Map<String,Object>>> qrySkuBySpuId(@RequestParam(value = "spuId",required = false) String spuId){
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

    @AutoLog("修改商品库存")
    @PostMapping(value = "/updateStock")
    public Result<T> updateStock(@RequestBody Stock stock){
      Result<T> result = new Result<T>();
      if(StringUtils.isEmpty(stock.getSkuId())){
          result.error500("参数丢失！");
      }else {
          Boolean resultOk = stockService.updateStock(stock);
          if(resultOk){
              result.success("库存添加成功！");
          }else {
              result.error500("库存添加失败！");
          }
      }
      return result;
    }

    @ApiOperation("添加SKU规格")
    @AutoLog("添加SKU规格")
    @PostMapping(value = "/saveSku")
    public Result<T> saveSku(@RequestBody SkuVo skuVo){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(skuVo.getSpuId())){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = skuService.saveSku(skuVo);
            if(resultOk){
                result.success("规格添加成功");
            }else {
                result.error500("规格添加失败");
            }
        }
        return result;
    }

}
