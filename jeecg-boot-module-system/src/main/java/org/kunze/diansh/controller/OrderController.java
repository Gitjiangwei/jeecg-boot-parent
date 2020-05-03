package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "订单")
@RestController
@RequestMapping(value = "/kunze/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车商品集合id
     * @param shopId 店铺id
     * @param userID 用户id
     * @return
     */
    @ApiOperation("创建订单")
    @AutoLog("创建订单")
    @PostMapping(value = "/createOrder")
    public Result<OrderBo> createOrder(@RequestParam("aid") String aid, @RequestParam("cids") String cids,
                                       @RequestParam("shopId")  String shopId,@RequestParam("userID")  String userID){
        Result<OrderBo> orderResult = new Result<OrderBo>();
        List cidsList = JSON.parseArray(cids);
        OrderBo order = orderService.createOrder(aid,cidsList,shopId,userID);
        if(null != order){
            orderResult.success("创建成功！");
            orderResult.setResult(order);
        }else{
            orderResult.error500("创建失败！");
        }
        return orderResult;
    }


    @ApiOperation("根据订单状态查询订单")
    @AutoLog("根据订单状态查询订单")
    @PostMapping(value = "/queryOrder")
    public Result<List<Order>> queryOrder(String status, String userID, String shopID){
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
}
