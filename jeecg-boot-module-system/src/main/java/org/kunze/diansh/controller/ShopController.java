package org.kunze.diansh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysPosition;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;
import org.kunze.diansh.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "超市管理")
@RestController
@RequestMapping(value = "/kunze/shop")
public class ShopController {


    @Autowired
    private IShopService shopService;

    @ApiOperation("客户端查询所有超市信息")
    @AutoLog("查询所有超市")
    @PostMapping(value = "/queryShops")
    public Result<PageInfo<ShopVo>> queryShopList(ShopVo shopVo,
                                                  @RequestParam(name = "pageNo") Integer pageNo,
                                                  @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<ShopVo>> result = new Result<PageInfo<ShopVo>>();
        shopVo.setIsFlag("1");
        PageInfo<ShopVo> shopVoPageInfo = shopService.queryShopList(shopVo,pageNo,pageSize);
        result.setSuccess(true);
        result.setResult(shopVoPageInfo);
        return result;
    }


    @AutoLog(value = "后台超市表-分页列表查询")
    @ApiOperation(value = "超市表-分页列表查询", notes = "超市表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<PageInfo<ShopVo>> queryShList(ShopVo shop,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
/*        Result<IPage<KzShop>> result = new Result<IPage<KzShop>>();
        QueryWrapper<KzShop> queryWrapper = QueryGenerator.initQueryWrapper(shop, req.getParameterMap());
        Page<KzShop> page = new Page<KzShop>(pageNo, pageSize);
        IPage<KzShop> pageList = shopService.page(page,queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);*/
        Result<PageInfo<ShopVo>> result = new Result<PageInfo<ShopVo>>();
        PageInfo<ShopVo> shopVoPageInfo = shopService.queryShopList(shop,pageNo,pageSize);
        result.setSuccess(true);
        result.setResult(shopVoPageInfo);
        return result;
    }


    @GetMapping(value = "/lists")
    public Result<List<ShopVo>> queryShopLists(){
        Result<List<ShopVo>> result = new Result<List<ShopVo>>();
        List<ShopVo> shopVoList = shopService.queryShopLists();
        result.setResult(shopVoList);
        result.setSuccess(true);
        return result;
    }


    @ApiOperation("添加超市信息")
    @AutoLog("添加超市信息")
    @PostMapping(value = "/insertShop")
    public Result<T> insertShop(@RequestBody KzShop shop){
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
    public Result<T> updateShop(@RequestBody KzShop shop) {
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


    @ApiOperation("删除超市信息")
    @AutoLog("删除超市")
    @DeleteMapping(value = "/delShops")
    public Result<T> delShops(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(ids==null||ids.equals("")){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = shopService.delShops(ids);
            if (resultOk){
                result.success("删除成功！");
            }else {
                result.error500("删除失败！");
            }
        }
        return result;
    }
}
