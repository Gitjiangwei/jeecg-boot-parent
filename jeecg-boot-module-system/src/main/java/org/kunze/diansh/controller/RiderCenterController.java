package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.RiderFlow;
import org.kunze.diansh.entity.RiderState;
import org.kunze.diansh.entity.Riders;
import org.kunze.diansh.service.IRiderCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "骑手管理")
@RestController
@RequestMapping(value = "/kunze/riders/center")
public class RiderCenterController {

    @Autowired
    private IRiderCenterService ridersService;

    @ApiOperation("查询骑手流水")
    @AutoLog("查询骑手流水")
    @GetMapping(value = "/queryRiderFlowList")
    public Result<List<RiderFlow>> queryRiderFlowList(RiderFlow riderFlow) {
        Result<List<RiderFlow>> result = new Result<List<RiderFlow>>();
        List<RiderFlow> riderFlowList=ridersService.queryRiderFlowList(riderFlow);
        result.setResult(riderFlowList);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("查询骑手信息")
    @AutoLog("查询骑手信息")
    @GetMapping(value = "/queryRiderInfo")
    public Result<Riders> queryRiderInfo(Riders rider) {
        Result<Riders> result = new Result<Riders>();
        Riders riderInfo = ridersService.queryRiderInfo(rider);
        result.setSuccess(true);
        result.setResult(riderInfo);
        return  result;

    }


    @ApiOperation("查询骑手资金管理")
    @AutoLog("查询骑手资金管理")
    @GetMapping(value = "/queryRiderMoney")
    public Result<RiderFlow> queryRiderMoneySum(RiderFlow riderFlow) {
            Result<RiderFlow> result = new Result<RiderFlow>();
            RiderFlow riderMoneyList = ridersService.queryRiderMoneySum(riderFlow);
            result.setSuccess(true);
            result.setResult(riderMoneyList);
            return  result;

    }


    @ApiOperation("查询骑手接单状态")
    @AutoLog("查询骑手接单状态")
    @GetMapping(value = "/queryState")

    public Result<RiderState> queryRiderState(RiderState riderState){
        Result<RiderState> result = new Result<RiderState>();
        RiderState  riderStates= ridersService.queryRiderState(riderState);
        result.setSuccess(true);
        result.setResult(riderStates);
        return result;
    }


    @ApiOperation("修改骑手接单状态")
    @AutoLog("修改骑手接单状态")
    @PostMapping(value = "/update")
    public  Result  updateRiderState(@RequestBody RiderState riderState)
    {

        Result<RiderState> result = new Result<RiderState>();
        int resultFlag = ridersService.updateRiderState(riderState);
        if(resultFlag>0){
            result.success("update success！");
        }else{
            result.error500("update fail！");
        }
        return result;
    }


    @ApiOperation("查看历史账单")
    @AutoLog("查看历史账单")
    @GetMapping(value = "/queryHistoryList")
    public Result<List<RiderFlow>> queryHistoryList(RiderFlow riderFlow) {
        Result<List<RiderFlow>> result = new Result<List<RiderFlow>>();
        List<RiderFlow> riderFlowList=ridersService.queryRiderHistoryList(riderFlow);
        result.setResult(riderFlowList);
        result.setSuccess(true);
        return result;
    }

}
