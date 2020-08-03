package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.RiderSend;
import org.kunze.diansh.entity.Riders;
import org.kunze.diansh.service.IRiderSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = "骑手派送管理")
@RestController
@RequestMapping(value = "/kunze/riders/send")

public class RiderSendController {

    @Autowired
    private IRiderSendService riderSendService;

    @ApiOperation("商店选择派送添加")
    @AutoLog("商店选择派送添加")
    @PostMapping(value = "/saveRiderSend")
    public Result<T> saveRiderFlowList(@RequestBody RiderSend riderSend) {
        Result<T> result = new Result<T>();
        Boolean riderSendList=riderSendService.saveRiderSend(riderSend);
        if (riderSendList){
            result.success("添加完成");
        }else {
            result.error500("添加失败！");
        }
      return result;
    }


    @ApiOperation("查看派送订单")
    @AutoLog("查看派送订单")
    @GetMapping(value = "/queryRiderOrder")
    public  Result<RiderSend> queryRiderOrder(RiderSend riderSend)
    {
        Result<RiderSend> result = new Result<RiderSend>();
        RiderSend rider=riderSendService.queryRiderOrder(riderSend);
        result.success("query success!");
        result.setResult(rider);
        return result;
    }

    @ApiOperation("修改骑手配送状态")
    @AutoLog("修改骑手状态")
    @PostMapping(value = "/update")

    public  Result  updateState(@RequestBody RiderSend riderSend)
    {

        Result<RiderSend> result = new Result<RiderSend>();
        int resultFlag = riderSendService.updateState(riderSend);
        if(resultFlag>0){
            result.success("update success！");
        }else{
            result.error500("update fail！");
        }
        return result;
    }

    @ApiOperation("查看历史订单")
    @AutoLog("查看历史订单")
    @GetMapping(value = "/queryHitoryOrderList")
    public Result<List<RiderSend>> queryHitoryOrderList(RiderSend riderSend)
    {
        Result<List<RiderSend>> result = new Result<List<RiderSend>>();
        List<RiderSend> riderSendList=riderSendService.queryHitoryOrderList(riderSend);
        result.setResult(riderSendList);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("查看骑手是否空闲")
    @AutoLog("查看骑手是否空闲")
    @GetMapping(value = "/queryRiderState")
    public Result<List<Riders>> queryRiderState(Riders riders)
    {
        Result<List<Riders>> result = new Result<List<Riders>>();
        List<Riders> riderstate=riderSendService.queryRiderState(riders);
        result.setResult(riderstate);
        result.setSuccess(true);
        return result;
    }



    @ApiOperation("修改订单配送状态")
    @AutoLog("修改订单配送状态")
    @PostMapping(value = "/updateIsTurn")
    public  Result  updateIsTurn(@RequestBody RiderSend riderSend)
    {

        Result<RiderSend> result = new Result<RiderSend>();
        int resultFlag = riderSendService.updateIsTurn(riderSend);
        if(resultFlag>0){
            result.success("update success！");
        }else{
            result.error500("update fail！");
        }
        return result;
    }


}



