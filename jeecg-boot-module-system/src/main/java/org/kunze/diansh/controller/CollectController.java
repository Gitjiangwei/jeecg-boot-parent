package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Collect;
import org.kunze.diansh.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 购物车控制层
 */
@Api(tags = "商品收藏")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/collect")
public class CollectController {

    @Autowired
    private ICollectService collectService;

    @ApiOperation("添加商品收藏记录")
    @AutoLog("添加商品收藏记录")
    @PostMapping("/insertCollect")
    public Result insertCollect(@RequestBody @Valid Collect collect, BindingResult bindingResult){
        Result result = new Result();
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }
        Collect collect1 = collectService.isCollect(collect.getUserId(),collect.getGoodId(),collect.getShopId());
        if(EmptyUtils.isNotEmpty(collect1)){
            return result.error500("该商品已加入收藏！");
        }
        String resultStr = collectService.insertCollect(collect);
        if(EmptyUtils.isNotEmpty(resultStr)){
            result.success("添加成功！");
            result.setResult(resultStr);
        }else {
            result.error500("添加时出现错误，请重试！");
        }
        return result;
    }

    @ApiOperation("删除商品收藏记录")
    @AutoLog("删除商品收藏记录")
    @PostMapping("/deleteCollect")
    public Result deleteCollect(@RequestBody List<String> list){
        Result result = new Result();
        if(EmptyUtils.isEmpty(list)){
            return Result.error("参数为空！");
        }
        Boolean flag = collectService.deleteCollect(list);
        if(flag){
            result.success("删除成功！");
        }else {
            result.error500("删除时出现错误，请重试！");
        }
        return result;
    }

    @ApiOperation("查询当前用户的收藏商品")
    @AutoLog("查询当前用户的收藏商品")
    @PostMapping("/selectCollectByUId")
    public Result selectCollectByUId(@RequestParam(name = "userId")String userId,@RequestParam(name = "shopId")String shopId){
        Result result = new Result();
        if(EmptyUtils.isEmpty(userId)){
            return Result.error("参数为空！");
        }
        if(EmptyUtils.isEmpty(shopId)){
            return Result.error("参数为空！");
        }
        List<Map<String,Object>> resultMap = collectService.selectCollectByUId(userId,shopId);
        if(EmptyUtils.isNotEmpty(resultMap))
            result.setResult(resultMap);
        return result;
    }

    @ApiOperation("查询当前用户收藏商品总数")
    @AutoLog("查询当前用户收藏商品总数")
    @PostMapping("/countCollectByUId")
    public Result countCollectByUId(@RequestParam(name = "userId")String userId,@RequestParam(name = "shopId")String shopId){
        Result result = new Result();
        if(EmptyUtils.isEmpty(userId)){
            return Result.error("参数为空！");
        }
        Integer countNum = collectService.countCollectByUId(userId,shopId);
        result.setResult(countNum);
        return result;
    }

    @ApiOperation("查询当前商品是否被收藏")
    @AutoLog("查询当前用户收藏商品总数")
    @PostMapping("/isCollect")
    public Result isCollect(@RequestParam(name = "userId")String userId,
                            @RequestParam(name = "spuId")String spuId,
                            @RequestParam(name = "shopId")String shopId){
        Result result = new Result();
        if(EmptyUtils.isEmpty(userId)){
            return result.error500("用户id不能为空！");
        }
        if(EmptyUtils.isEmpty(spuId)){
            return result.error500("商品id不能为空！");
        }
        Collect collect = collectService.isCollect(userId,spuId,shopId);
        if(EmptyUtils.isNotEmpty(collect)){
            result.setResult(collect.getId());
            result.setSuccess(true);
        }
        return result;
    }


}
