package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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
    @GetMapping("/qryCart")
    public Result<List<Cart>> qryCart(){
        Result<List<Cart>> resultList = new Result<List<Cart>>();
        List<Cart> cartList = cartService.queryCart();
        if (null != cartList){
            resultList.setResult(cartList);
        }
        return resultList;
    }

    @ApiOperation("添加商品到购物车")
    @AutoLog("添加商品到购物车")
    @PostMapping("/addCart")
    public Result<Void> addCart(@RequestParam(name = "cart") String cart){
        Result<Void> resultList = new Result<Void>();
        Cart cartObject = JSONObject.parseObject(cart,Cart.class);

        cartService.addCart(cartObject);
        return resultList;
    }


    @ApiOperation("更新购物车商品数量")
    @AutoLog("更新购物车商品数量")
    @PostMapping("/updateCartNum")
    public Result<Void> updateCart(@RequestParam(name = "cart") String cart){
        Result<Void> resultList = new Result<Void>();
        List<Cart> cartList = JSON.parseArray(cart,Cart.class);

        cartService.updateCart(cartList);
        return resultList;
    }


    @ApiOperation("删除购物车商品数量")
    @AutoLog("删除购物车商品数量")
    @PostMapping("/deleteCart")
    public Result<Void> deleteCart(String skuId){
        Result<Void> resultList = new Result<Void>();
        cartService.deleteCart(skuId);
        return resultList;
    }

}
