package org.kunze.diansh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Order> createOrder(String aid, String[] cids, String shopId, String userID){
        Result<Order> orderResult = new Result<Order>();
        Order order = orderService.createOrder(aid,cids,shopId,userID);
        if(null != order){
            orderResult.success("创建成功！");
            orderResult.setResult(order);
        }else{
            orderResult.error500("创建失败！");
        }
        return orderResult;
    }
}
