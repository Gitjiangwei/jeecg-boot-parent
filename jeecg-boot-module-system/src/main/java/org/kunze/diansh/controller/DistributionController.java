package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.RiderDistBo;
import org.kunze.diansh.controller.vo.RiderAndroidVo;
import org.kunze.diansh.controller.vo.RiderDistVo;
import org.kunze.diansh.service.IDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "配送信息")
@RestController
@RequestMapping(value = "/kunze/dist")
public class DistributionController {

    @Autowired
    private IDistributionService distributionService;


    /***
     * 批量结算
     * jw
     * @param ids
     * @return
     */
    @ApiOperation("批量结算骑手账单")
    @AutoLog("批量结算骑手账单")
    @DeleteMapping(value = "/editDist")
    public Result<T> editDistribution(@RequestParam(name = "ids") String ids){
        Result<T> result = new Result<T>();
        if(StringUtils.isEmpty(ids)){
            result.error500("参数丢失！");
        }else {
            Boolean resultOk = distributionService.editDistribution(ids);
            if (resultOk){
                result.success("结算成功");
            }else {
                result.error500("结算失败");
            }
        }
        return result;
    }

    /***
     * 查询配送信息
     * jw
     * @param riderDistBo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation("后台查询配送信息")
    @AutoLog("后台查询配送信息")
    @GetMapping(value = "/qeruyDistList")
    public Result<PageInfo<RiderDistVo>> qeruyDistList(RiderDistBo riderDistBo,@RequestParam(name = "pageNo") String pageNo,
                                                       @RequestParam(name = "pageSize") String pageSize){
        Result<PageInfo<RiderDistVo>> result = new Result<PageInfo<RiderDistVo>>();
        PageInfo<RiderDistVo> pageInfo = distributionService.qeruyDistList(riderDistBo,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        result.setSuccess(true);
        result.setResult(pageInfo);
        return result;
    }

    /***
     * 骑手查询订单信息
     * jw
     * @param orderId
     * @return
     */
    @ApiOperation("骑手查询配送信息")
    @AutoLog("骑手查询配送信息")
    @PostMapping(value = "/androidDist")
    public Result<RiderAndroidVo> queryRiderAndroid(@RequestParam(name = "orderId") String orderId){
        Result<RiderAndroidVo> result = new Result<RiderAndroidVo>();
        RiderAndroidVo riderAndroidVo = distributionService.queryRiderAndroid(orderId);
        result.setSuccess(true);
        result.setResult(riderAndroidVo);
        return result;
    }
}
