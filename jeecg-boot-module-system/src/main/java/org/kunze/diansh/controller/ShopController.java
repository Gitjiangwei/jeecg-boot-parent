package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.Shop;
import org.kunze.diansh.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "超市管理")
@RestController
@RequestMapping(value = "/kunze/shop")
public class ShopController {


    @Autowired
    private IShopService shopService;

    @ApiOperation("查询所有超市信息")
    @AutoLog("查询所有超市")
    @GetMapping(value = "/queryShops")
    public Result<PageInfo<ShopVo>> queryShopList(ShopVo shopVo,
                                                  @RequestParam(name = "pageNo") Integer pageNo,
                                                  @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<ShopVo>> result = new Result<PageInfo<ShopVo>>();
        PageInfo<ShopVo> shopVoPageInfo = shopService.queryShopList(shopVo,pageNo,pageSize);
        result.setSuccess(true);
        result.setResult(shopVoPageInfo);
        return result;
    }


    @ApiOperation("添加超市信息")
    @AutoLog("添加超市信息")
    @PostMapping(value = "/insertShop")
    public Result<T> insertShop(@RequestBody Shop shop){
        Result<T> result = new Result<T>();
        Boolean resultOk = shopService.insertShop(shop);
        if(resultOk){
            result.success("添加成功");
        }else {
            result.error500("添加失败");
        }
        return result;
    }


    @ApiOperation("修改超市信息")
    @AutoLog("修改超市信息")
    @PostMapping(value = "updateShop")
    public Result<T> updateShop(@RequestBody Shop shop) {
        Result<T> result = new Result<T>();
        if (shop.getId() == null || shop.getId().equals("")) {
            result.error500("参数丢失！");
        } else {
            Boolean resultOk = shopService.updateShop(shop);
            if (resultOk) {
                result.success("修改成功");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }
}
