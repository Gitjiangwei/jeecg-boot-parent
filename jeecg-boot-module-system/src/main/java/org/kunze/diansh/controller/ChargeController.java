package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.Charge;
import org.kunze.diansh.service.IChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "手续费接口")
@RestController
@RequestMapping(value = "/kunze/charge")
public class ChargeController {



    @Autowired
    private IChargeService chargeService;


    /***
     * 添加手续费
     * @param jsonObject
     * @return
     */
    @ApiOperation("添加手续费")
    @AutoLog("添加手续费")
    @PostMapping(value = "/saveCharge")
    public Result<T> saveCharge(@RequestBody JSONObject jsonObject){
        Result<T> result = new Result<T>();
        String serviceCharge = jsonObject.getString("serviceCharge");
        String shopId = jsonObject.getString("shopId");
        if(StringUtils.isEmpty(serviceCharge)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = chargeService.saveCharge(shopId,serviceCharge);
            if(resultOk){
                result.success("添加成功！");
            }else {
                result.error500("添加失败！");
            }
        }
        return result;
    }

    /**
     * 修改手续费
     * @param charge
     * @return
     */
    @ApiOperation("修改手续费")
    @AutoLog("修改手续费")
    @PostMapping(value = "/updateCharge")
    public Result<T> updateCharge(@RequestBody Charge charge){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(charge.getId())){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = chargeService.updateCharge(charge);
            if(resultOk){
                result.success("修改成功！");
            }else {
                result.error500("修改失败！");
            }
        }
        return result;
    }

    /***
     * 查询手续费
     * @return
     */
    @ApiOperation("查询手续费")
    @AutoLog("查询手续费")
    @GetMapping(value = "/queryCharge")
    public Result<Map<String,String>> selectCharge(@RequestParam(name = "shopId",required = false) String shopId){
        Result<Map<String,String>> result = new Result<Map<String,String>>();
        if(!StringUtils.isEmpty(shopId)){
            Map<String,String> map = chargeService.selectCharge(shopId);
            result.setResult(map);
            result.setSuccess(true);
        }else {
            result.setResult(null);
            result.setSuccess(true);
        }
        return result;
    }
}
