package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.controller.vo.InformationVo;
import org.kunze.diansh.controller.vo.SalesVo;
import org.kunze.diansh.entity.OrderRecord;
import org.kunze.diansh.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "后台管理系统首页")
@RestController
@RequestMapping(value = "/kunze/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;


    @ApiOperation("门店总排行")
    @GetMapping(value = "/loaderboard")
    public Result<PageInfo<Map<String,String>>> selectStoreLeaderboard(@RequestParam(name = "shopName",required = false) String shopName,
                                                                       @RequestParam(name = "more",defaultValue = "1") String more,
                                                                   @RequestParam(name = "choiceTime",defaultValue = "0")String choiceTime,
                                                                   @RequestParam(name = "pageNo",defaultValue = "1") String pageNo,
                                                                   @RequestParam(name = "pageSize",defaultValue = "7") String pageSize){
        Result<PageInfo<Map<String,String>>> result = new Result<PageInfo<Map<String, String>>>();
        PageInfo<Map<String,String>> mapList = menuService.selectStoreLeaderboard(shopName,more,choiceTime,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setSuccess(true);
        result.setResult(mapList);
        return result;
    }

    @ApiOperation("门店整年销售统计图")
    @GetMapping(value = "/loaderShop")
    public Result<List<String>> selectStoreByShop(@RequestParam(name = "shopId",required = false) String shopId,
                                                  @RequestParam(name = "year",required = false) String year){
        Result<List<String>> result = new Result<List<String>>();
        List<String> stringList = menuService.selectStoreByShop(shopId,year);
        result.setSuccess(true);
        result.setResult(stringList);
        return result;
    }


    @ApiOperation("总订单量和当日完成的订单数")
    @GetMapping(value = "/orderLeader")
    public Result<Map<String,String>> selectOrderLeader(@RequestParam(name = "shopId",required = false) String shopId){
        Result<Map<String,String>> result = new Result<Map<String,String>>();
        Map<String,String> stringMap = menuService.selectOrderLeader(shopId);
        result.setSuccess(true);
        result.setResult(stringMap);
        return result;
    }


    @ApiOperation("总订单量和当日完成的订单数")
    @GetMapping(value = "/orderLeaders")
    public Result<List<Map<String,String>>> selectOrderLeaders(@RequestParam(name = "shopId",required = false) String shopId){
        Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
        List<Map<String,String>> stringMap = menuService.selectOrderLeaders(shopId);
        result.setSuccess(true);
        result.setResult(stringMap);
        return result;
    }


    @ApiOperation("查询销售额")
    @GetMapping(value = "/sales")
    public Result<SalesVo> selectSales(@RequestParam(name = "shopId",required = false) String shopId){
        Result<SalesVo> result = new Result<SalesVo>();
        SalesVo salesVo = menuService.selectSales(shopId);
        result.setSuccess(true);
        result.setResult(salesVo);
        return result;
    }


    @ApiOperation("查询平台信息数据统计")
    @GetMapping(value = "/selectInfo")
    public Result<InformationVo> selectInfo(@RequestParam(name = "shopId") String shopId){
        Result<InformationVo> result = new Result<InformationVo>();
        if(shopId==null || shopId.equals("")){
            result.error500("参数丢失！");
        }else {
            InformationVo informationVo = menuService.selectInfo(shopId);
            result.setSuccess(true);
            result.setResult(informationVo);
        }
        return result;
    }

    @ApiOperation("超市订单统计")
    @GetMapping(value = "/selectOrderstatistics")
    public Result<Map<String,String>> selectOrderstatistics(@RequestParam(name = "shopId") String shopId){
        Result<Map<String,String>> result = new Result<Map<String,String>>();
        Map<String,String> stringMap = menuService.selectOrderstatistics(shopId);
        result.setSuccess(true);
        result.setResult(stringMap);
        return result;
    }


    @ApiOperation("超市库存统计")
    @GetMapping(value = "/selectWarehouseStatistics")
    public Result<Map<String,String>> selectWarehouseStatistics(@RequestParam(name = "shopId") String shopId){
        Result<Map<String,String>> result = new Result<Map<String,String>>();
        Map<String,String> stringMap = menuService.selectWarehouseStatistics(shopId);
        result.setSuccess(true);
        result.setResult(stringMap);
        return result;
    }


    @ApiOperation("超市库存统计")
    @GetMapping(value = "/selectSevenDeal")
    public Result<List<Map<String,String>>> selectSevenDeal(@RequestParam(name = "shopId") String shopId){
        Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
        List<Map<String,String>> mapList = menuService.selectSevenDeal(shopId);
        result.setSuccess(true);
        result.setResult(mapList);
        return result;
    }


    @ApiOperation("查询库存不足的商品")
    @GetMapping(value = "/selectStock")
    public Result<PageInfo<Map<String,Object>>> selectStock(@RequestParam(name = "shopId")String shopId,
                                                            @RequestParam(name = "title",required = false) String title,
                                                            @RequestParam(name = "enable",required = false) String enable,
                                                            @RequestParam(name = "pageNo")String pageNo,
                                                            @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<Map<String,Object>>> result = new Result<PageInfo<Map<String, Object>>>();
        if(StringUtils.isEmpty(shopId.trim())){
            result.error500("参数丢失！");
        }else {
            PageInfo<Map<String, Object>> pageInfo = menuService.selectStock(shopId,title,enable,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            result.setSuccess(true);
            result.setResult(pageInfo);
        }
        return result;
    }


    @ApiOperation("查询最新消息")
    @GetMapping(value = "/queryOrderRecordTotal")
    public Result<PageInfo<Map<String,Object>>> queryOrderRecordTotal(@RequestParam(name = "shopId")String shopId,
                                                               @RequestParam(name = "pageNo",defaultValue = "1") String pageNo,
                                                               @RequestParam(name = "pageSize",defaultValue = "20") String pageSize){
        Result<PageInfo<Map<String,Object>>> result = new Result<PageInfo<Map<String,Object>>>();
        PageInfo<Map<String,Object>> pageInfo = menuService.queryOrderRecordTotal(shopId,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setSuccess(true);
        result.setResult(pageInfo);
        return result;
    }
}
