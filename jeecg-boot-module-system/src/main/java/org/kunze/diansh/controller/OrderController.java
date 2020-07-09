package org.kunze.diansh.controller;

import com.github.pagehelper.PageInfo;
import com.alibaba.fastjson.JSONObject;
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
import org.kunze.diansh.controller.bo.OrderParams;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.OrderVo;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            @ApiImplicitParam(name="payType",value = "付款类型 0.微信 1.支付宝")
    })
    public Result<Order> createOrder(@RequestBody @Valid OrderParams params,BindingResult bindingResult){
        Result<Order> orderResult = new Result<Order>();
        //参数校验
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1,m2)->","+m2)
                    .orElse("输入参数有误！");
            throw new IllegalArgumentException(messages);
        }
        Boolean flag = orderService.selectOrderByUserId(params.getUserID(),params.getShopId());
        if(flag){
            return orderResult.error500("您有未支付的订单，请勿重复支付！");
        }

        Order order = orderService.createOrder(params);
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


    @ApiOperation("后台管理系统查询订单")
    @AutoLog("后台管理系统查询订单")
    @GetMapping(value = "/selectOrder")
    public Result<PageInfo<OrderVo>> selectOrder(@RequestParam(name = "orderId",required = false) String orderId,
                                                 @RequestParam(name = "status",required = false) String status,
                                                 @RequestParam(name = "telphone",required = false) String telphone,
                                                 @RequestParam(name = "shopId") String shopId,
                                                 @RequestParam(name = "pageNo") Integer pageNo,
                                                 @RequestParam(name = "pageSize") Integer pageSize){
        Result<PageInfo<OrderVo>> result = new Result<PageInfo<OrderVo>>();
        PageInfo<OrderVo> orderVoPageInfo = orderService.selectOrder(shopId,status,telphone,orderId,pageNo,pageSize);
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
        String resultOk = orderService.updateOrderStatu(status,orderId);
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

    /**
     * 取消订单
     * 订单只允许在未支付时取消
     * @return
     */
    @ApiOperation("取消订单(只能取消未支付订单)")
    @PostMapping(value = "/cancelOrder")
    public Result cancelOrder(@RequestParam(name = "orderId")String orderId,
                              @RequestParam(name = "userId")String userId){
        Result result = new Result();
        if(EmptyUtils.isEmpty(orderId)){
            return result.error500("订单号参数不能为空！");
        }
        if(EmptyUtils.isEmpty(userId)){
            return result.error500("用户id不能为空！");
        }
        Order order = orderService.selectById(orderId);
        if(EmptyUtils.isEmpty(order)){
            return result.error500("订单不存在！");
        }
        if(!"1".equals(order.getStatus().toString())){
            return result.error500("当前订单状态不可取消！");
        }else{
            //从队列删除
            OrderComsumer.removeToOrderDelayQueue(orderId);
            orderService.updateOrderStatu("6",orderId);
        }
        return Result.ok("取消成功！");
    }


    @ApiOperation("再来一单时获取商品最新数据")
    @AutoLog("再来一单时获取商品最新数据")
    @PostMapping(value = "/againOrder")
    public Result<Map<String,Object>> againOrder(@RequestParam(name = "orderId") String orderId,
                                         @RequestParam(name = "userID")String userID,
                                         @RequestParam(name = "shopID") String shopID){
        Result<Map<String,Object>> result = new Result<Map<String,Object>>(){};

        if(orderId == null || orderId.equals("")){
            return result.error500("参数丢失");
        }
        if(shopID == null || shopID.equals("")){
            return result.error500("参数丢失");
        }
        try{
            Map<String,Object> map = orderService.againOrder(orderId,userID,shopID);
            result.setResult(map);
            result.setSuccess(true);
        }catch (Exception e){
            result.error500("获取数据时出现错误！");
        }
        return  result;
    }
}
