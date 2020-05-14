package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.OrderCodeUtils;
import org.jeecg.modules.message.mapper.SysUserShopMapper;
import org.jeecg.modules.message.websocket.WebSocket;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.pust.Demo;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    CartServiceImpl cartService = new CartServiceImpl();

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private SysUserShopMapper sysUserShopMapper;


    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车集合
     * @param shopId 店铺id
     * @param userID 用户id
     */
    @Override
    @Transactional
    public OrderBo createOrder(String aid, List cids, String shopId, String userID) {
        //当前时间
        Date date = new Date();
        //根据aid查找相关的地址信息
        Address address = addressMapper.selectAddressByID(aid);

        //根据cids获取购买的物品的信息
        List<Cart> cartList = cartService.selectCartByCids(cids,userID);

        //总价格
        Integer totalPrice = 0;

        //计算订单总价
        for (Cart c:cartList) {
            BigDecimal unitPrice = new BigDecimal(c.getCartPrice());
            BigDecimal num = new BigDecimal(c.getCartNum());
            BigDecimal price = unitPrice.multiply(num);
            totalPrice = totalPrice+price.intValue();
        }

        Integer orderNum = orderMapper.selectShopOrderNum(shopId);

        //订单
        Order order = new Order();
        KzShop shop = shopMapper.selectByKey(shopId);
        if(shop == null){
            return  null;
        }
        //生成订单号
        order.setOrderId(OrderCodeUtils.orderCode(shop.getShopName(),orderNum.toString()));
        order.setShopId(shopId); //店铺id
        order.setUserID(userID); //用户id

        order.setAddressId(aid); //地址
        order.setCreateTime(date);//创建时间
        order.setUpdateTime(date);//更新时间
        order.setAmountPayment(totalPrice.toString()); //应付金额
        order.setPayment("0"); //实付金额
        order.setStatus(1); //订单状态 未付款

        //插入订单数据
        Integer rows = orderMapper.insertOrder(order);
        if (rows != 1){
            new Exception("创建订单失败！插入订单时出现未知错误");
        }

        List<OrderDetail> odList = new ArrayList<OrderDetail>();
        for (Cart cart:cartList) {
            OrderDetail od = new OrderDetail();
            od.setId(UUID.randomUUID().toString().replace("-",""));
            od.setOrderId(order.getOrderId());
            od.setSkuId(cart.getSkuid());
            od.setNum(cart.getCartNum());
            od.setTitle(cart.getTitile());
            od.setOwnSpec(cart.getOwnSpec());
            od.setPrice(Integer.parseInt(cart.getCartPrice()));
            od.setImage(cart.getImage());
            Integer odRows = orderMapper.insertOrderDetail(od);
            odList.add(od);
            if (rows != 1){
                new Exception("创建订单失败！插入订单时出现未知错误");
            }
        }
        OrderBo result = new OrderBo();
        result.setAddress(address);
        result.setOrder(order);
        result.setOdList(odList);
        return result;
    }

    /**
     * 根据订单状态查询订单数据
     * @param status 订单状态
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    @Override
    public List<Order> selectOrderByStatus(String status, String userID, String shopID) {
        List<Order>  orders = orderMapper.selectOrderByStatus(status,userID,shopID);
        if(orders.size() != 0){
            for (int  i=0;i<orders.size();i++) {
                Order order = orders.get(i);
                List<OrderDetail> odlist = orderMapper.selectOrderDetailById(order.getOrderId());
                order.setOdList(odlist);
                orders.set(i,order);
            }
        }
        return orders;
    }

    /***
     * 订单支付后修改状态
     * @param orderId
     * @return
     */
    @Override
    public String updateOrderStatus(String orderId) {
        String flag = "error";
        //1、修改订单状态为【已支付】
        int orderStatus = orderMapper.updateOrderStatus("2",orderId);
        if(orderStatus > 0){
            //获取订单信息
            Order order = orderMapper.selectById(orderId);
            if(order==null){
                return flag;
            }
            List<String> stringList = sysUserShopMapper.selectByIds(order.getShopId());
            JSONObject obj = new JSONObject();
            if(stringList.size()>0 && stringList.size() == 1){
                //通过WebScoket进行发送
                obj.put("cmd","user");
                obj.put("userId",stringList.get(0));
                obj.put("msgId", "M0001");
                obj.put("msgTxt", "您有一条新的订单，请注意查收");
                webSocket.sendOneMessage(stringList.get(0),obj.toJSONString());
                //通过友盟进行消息推送
                Demo demo = new Demo("5eb2184adbc2ec0856ab2aac","xe2gzni0gkjesy8mfomucngiddpiumm1");
                try {
                    demo.sendAndroidBroadcast();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {

            }
            flag = "OK";
        }
        return flag;
    }


}
