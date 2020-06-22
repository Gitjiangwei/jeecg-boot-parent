package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.Cart;
import org.kunze.diansh.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车控制层
 */
@Api(tags = "购物车模块")
@Slf4j
@RestController
@RequestMapping(value = "/kunze/cart")
public class CartController {

    @Autowired
    private ICartService cartService;


    @ApiOperation("查询购物车")
    @AutoLog("查询购物车")
    @PostMapping("/qryCart")
    public Result<List<Cart>> qryCart(@RequestParam String shopId){
        Result<List<Cart>> resultList = new Result<List<Cart>>();
        if(EmptyUtils.isEmpty(shopId)){
            return resultList.error500("参数不能为空！");
        }
        List<Cart> cartList = cartService.queryCart(shopId);
        resultList.setResult(cartList);
        return resultList;
    }

    @ApiOperation("添加商品到购物车")
    @AutoLog("添加商品到购物车")
    @PostMapping("/addCart")
    public Result<Void> addCart(@RequestBody Cart cart){
        Result<Void> resultList = new Result<Void>();
        if(EmptyUtils.isEmpty(cart.getShopId())){
            return resultList.error500("店铺参数丢失！");
        }
        cartService.addCart(cart);
        return resultList;
    }


    @ApiOperation("更新购物车商品数量")
    @AutoLog("更新购物车商品数量")
    @PostMapping("/updateCartNum")
    public Result<Void> updateCart(@RequestBody Cart cart){
        Result<Void> resultList = new Result<Void>();
        if(EmptyUtils.isEmpty(cart.getShopId())){
            return resultList.error500("店铺参数丢失！");
        }
        cartService.updateCart(cart);
        return resultList;
    }


    @ApiOperation("删除购物车商品")
    @AutoLog("删除购物车商品")
    @PostMapping("/deleteCart")
    @ApiImplicitParam(name = "skuIdList",value = "商品'skuId'的集合")
    public Result<Void> deleteCart(@RequestParam(name = "skuIdList") List<String> skuIdList,@RequestParam(name = "shopId")String shopId){
        Result<Void> resultList = new Result<Void>();
        if(EmptyUtils.isEmpty(shopId)){
            return resultList.error500("店铺参数丢失！");
        }
        cartService.deleteCart(skuIdList,shopId);
        return resultList;
    }

}
