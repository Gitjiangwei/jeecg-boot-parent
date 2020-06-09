package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.OrderComsumer;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.common.util.OrderCodeUtils;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.OrderVo;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "订单")
@RestController
@RequestMapping(value = "/kunze/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /**
     * 创建订单
     *  aid 地址id
     *  cids 购物车商品集合id
     *  shopId 店铺id
     *  userID 用户id
     *  pick_up 配送方式
     * @return
     */
    @ApiOperation("创建订单")
    @AutoLog("创建订单")
    @PostMapping(value = "/createOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userID",value = "用户id"),
            @ApiImplicitParam(name="shopId",value = "店铺id"),
            @ApiImplicitParam(name="aid",value = "地址id"),
            @ApiImplicitParam(name="pick_up",value = "配送方式 1自提 2商家配送"),
            @ApiImplicitParam(name="postFree",value = "配送费 单位（元）"),
            @ApiImplicitParam(name="cids",value = "购物车商品的集合"),
    })
    public Result<Order> createOrder(@RequestBody JSONObject params){
        Result<Order> orderResult = new Result<Order>();
        String userID = params.get("userID").toString();
        String shopId = params.get("shopId").toString();
        String aid = params.get("aid").toString();
        String pick_up = params.get("pick_up").toString();
        String postFree = params.getString("postFree");
        JSONArray cids = params.getJSONArray("cids");

        if(EmptyUtils.isEmpty(cids)){
            return orderResult.error500("cids参数丢失！");
        }
        if(EmptyUtils.isEmpty(shopId)){
            return orderResult.error500("店铺参数丢失！");
        }
        if(EmptyUtils.isEmpty(userID)){
            return orderResult.error500("用户参数丢失！");
        }
        if(EmptyUtils.isEmpty(pick_up)){
            return orderResult.error500("配送参数丢失！");
        }
        Order order = orderService.createOrder(aid,cids,shopId,userID,pick_up,postFree);
        if(null != order){
            orderResult.success("创建成功！");
            orderResult.setResult(order);
            //创建订单成功后加入队列
            OrderComsumer.queue.put(order);
        }else{
            orderResult.error500("创建失败！");
        }
        return orderResult;
    }


    @ApiOperation("根据订单状态查询订单")
    @AutoLog("根据订单状态查询订单")
    @PostMapping(value = "/queryOrder")
    public Result<List<Order>> queryOrder(@RequestParam(name = "status") String status,
                                          @RequestParam(name = "userID")String userID,
                                          @RequestParam(name = "shopID") String shopID){
        Result<List<Order>> result = new Result<List<Order>>(){};

        if(userID == null || userID.equals("")){
            return result.error500("参数丢失");
        }
        if(shopID == null || shopID.equals("")){
            return result.error500("参数丢失");
        }
        try{
            List<Order> orderList = orderService.selectOrderByStatus(status,userID,shopID);
            result.setSuccess(true);
            result.setResult(orderList);
        }catch (Exception e){
            result.error500("查询订单时出现错误！");
        }
        return  result;
    }


    @ApiOperation("根据订单ID查询订单")
    @AutoLog("根据订单ID查询订单")
    @PostMapping(value = "/selectOrderById")
    public Result<Order> selectOrderById(@RequestParam(name = "orderId") String orderId,
                                          @RequestParam(name = "userID")String userID,
                                          @RequestParam(name = "shopID") String shopID){
        Result<Order> result = new Result<Order>(){};

        if(orderId == null || orderId.equals("")){
            return result.error500("参数丢失");
        }
        if(shopID == null || shopID.equals("")){
            return result.error500("参数丢失");
        }
        try{
            Order order = orderService.selectOrderById(orderId,userID,shopID);
            result.setResult(order);
            result.setSuccess(true);
        }catch (Exception e){
            result.error500("查询订单时出现错误！");
        }
        return  result;
    }



    @ApiOperation("付款后修改订单状态为【已付款】")
    @AutoLog("付款成功后修改订单状态")
    @PostMapping(value = "/updatePayMent")
    public Result<T> updateOrderStatus(@RequestParam(name = "orderId") String orderId){
        Result<T> result = new Result<T>();
        if(orderId == null || orderId.equals("")){
            result.error500("参数丢失！");
        }else {
            String resultOk = orderService.updateOrderStatus(orderId);
            if(resultOk.equals("error")){
                result.error500("出现未知异常！");
            }else if (resultOk.equals("OK")){
                result.success("下单成功！");
            }
        }
        return result;
    }


    @ApiOperation("后台管理系统查询订单")
    @AutoLog("后台管理系统查询订单")
    @GetMapping(value = "/selectOrder")
    public Result<PageInfo<OrderVo>> selectOrder(@RequestParam(name = "orderId",required = false) String orderId,
                                                 @RequestParam(name = "status",required = false) String status,
                                                 @RequestParam(name = "shopId") String shopId,
                                                 @RequestParam(name = "pageNo") Integer pageNo,
                                                 @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<OrderVo>> result = new Result<PageInfo<OrderVo>>();
        PageInfo<OrderVo> orderVoPageInfo = orderService.selectOrder(shopId,status,orderId,pageNo,pageSize);
        result.setResult(orderVoPageInfo);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("后台管理系统查询详情")
    @AutoLog("后台管理系统查询订单详情")
    @GetMapping(value = "/selectOrderDetail")
    public Result<OrderDetailVo> selectOrderDetail(@RequestParam(name = "orderId") String orderId){
        Result<OrderDetailVo> result = new Result<OrderDetailVo>();
        OrderDetailVo orderDetailVo = orderService.selectOrderDetail(orderId);
        result.setResult(orderDetailVo);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("修改订单状态")
    @AutoLog("修改订单状态")
    @PostMapping(value = "/updateStatus")
    public Result<T> updatesOrderStatus(@RequestBody String orderStatus){
        Result<T> result = new Result<T>();
        JSONObject jsonObject = JSONObject.parseObject(orderStatus);
        String status = jsonObject.getString("status");
        String orderId = jsonObject.getString("orderId");
        String resultOk = orderService.updateOrderStatus(status,orderId);
        if(resultOk.equals("ok")){
            result.success("ok");
        }else {
            result.error500("error");
        }
        return result;
    }


    @ApiOperation("商家查看订单记录")
    @GetMapping(value = "/queryOrderRecord")
    public Result<List<Map<String,String>>> queryOrderRecord(@RequestParam(name = "orderId") String orderId){
        Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
        List<Map<String,String>> mapList = orderService.queryOrderRecord(orderId);
        result.setResult(mapList);
        result.setSuccess(true);
        return result;
    }
}
