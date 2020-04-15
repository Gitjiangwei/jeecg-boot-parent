package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.JsonUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.CollectionUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.entity.Cart;
import org.kunze.diansh.entity.Sku;
import org.kunze.diansh.mapper.CartMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {


    //购物车前缀
    private static final String KEY_PREFIX = "kunze:cart:uid:";

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    /**
     * 查询购物车
     * @param
     * @return
     */
    @Override
    public List<Cart> queryCart() {
        List<Cart> cartList = new ArrayList<>();
        //获取用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String key = KEY_PREFIX + "456789";
        if(!redisTemplate.hasKey(key)){
            return null;
        }
        BoundHashOperations<String,Object,Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }

        return carts.stream().map(o -> JSONObject.parseObject(o.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 添加商品到购物车
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //登录用户对象
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //String userKey = KEY_PREFIX + sysUser.getId();
        String userKey = KEY_PREFIX+"456789";
        BoundHashOperations<String,Object,Object> hashOps = this.redisTemplate.boundHashOps(userKey);

        String skuId = cart.getSkuid(); //商品id
        Integer num = cart.getCartNum(); //购买数量
        boolean isExist = hashOps.hasKey(skuId);
        if(isExist){
            //redis中存在
            String json = hashOps.get(skuId).toString();
            cart = JSONObject.parseObject(json,Cart.class);

            // 修改购物车数量
            cart.setCartNum(cart.getCartNum() + num);
        }else{
            //不存在
            cart.setUserId("456789");

            Sku sku = skuMapper.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":sku.getImages().split(",")[0]);
            cart.setCartPrice(sku.getPrice());//商品价格
            cart.setTitile(sku.getTitle()); //标题
            cart.setCartNum(num); //商品数量
            cart.setOwnSpec(sku.getOwnSpec()); //详细规格参数
        }
        //写入Redis
        hashOps.put(cart.getSkuid().toString(),JSONObject.toJSONString(cart));
    }

    /**
     * 修改购物车商品数量
     * @param skuId 商品id
     * @param cartNum 商品数量
     */
    @Override
    public void updateCart(String skuId, Integer cartNum) {
        //登录用户对象
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String key = KEY_PREFIX+"456789";
        BoundHashOperations<String,Object,Object> hashOps = this.redisTemplate.boundHashOps(key);

        //获取商品对象
        String cartJson = hashOps.get(skuId).toString();
        Cart cart = JSONObject.parseObject(cartJson,Cart.class);
        cart.setCartNum(cartNum);

        //写入Redis
        hashOps.put(skuId,JSONObject.toJSONString(cart));
    }

    /**
     * 删除购物车商品
     * @param skuId 商品id
     */
    @Override
    public void deleteCart(String skuId) {
        //登录用户对象
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String key = KEY_PREFIX+"456789";
        BoundHashOperations<String,Object,Object> hashOps = this.redisTemplate.boundHashOps(key);
        //从Redis中删除
        hashOps.delete(skuId);
    }

}
