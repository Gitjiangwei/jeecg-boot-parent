package org.kunze.diansh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Cart;

import java.util.List;

public interface ICartService extends IService<Cart> {

    /**
     * 购物车查询
     * @param shopId
     * @return
     */
    List<Cart> queryCart(String shopId);

    /**
     * 添加商品到购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 修改购物车商品数量
     * @param cart
     */
    void updateCart(Cart cart);

    /**
     * 删除购物车商品
     * @param skuId 商品id
     */
    void deleteCart(List skuId,String shopId);

}
