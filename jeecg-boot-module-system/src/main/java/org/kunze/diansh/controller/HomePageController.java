package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.DealInfoVo;
import org.kunze.diansh.entity.HomePage;
import org.kunze.diansh.service.IHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "首页分类专区管理")
@RestController
@RequestMapping(value = "/kunze/page")
public class HomePageController {

    @Autowired
    private IHomePageService homePageService;

    /**
     * 添加分类专区
     * @param homePage
     * @return
     */
    @ApiOperation("添加分类专区")
    @AutoLog("添加分类专区")
    @PostMapping(value = "/insertPage")
    public Result<T> insertHomePage(@RequestBody HomePage homePage){
        Result<T> result = new Result<T>();
        Boolean resultOk = homePageService.insertHomePage(homePage);
        if(resultOk){
            result.success("添加成功！");
        }else {
            result.error500("添加失败！");
        }
        return result;
    }

    /**
     * 修改分类专区
     * @param homePage
     * @return
     */
    @ApiOperation("修改分类专区")
    @AutoLog("修改分类专区")
    @PostMapping(value = "/updatePage")
    public Result<T> updateHomePage(@RequestBody HomePage homePage){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(homePage.getId())){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homePageService.updateHomePage(homePage);
            if (resultOk) {
                result.success("修改成功！");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /**
     * 查询分类专区
     */
    @ApiOperation("后台查询分类专区")
    @AutoLog("后台查询分类专区")
    @GetMapping(value = "/queryPage")
    public Result<PageInfo<HomePage>> queryHomePage(@RequestParam(name = "homgName",required = false) String homgName,
                                                      @RequestParam(name = "pageNo") String pageNo,
                                                      @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<HomePage>> result = new Result<PageInfo<HomePage>>();
        PageInfo<HomePage> pagePageInfo = homePageService.queryHomePage(homgName,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setResult(pagePageInfo);
        result.setSuccess(true);
        return result;
    }


    /**
     * 删除分类专区
     * @param ids
     * @return
     */
    @ApiOperation("删除分类专区")
    @AutoLog("删除分类专区")
    @DeleteMapping(value = "/delPage")
    public Result<T> delHomgPage(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(ids)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homePageService.delHomgPage(ids);
            if(resultOk){
                result.success("删除成功！");
            }else {
                result.error500("删除失败！");
            }
        }
        return result;
    }

    /**
     * 检索正在使用的专区
     * @param jsonObject
     * @return
     */
    @ApiOperation("检索正在使用的专区")
    @PostMapping(value = "/queryNotPage")
    public Result<T> queryNotPage(@RequestBody JSONObject jsonObject){
        Result<T> result = new Result<T>();
        String homePageId = jsonObject.getString("homePageId");
        if(StringUtils.isEmpty(homePageId)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = homePageService.queryNotPage(homePageId);
            if(resultOk){
                result.error500("您删除的专区正在使用，无法删除");
            }else {
                result.success("ok");
            }
        }
        return result;
    }
}
