package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.HotelSku;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.service.ISkuService;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @ApiOperation("通过id删除sku")
    @AutoLog("通过id删除sku")
    @PostMapping(value = "/delSkuById")
    public Result delSkuById(@RequestParam(name = "id")String id){
        Result result = new Result();
        if(EmptyUtils.isEmpty(id)){
            return result.error500("id is not null");
        }
        Boolean isSuccess = skuService.delSkuById(id);
        if(isSuccess){
            result.success("删除成功！");
        }else{
            result.error500("删除失败！");
        }
        return result;
    }

    @ApiOperation("添加sku 类型为餐饮")
    @AutoLog("添加sku 类型为餐饮")
    @PostMapping(value = "/addHotelSku")
    public Result addHotelSku(@RequestBody HotelSku hotelSku, BindingResult bindingResult){
        Result<T> result = new Result<T>();
        //参数校验
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }
        Boolean resultFlag = skuService.addHotelSku(hotelSku);
        if(resultFlag){
            result.success("添加成功！");
        }else {
            result.error500("添加失败！");
        }
        return result;
    }

    @ApiOperation("修改sku 类型为餐饮")
    @AutoLog("修改sku 类型为餐饮")
    @PostMapping(value = "/updateHotelSku")
    public Result updateHotelSku(@RequestBody HotelSku hotelSku){
        Result<T> result = new Result<T>();
        if(EmptyUtils.isEmpty(hotelSku.getId())){
            return result.error500("id is not null!");
        }
        Boolean resultFlag = skuService.updateHotelSku(hotelSku);
        if(resultFlag){
            result.success("修改成功！");
        }else {
            result.error500("修改失败！");
        }
        return result;
    }

    @ApiOperation("删除sku 通过id 类型为餐饮")
    @AutoLog("删除sku 通过id 类型为餐饮")
    @PostMapping(value = "/delHotelSkuById")
    public Result delHotelSkuById(@RequestParam String id){
        Result<T> result = new Result<T>();
        if(EmptyUtils.isEmpty(id)){
            return result.error500("id is not null!");
        }
        Boolean resultFlag = skuService.delHotelSkuById(id);
        if(resultFlag){
            result.success("删除成功！");
        }else {
            result.error500("删除失败！");
        }
        return result;
    }

    @ApiOperation("查询sku 通过店铺id 类型为餐饮")
    @AutoLog("查询sku 通过店铺id 类型为餐饮")
    @PostMapping(value = "/queryHotelSku")
    public Result queryHotelSku(HotelSku hotelSku,
                                @RequestParam Integer pageNo,
                                @RequestParam Integer pageSize){
        Result result = new Result();
        if(EmptyUtils.isEmpty(hotelSku.getShopId())){
            return result.error500("shopId is not null!");
        }
        PageInfo<Map<String,Object>> resultList = skuService.queryHotelSku(hotelSku,pageNo,pageSize);
        if(EmptyUtils.isNotEmpty(resultList)){
            result.setResult(resultList);
        }else {
            result.error500("error!");
        }
        return result;
    }


    @ApiOperation("查询sku 通过分类id 类型为餐饮")
    @AutoLog("查询sku 通过分类id 类型为餐饮")
    @PostMapping(value = "/queryHotelSkuByCid")
    public Result queryHotelSkuByCid(@RequestParam String shopId,
                                     @RequestParam String cid,
                                     @RequestParam String saleable){
        Result result = new Result();
        if(EmptyUtils.isEmpty(shopId)){
            return result.error500("shopId is not null!");
        }
        if(EmptyUtils.isEmpty(cid)){
            return result.error500("cid is not null!");
        }
        List<Map<String,Object>> resultList = skuService.queryHotelSkuByCid(shopId,cid,saleable);
        if(EmptyUtils.isNotEmpty(resultList)){
            result.setResult(resultList);
        }else {
            result.error500("error!");
        }
        return result;
    }

    @ApiOperation("通过id查询hotelSku 类型为餐饮")
    @AutoLog("通过id查询hotelSku 类型为餐饮")
    @PostMapping(value = "/queryHotelById")
    public Result queryHotelById(@RequestParam String id){
        Result result = new Result();
        if(EmptyUtils.isEmpty(id)){
            return result.error500("id is not null!");
        }
        HotelSku hotelSku = skuService.queryHotelById(id);
        if(EmptyUtils.isNotEmpty(hotelSku)){
            result.setResult(hotelSku);
        }else {
            result.error500("error!");
        }
        return result;
    }
}
