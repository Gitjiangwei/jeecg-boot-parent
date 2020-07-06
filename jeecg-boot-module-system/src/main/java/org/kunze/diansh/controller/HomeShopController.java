package org.kunze.diansh.controller;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.HomeShopVo;
import org.kunze.diansh.entity.HomeShop;
import org.kunze.diansh.service.IHomeShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "超市分类专区管理")
@RestController
@RequestMapping(value = "/kunze/homeShop")
public class HomeShopController {

    @Autowired
    private IHomeShopService homeShopService;



    /**
     * 超市查询分类专区
     * @param shopId
     * @param homgName
     * @return
     */
    @ApiOperation("超市查询分类专区")
    @AutoLog("超市查询分类专区")
    @GetMapping(value = "/queryHomeShop")
    public Result<PageInfo<HomeShopVo>> queryHomeShop(@RequestParam(name = "shopId") String shopId,
                                                      @RequestParam(name = "homgName",required = false) String homgName,
                                                      @RequestParam(name = "pageNo") Integer pageNo,
                                                      @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<HomeShopVo>> result = new Result<PageInfo<HomeShopVo>>();
        if(StringUtils.isEmpty(shopId)){
            result.error500("超市ID丢失！");
        }else {
            PageInfo<HomeShopVo> shopVoPageInfo = homeShopService.queryHomeShop(shopId,homgName,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            result.setResult(shopVoPageInfo);
            result.setSuccess(true);
        }
        return result;

    }

    /**
     * 超市添加分类专区
     * @param homeShop
     * @return
     */
    @ApiOperation("超市添加分类专区")
    @AutoLog("超市添加分类专区")
    @PostMapping(value = "/saveHomeShop")
    public Result<T> saveHomeShop(@RequestBody HomeShop homeShop){
        Result<T> result = new Result<T>();
        Boolean resultOk = homeShopService.saveHomeShop(homeShop);
        if(resultOk){
            result.success("添加成功");
        }else {
            result.error500("添加失败");
        }
        return result;
    }


    /**
     * 超市修改分类专区
     * @param homeShop
     * @return
     */
    @ApiOperation("超市修改分类专区")
    @AutoLog("超市修改分类专区")
    @PostMapping(value = "/editHomeShop")
    public Result<T> editHomeShop(@RequestBody HomeShop homeShop){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(homeShop.getId())){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homeShopService.editHomeShop(homeShop);
            if (resultOk) {
                result.success("添加成功");
            } else {
                result.error500("添加失败");
            }
        }
        return result;
    }

    /**
     * 超市删除分类专区
     * @param ids
     * @return
     */
    @ApiOperation("超市删除分类专区")
    @AutoLog("超市删除分类专区")
    @DeleteMapping(value = "/delHomeShop")
    public Result<T> delHomeShop(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(ids)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homeShopService.delHomeShop(ids);
            if (resultOk) {
                result.success("删除成功");
            } else {
                result.error500("删除失败");
            }
        }
        return result;
    }


    /**
     * 检索当前超市下是否存在相同的专区
     * @param homePageId
     * @param shopId
     * @return
     */
    @ApiOperation("检索当前超市下是否存在相同的专区")
    @GetMapping(value = "/queryNotHomeShop")
    public Result<T> queryNotHomeShop(@RequestParam(name = "homePageId") String homePageId,
                                      @RequestParam(name = "shopId") String shopId){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(homePageId)){
            result.error500("参数丢失！");
        }else if(StringUtils.isEmpty(shopId)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homeShopService.queryNotHomeShop(homePageId,shopId);
            if (resultOk) {
                result.success("不能添加相同的分类专区！");
            } else {
                result.error500("ok");
            }
        }
        return result;
    }
}
