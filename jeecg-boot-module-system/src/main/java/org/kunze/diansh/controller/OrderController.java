package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.OrderComsumer;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.common.util.OrderCodeUtils;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "订单")
@RestController
@RequestMapping(value = "/kunze/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;


//    @RequestParam("aid") String aid, @RequestParam("cids") String cids,
//    @RequestParam("shopId")  String shopId,@RequestParam("userID")  String userID
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
    public Result<Order> createOrder(@RequestBody JSONObject params){
        Result<Order> orderResult = new Result<Order>();
        String userID = params.get("userID").toString();
        String shopId = params.get("shopId").toString();
        String aid = params.get("aid").toString();
        String pick_up = params.get("pick_up").toString();
        List<String> cids = params.getJSONArray("cids").toJavaList(String.class);

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
        Order order = orderService.createOrder(aid,cids,shopId,userID,pick_up);
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
        if(userID == null || userID.equals("")){
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
}
