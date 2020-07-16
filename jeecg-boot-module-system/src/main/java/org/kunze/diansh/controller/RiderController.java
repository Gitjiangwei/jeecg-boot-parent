package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.entity.Rider;
import org.kunze.diansh.service.IRiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "骑手管理")
@RestController
@RequestMapping(value = "/kunze/rider")
public class RiderController {

    @Autowired
    private IRiderService riderService;


    @ApiOperation("添加骑手信息")
    @AutoLog("添加骑手信息")
    @PostMapping(value = "/saveRider")
    public Result<T> saveRider(@RequestBody Rider rider){
        Result<T> result = new Result<T>();
        Boolean resultOk = riderService.saveRider(rider);
        if(resultOk){
            result.success("添加成功");
        }else {
            result.error500("添加失败！");
        }
        return result;
    }


    @ApiOperation("修改骑手信息")
    @AutoLog("修改骑手信息")
    @PostMapping(value = "/editRider")
    public Result<T> editRider(@RequestBody Rider rider){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(rider.getId())){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = riderService.editRider(rider);
            if (resultOk) {
                result.success("修改成功");
            } else {
                result.error500("修改失败！");
            }
        }
        return result;
    }


    @ApiOperation("查询骑手信息")
    @AutoLog("查询骑手信息")
    @GetMapping(value = "/queryRider")
    public Result<PageInfo<RiderVo>> queryRider(Rider rider,
                                                @RequestParam(name = "pageNo") String pageNo,
                                                @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<RiderVo>> result = new Result<PageInfo<RiderVo>>();
        PageInfo<RiderVo> pageInfo = riderService.queryRiderList(rider,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setSuccess(true);
        result.setResult(pageInfo);
        return result;
    }


    @ApiOperation("删除骑手信息")
    @AutoLog("删除骑手信息")
    @DeleteMapping(value = "/delRider")
    public Result<T> delRider(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(ids)){
            result.error500("参数丢失！");
        }else {
            Boolean isFlag = riderService.delRider(ids);
            if(isFlag){
                result.success("删除成功");
            }else {
                result.error500("删除失败！");
            }
        }
        return result;
    }
}
