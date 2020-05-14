package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.kunze.diansh.controller.vo.SalesVo;
import org.kunze.diansh.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NameParser;
import java.util.List;
import java.util.Map;

@Api(tags = "后台管理系统首页")
@RestController
@RequestMapping(value = "/kunze/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;


    @ApiOperation("门店总排行")
    @GetMapping(value = "/loaderboard")
    public Result<List<Map<String,String>>> selectStoreLeaderboard(@RequestParam(name = "more",defaultValue = "1") String more,
                                                                   @RequestParam(name = "choiceTime",defaultValue = "0")String choiceTime){
        Result<List<Map<String,String>>> result = new Result<List<Map<String, String>>>();
        List<Map<String,String>> mapList = menuService.selectStoreLeaderboard(more,choiceTime);
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
}
