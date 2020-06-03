package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.WheelBo;
import org.kunze.diansh.controller.vo.WheelVo;
import org.kunze.diansh.entity.Wheel;
import org.kunze.diansh.service.IWheelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "轮播图")
@RequestMapping(value = "/kunze/wheel")
public class WheelController {

    @Autowired
    private IWheelService wheelService;


    @ApiOperation("添加轮播图片")
    @AutoLog("添加轮播图片")
    @PostMapping(value = "/saveWheel")
    public Result<T> saveWheel(@RequestBody WheelBo wheelBo){
        Result<T> result = new Result<T>();
        if(wheelBo.getWheelPort()== null || wheelBo.getWheelPort().equals("")){
            result.error500("参数丢失！");
        }else if(Integer.parseInt(wheelBo.getWheelPort())>3 || Integer.parseInt(wheelBo.getWheelPort()) < 0){
            result.error500("参数被篡改！");
        }else {
            Boolean resultOk = wheelService.saveWheel(wheelBo);
            if(resultOk){
                result.success("添加成功！");
            }else{
                result.error500("添加失败！");
            }
        }
        return result;
    }

/*    @ApiOperation("管理图片查询轮播图")
    @AutoLog("管理界面查询轮播图")
    @GetMapping(value = "/pageWheelList")
    public Result<PageInfo<Wheel>> queryWheelPage(WheelVo wheelVo,
                                              @RequestParam(name = "pageNo") Integer pageNo,
                                              @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<Wheel>> result = new Result<PageInfo<Wheel>>();
        PageInfo<Wheel> wheelPageInfo = wheelService.queryWheel(wheelVo,pageNo,pageSize);
        result.setSuccess(true);
        result.setResult(wheelPageInfo);
        return result;
    }*/

    @ApiOperation("首页查询轮播图")
    @AutoLog("首页查询轮播图")
    @GetMapping(value = "/homeWheelList")
    public  Result<PageInfo<Wheel>> queryWheelHome(WheelVo wheelVo,
                                              @RequestParam(name = "pageSize",defaultValue = "5") Integer pageSize){
        Result<PageInfo<Wheel>> result = new Result<PageInfo<Wheel>>();
        if(wheelVo.getWheelPort()== null || wheelVo.getWheelPort().equals("")){
            result.error500("参数丢失！");
        }else if(Integer.parseInt(wheelVo.getWheelPort())>3 || Integer.parseInt(wheelVo.getWheelPort()) < 0) {
            result.error500("参数被篡改！");
        }else {
            wheelVo.setIsFlag("0");
            PageInfo<Wheel> wheelPageInfo = wheelService.queryWheel(wheelVo, 1, pageSize);
            result.setSuccess(true);
            result.setResult(wheelPageInfo);
        }
        return result;
    }

    @ApiOperation("修改轮播图片")
    @AutoLog("修改轮播图")
    @PostMapping(value = "/updateWheel")
    public Result<T> updateWheel(@RequestBody WheelBo wheelBo){
        Result<T> result = new Result<T>();
        if(wheelBo.getWheelId()==null || wheelBo.getWheelId().equals("")){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = wheelService.updateWheel(wheelBo);
            if(resultOk){
                result.success("修改成功！");
            }else{
                result.error500("修改失败！");
            }
        }
        return result;
    }


    @ApiOperation("删除轮播图片")
    @AutoLog("删除轮播图片")
    @DeleteMapping(value = "/delWheel")
    public Result<T> delWheel(@RequestParam(name = "id") String id){
        Result<T> result = new Result<T>();
        if(id == null || id.equals("")){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = wheelService.delWheel(id);
            if(resultOk){
                result.success("删除成功");
            }else{
                result.error500("删除失败");
            }
        }
        return result;
    }

    @ApiOperation("批量删除轮播图片")
    @AutoLog("批量删除轮播图片")
    @DeleteMapping(value = "/delWheels")
    public Result<T> delWheels(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(ids == null || ids.equals("")){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = wheelService.delWheels(ids);
            if(resultOk){
                result.success("批量删除成功");
            }else{
                result.error500("批量删除失败");
            }
        }
        return result;
    }


    @ApiOperation("首页轮播图展示打开或关闭")
    @AutoLog("首页轮播图展示打开或关闭")
    @PostMapping(value = "/updateIsFlag")
    public Result<T> updateIsFlag(@RequestBody String enable){
        Result<T> result = new Result<T>();
        JSONObject jsonObject = JSONObject.parseObject(enable);
        String isFlag = jsonObject.get("isFlag").toString();
        String ids = jsonObject.get("ids").toString();
        if (isFlag==null || isFlag.equals("")){
            result.error500("参数丢失！");
        }else if(ids == null || ids.equals("")){
            result.error500("参数丢失！");
        }else{
            if(!isFlag.equals("0") && !isFlag.equals("1")){
                result.error500("非法参数");
            }else {
                Boolean resultOk = wheelService.updateIsFlag(isFlag, ids);
                if (resultOk) {
                    if (isFlag.equals("1")) {
                        result.success("图片已关闭，将不在首页展示");
                    } else {
                        result.success("图片将展示在首页");
                    }
                }
            }
        }
        return  result;
    }


    @AutoLog("查询轮播图列表")
    @ApiOperation("后台查询轮播图列表")
    @GetMapping(value = "/pageWheelList")
    public Result<PageInfo<Wheel>> qeryWheelbackstage(WheelBo wheelBo,
                                                      @RequestParam(name = "pageNo") Integer pageNo,
                                                      @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<Wheel>> result = new Result<PageInfo<Wheel>>();
        PageInfo<Wheel> pageInfo = wheelService.qeryWheelbackstage(wheelBo,pageNo,pageSize);
        result.setResult(pageInfo);
        result.setSuccess(true);
        return result;
    }

    @GetMapping(value = "/selectByShopId")
    public Result<List<String>> selectByShopId(@RequestParam(name = "id") String id){
        Result<List<String>> result = new Result<List<String>>();
        List<String> stringList = wheelService.selectByShopId(id);
        result.setSuccess(true);
        result.setResult(stringList);
        return result;
     }
}
